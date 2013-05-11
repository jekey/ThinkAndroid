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
package com.ta.util.bitmap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import com.ta.util.TALogger;
import com.ta.util.cache.TAProcessDataHandler;

public abstract class TAProcessBitmapHandler extends TAProcessDataHandler
{
	// ��ѹ��ͼƬ�����̵���Ĭ�ϸ�ʽ
	private static final CompressFormat DEFAULT_COMPRESS_FORMAT = CompressFormat.JPEG;
	private static final int DEFAULT_COMPRESS_QUALITY = 70;

	@Override
	public byte[] processData(Object data)
	{
		// TODO Auto-generated method stub
		byte[] buffer = null;
		Bitmap bitmap = processBitmap(data);
		InputStream is = null;
		if (bitmap != null)
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(DEFAULT_COMPRESS_FORMAT, DEFAULT_COMPRESS_QUALITY,
					baos);
			is = new ByteArrayInputStream(baos.toByteArray());
			try
			{
				buffer = readStream(is);
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				TALogger.d(TAProcessBitmapHandler.this, "processData" + "ʧ��");
			}
		}
		return buffer;
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

	protected abstract Bitmap processBitmap(Object data);

}
