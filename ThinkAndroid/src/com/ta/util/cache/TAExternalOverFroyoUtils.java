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

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.ta.common.AndroidVersionCheckUtils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

/**
 * @Title TAExternalOverFroyoUtils
 * @Package com.ta.util.cache
 * @Description ����Ĺ�����,Android 2.2���ϰ汾ʹ��
 * @author ��è
 * @date 2013-1-20
 * @version V1.0
 */
@TargetApi(9)
public class TAExternalOverFroyoUtils
{
	/**
	 * �ж��Ƿ�����ⲿ�洢�豸
	 * 
	 * @return ��������ڷ���false
	 */
	public static boolean hasExternalStorage()
	{
		Boolean externalStorage = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		return externalStorage;
	}

	/**
	 * ��ȡĿ¼ʹ�õĿռ��С
	 * 
	 * @param path
	 *            ����·��·��
	 * @return ���ֽڵĿ��ÿռ�
	 */
	public static long getUsableSpace(File path)
	{
		if (AndroidVersionCheckUtils.hasGingerbread())
		{
			return path.getUsableSpace();
		}
		final StatFs stats = new StatFs(path.getPath());
		return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
	}

	/**
	 * ����ⲿӦ�ó��򻺴�Ŀ¼
	 * 
	 * @param context
	 *            ��������Ϣ
	 * @return �ⲿ����Ŀ¼
	 */
	public static File getExternalCacheDir(Context context)
	{
		if (AndroidVersionCheckUtils.hasFroyo())
		{
			return context.getExternalCacheDir();
		}
		final String cacheDir = "/Android/data/" + context.getPackageName()
				+ "/cache/";
		return new File(Environment.getExternalStorageDirectory().getPath()
				+ cacheDir);
	}

	/**
	 * �������ⲿ�洢�������õĻ��ǿ��ƶ��ġ�
	 * 
	 * @return ����ⲿ�洢�ǿ��ƶ���(����һ��SD��)����Ϊ true,����false��
	 */
	public static boolean isExternalStorageRemovable()
	{
		if (AndroidVersionCheckUtils.hasGingerbread())
		{
			return Environment.isExternalStorageRemovable();
		}
		return true;
	}

	/**
	 * һ��ɢ�з���,�ı�һ���ַ���(��URL)��һ��ɢ���ʺ�ʹ����Ϊһ�������ļ�����
	 */
	public static String hashKeyForDisk(String key)
	{
		String cacheKey;
		try
		{
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(key.getBytes());
			cacheKey = bytesToHexString(mDigest.digest());
		} catch (NoSuchAlgorithmException e)
		{
			cacheKey = String.valueOf(key.hashCode());
		}
		return cacheKey;
	}

	private static String bytesToHexString(byte[] bytes)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++)
		{
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1)
			{
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	/**
	 * �õ�һ�����õĻ���Ŀ¼(����ⲿ����ʹ���ⲿ,�����ڲ�)��
	 * 
	 * @param context
	 *            ��������Ϣ
	 * @param uniqueName
	 *            Ŀ¼����
	 * @return ����Ŀ¼����
	 */
	public static File getDiskCacheDir(Context context, String uniqueName)
	{
		// ����Ƿ�װ��洢ý�������õ�,���������,����ʹ��
		// �ⲿ���� Ŀ¼
		// ����ʹ���ڲ����� Ŀ¼
		final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()) || !isExternalStorageRemovable() ? getExternalCacheDir(
				context).getPath()
				: context.getCacheDir().getPath();

		return new File(cachePath + File.separator + uniqueName);
	}

	/**
	 * �õ�һ�����õĻ���Ŀ¼(����ⲿ����ʹ���ⲿ,�����ڲ�)��
	 * 
	 * @param context
	 *            ��������Ϣ
	 * @param uniqueName
	 *            Ŀ¼����
	 * @return ����Ŀ¼����
	 */
	public static File getSystemDiskCacheDir(Context context)
	{
		// ����Ƿ�װ��洢ý�������õ�,���������,����ʹ��
		// �ⲿ���� Ŀ¼
		// ����ʹ���ڲ����� Ŀ¼
		final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()) || !isExternalStorageRemovable() ? getExternalCacheDir(
				context).getPath()
				: context.getCacheDir().getPath();

		return new File(cachePath);
	}

	public static int getMemoryClass(Context context)
	{
		return ((ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
	}

}
