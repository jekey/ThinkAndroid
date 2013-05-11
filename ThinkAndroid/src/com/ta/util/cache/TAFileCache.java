/*
 * Copyright (C) 2013  WhiteCat ��è (www.thinkandroid.cn)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ta.util.cache;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.ta.common.AndroidVersionCheckUtils;
import com.ta.util.TALogger;
import android.content.Context;

/**
 * @Title TAFileCache
 * @Package com.ta.util.cache
 * @Description �ļ��Ļ��������,�����ڴ滺�棬����̻���
 * @author ��è
 * @date 2013-1-20
 * @version V1.0
 */
public class TAFileCache
{
	// Ĭ���ڴ洢�洢��С
	private static final int DEFAULT_MEM_CACHE_SIZE = 1024 * 1024 * 5; // 5MB
	// Ĭ�ϴ��̴洢��С
	public static final int DEFAULT_DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB
	// ��ѹ��ͼƬ�����̵���Ĭ�ϸ�ʽ
	private static final int DEFAULT_COMPRESS_QUALITY = 70;
	private static final int DISK_CACHE_INDEX = 0;
	// ��Щ�������������л���ͬ�Ļ���
	private static final boolean DEFAULT_MEM_CACHE_ENABLED = true; // Ĭ���ڴ滺������
	private static final boolean DEFAULT_DISK_CACHE_ENABLED = true; // Ĭ�ϴ��̻�������
	private static final boolean DEFAULT_CLEAR_DISK_CACHE_ON_START = false;// Ĭ�ϵ������Ĵ��̻��濪ʼ
	private static final boolean DEFAULT_INIT_DISK_CACHE_ON_CREATE = false; // Ĭ�ϵĳ�ʼ���Ĵ��̸��ٻ��濪ʼ
	private TACacheParams mCacheParams;
	private boolean mDiskCacheStarting = true;
	private LruCache<String, byte[]> mMemoryCache;
	private DiskLruCache mDiskLruCache;
	private final Object mDiskCacheLock = new Object();

	/**
	 * ����һ���µ�TAFileCache����ʹ��ָ���Ĳ�����
	 * 
	 * @param cacheParams
	 *            �������������ʼ������
	 */
	public TAFileCache(TACacheParams cacheParams)
	{
		init(cacheParams);
	}

	/**
	 * ����һ������
	 * 
	 * @param context
	 *            ��������Ϣ
	 * @param uniqueName
	 *            �����ʾ���ֻᱻ��ӵ����ɾ����ļ���
	 * 
	 */
	public TAFileCache(Context context, String uniqueName)
	{
		init(new TACacheParams(context, uniqueName));
	}

	private void init(TACacheParams cacheParams)
	{
		mCacheParams = cacheParams;
		if (mCacheParams.memoryCacheEnabled)
		{
			mMemoryCache = new LruCache<String, byte[]>(
					mCacheParams.memCacheSize)
			{
				@Override
				protected int sizeOf(String key, byte[] buffer)
				{
					return getSize(key, buffer);
				}

			};
		}

		// ���Ĭ�ϵĴ��̻���û�г�ʼ�����Ǿ���Ҫ��ʼ������һ���������߳����ڴ��̷��ʡ�
		if (!mCacheParams.initDiskCacheOnCreate)
		{
			// Set up disk cache
			initDiskCache();
		}
	}

	/**
	 * ��ʼ�����̻���
	 */
	public void initDiskCache()
	{
		// Set up disk cache
		synchronized (mDiskCacheLock)
		{
			if (mDiskLruCache == null || mDiskLruCache.isClosed())
			{
				File diskCacheDir = mCacheParams.diskCacheDir;
				if (mCacheParams.diskCacheEnabled && diskCacheDir != null)
				{
					if (!diskCacheDir.exists())
					{
						diskCacheDir.mkdirs();
					}
					long usableSpace = 0;
					if (AndroidVersionCheckUtils.hasGingerbread())
					{
						usableSpace = TAExternalOverFroyoUtils
								.getUsableSpace(diskCacheDir);
					} else
					{
						usableSpace = TAExternalUnderFroyoUtils
								.getUsableSpace(diskCacheDir);
					}
					if (usableSpace > mCacheParams.diskCacheSize)
					{
						try
						{

							mDiskLruCache = DiskLruCache.open(diskCacheDir, 1,
									1, DEFAULT_DISK_CACHE_SIZE);
							// mCacheParams.initDiskCacheOnCreate = true;
						} catch (final IOException e)
						{
							mCacheParams.diskCacheDir = null;
							TALogger.e(TAFileCache.this, "initDiskCache - " + e);
						}
					}
				}
			}
			mDiskCacheStarting = false;
			mDiskCacheLock.notifyAll();
		}
	}

	/**
	 * ��� byte[]�������ݵ��ڴ滺��ʹ��̻���
	 * 
	 * @param data
	 *            byte[]��Ωһ��ʶ�����洢,һ����URL
	 * @param buffer
	 *            ��Ҫ��ӵ����������
	 */
	public void addBufferToCache(String data, byte[] buffer)
	{
		if (data == null || buffer == null)
		{
			return;
		}

		// ��ӵ��ڴ滺��
		if (mMemoryCache != null && mMemoryCache.get(data) == null)
		{
			mMemoryCache.put(data, buffer);
		}

		synchronized (mDiskCacheLock)
		{
			// ��ӵ����̻���
			if (mDiskLruCache != null)
			{
				String key = "";
				if (AndroidVersionCheckUtils.hasGingerbread())
				{
					key = TAExternalOverFroyoUtils.hashKeyForDisk(data);
				} else
				{
					key = TAExternalUnderFroyoUtils.hashKeyForDisk(data);
				}
				OutputStream out = null;
				try
				{
					DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
					if (snapshot == null)
					{
						final DiskLruCache.Editor editor = mDiskLruCache
								.edit(key);
						if (editor != null)
						{
							out = editor.newOutputStream(DISK_CACHE_INDEX);
							out.write(buffer, 0, buffer.length);
							editor.commit();
							out.close();
						}
					} else
					{
						snapshot.getInputStream(DISK_CACHE_INDEX).close();
					}
				} catch (final IOException e)
				{
					TALogger.e(TAFileCache.this, "addBufferToCache - " + e);
				} catch (Exception e)
				{
					TALogger.e(TAFileCache.this, "addBufferToCache - " + e);
				} finally
				{
					try
					{
						if (out != null)
						{
							out.close();
						}
					} catch (IOException e)
					{
					}
				}
			}
		}
	}

	/**
	 * ���ڴ滺���ȡ���ݣ�����ڴ滺��û�У�����Ӵ��̻����ȡ��䵽�ڴ滺��
	 * 
	 * @param data
	 *            byte[]��Ωһ��ʶ�����洢,һ����URL
	 * @return ����byte[]���͵�һ������
	 */
	public byte[] getBufferFromMemCache(String data)
	{
		byte[] memValue = null;
		try
		{
			if (mMemoryCache != null)
			{
				memValue = mMemoryCache.get(data);
			}
			/*
			 * if (memValue == null) { memValue =
			 * getInputStreamFromDiskCache(data); // ������ݵ��ڴ滺�� if (memValue !=
			 * null && mMemoryCache != null && mMemoryCache.get(data) == null) {
			 * mMemoryCache.put(data, memValue); } }
			 */
			//
		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
			TALogger.e(TAFileCache.this, "��ȡ��������ʧ�ܣ�");
		}
		return memValue;
	}

	/**
	 * �Ӵ��̻����л�ȡ����
	 * 
	 * @param data
	 *            ���صı�ʶ����Ŀ�õ�
	 * @return ����ڻ������ҵ���Ӧ�����ݣ��򷵻�����,����Ϊnull
	 */
	public byte[] getBufferFromDiskCache(String data)
	{
		String key = "";
		if (AndroidVersionCheckUtils.hasGingerbread())
		{
			key = TAExternalOverFroyoUtils.hashKeyForDisk(data);
		} else
		{
			key = TAExternalUnderFroyoUtils.hashKeyForDisk(data);
		}

		synchronized (mDiskCacheLock)
		{
			while (mDiskCacheStarting)
			{
				try
				{
					mDiskCacheLock.wait();
				} catch (InterruptedException e)
				{
				}
			}
			if (mDiskLruCache != null)
			{
				byte[] buffer = null;
				try
				{
					final DiskLruCache.Snapshot snapshot = mDiskLruCache
							.get(key);
					if (snapshot != null)
					{
						InputStream fileInputStream = snapshot
								.getInputStream(DISK_CACHE_INDEX);

						buffer = readStream(fileInputStream);
						// ������ݵ��ڴ滺��
						if (buffer != null && mMemoryCache != null
								&& mMemoryCache.get(data) == null)
						{
							mMemoryCache.put(data, buffer);
						}
						return buffer;
					}
				} catch (final IOException e)
				{

					TALogger.e(TAFileCache.this, "getBufferFromDiskCache - "
							+ e);
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					TALogger.e(TAFileCache.this, "getBufferFromDiskCache - "
							+ e);
				} finally
				{

				}
			}
			return null;
		}
	}

	/*
	 * �õ�ͼƬ�ֽ��� �����С
	 */
	public static byte[] readStream(InputStream inStream) throws Exception
	{
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1)
		{
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		return outStream.toByteArray();
	}

	/**
	 * λͼ��Ωһ��ʶ�����洢����ڴ�ʹ��̻��涼TAFileCache������ ���� ע��,������Դ��̷���,�������ǲ�Ӧ�������̻߳�
	 * UI�߳���ִ�еġ�
	 */
	public void clearCache()
	{
		if (mMemoryCache != null)
		{
			mMemoryCache.evictAll();
		}

		synchronized (mDiskCacheLock)
		{
			mDiskCacheStarting = true;
			if (mDiskLruCache != null && !mDiskLruCache.isClosed())
			{
				try
				{
					mDiskLruCache.delete();

				} catch (IOException e)
				{
					TALogger.e(TAFileCache.this, "clearCache - " + e);
				}
				mDiskLruCache = null;
				initDiskCache();
			}
		}
	}

	/**
	 * ���̻���ˢ��TAFileCache�����صĶ���ע��,������Դ��̷���,�������ǲ�Ӧ�������̻߳� UI�߳���ִ�еġ�
	 */
	public void flush()
	{
		synchronized (mDiskCacheLock)
		{
			if (mDiskLruCache != null)
			{
				try
				{
					mDiskLruCache.flush();
				} catch (IOException e)
				{
					TALogger.e(TAFileCache.this, "flush - " + e);
				}
			}
		}
	}

	/**
	 * �رմ��̻���������TAFileCache����ע��,������Դ��̷���,�������ǲ�Ӧ�������̻߳� UI�߳���ִ�еġ�
	 */
	public void close()
	{
		synchronized (mDiskCacheLock)
		{
			if (mDiskLruCache != null)
			{
				try
				{
					if (!mDiskLruCache.isClosed())
					{
						mDiskLruCache.close();
						mDiskLruCache = null;
						/*
						 * if (BuildConfig.DEBUG) { Log.d(TAG,
						 * "Disk cache closed"); }
						 */
					}
				} catch (IOException e)
				{
					TALogger.e(TAFileCache.this, "close" + e);
				}
			}
		}
	}

	/**
	 * �ӻ��byte[]�ĳ���
	 * 
	 * @param key
	 * @param value
	 * @return bytes���͵ĳ���
	 */
	private int getSize(String key, byte[] value)
	{
		// TODO Auto-generated method stub
		return value.length;
	}

	/**
	 * @Title TACacheParams
	 * @Package com.ta.util.cache
	 * @Description ����Ĳ�����
	 * @author ��è
	 * @date 2013-1-20
	 * @version V1.0
	 */
	public static class TACacheParams
	{
		public int memCacheSize = DEFAULT_MEM_CACHE_SIZE;
		public int diskCacheSize = DEFAULT_DISK_CACHE_SIZE;
		public File diskCacheDir;
		public int compressQuality = DEFAULT_COMPRESS_QUALITY;
		public boolean memoryCacheEnabled = DEFAULT_MEM_CACHE_ENABLED;
		public boolean diskCacheEnabled = DEFAULT_DISK_CACHE_ENABLED;
		public boolean clearDiskCacheOnStart = DEFAULT_CLEAR_DISK_CACHE_ON_START;
		public boolean initDiskCacheOnCreate = DEFAULT_INIT_DISK_CACHE_ON_CREATE;

		/**
		 * ��ʼ�����̲���
		 * 
		 * @param context
		 *            ������
		 * @param uniqueName
		 *            �����ļ�����
		 */
		public TACacheParams(Context context, String uniqueName)
		{
			if (AndroidVersionCheckUtils.hasGingerbread())
			{
				diskCacheDir = TAExternalOverFroyoUtils.getDiskCacheDir(
						context, uniqueName);
			} else
			{
				diskCacheDir = TAExternalUnderFroyoUtils.getDiskCacheDir(
						context, uniqueName);
			}
		}

		/**
		 * ��ʼ�����̲���
		 * 
		 * @param diskCacheDir
		 *            �����ļ���
		 */
		public TACacheParams(File diskCacheDir)
		{
			this.diskCacheDir = diskCacheDir;
		}

		/**
		 * ���û���Ĵ�С
		 * 
		 * @param context
		 *            ������
		 * @param percent
		 *            ���÷��仺��Ϊ���豸�İٶȱȣ���0.01f����
		 * 
		 */
		public void setMemCacheSizePercent(Context context, float percent)
		{
			if (percent < 0.05f || percent > 0.8f)
			{
				throw new IllegalArgumentException(
						"setMemCacheSizePercent - percent must be "
								+ "between 0.05 and 0.8 (inclusive)");
			}
			memCacheSize = Math.round(percent * getMemoryClass(context) * 1024
					* 1024);
		}

		private static int getMemoryClass(Context context)
		{
			if (AndroidVersionCheckUtils.hasGingerbread())
			{
				return TAExternalOverFroyoUtils.getMemoryClass(context);
			} else
			{
				return TAExternalUnderFroyoUtils.getMemoryClass(context);
			}

		}
	}

}
