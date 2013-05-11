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
package com.ta.util.resoperate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import com.ta.util.TALogger;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @Title TAAssetsOperateUtils
 * @Package com.ta.util.resoperate
 * @Description TAAssetsOperateUtils��һ������Ӧ�ó���Assets����Դ
 * @author ��è
 * @date 2013-1-22 ���� 9:35
 * @version V1.0
 */
public class TAAssetsOperateUtils
{

	private static final String TAG = "TAAssetsOperateUtils";

	/**
	 * ͨ���ļ�����Assets�л����Դ,������������ʽ����
	 * 
	 * @param context
	 * @param fileName
	 *            �ļ���ӦΪassets�ļ����ؾ���·��
	 * @return ��InputStream����ʽ����
	 */
	public static InputStream getInputStreamForName(Context context,
			String fileName)
	{
		AssetManager assetManager = context.getAssets();
		InputStream inputStream = null;
		try
		{
			inputStream = assetManager.open(fileName);
		} catch (IOException e)
		{
			TALogger.d(TAG, e.getMessage());
		}
		return inputStream;
	}

	/**
	 * ͨ���ļ�����Assets�л����Դ�����ַ�������ʽ����
	 * 
	 * @param context
	 * @param fileName
	 *            �ļ���ӦΪassets�ļ����ؾ���·��
	 * @return ���ַ�������ʽ����
	 */
	public static String getStringForName(Context context, String fileName)
	{
		InputStream inputStream = null;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte buf[] = new byte[1024];
		int len;
		try
		{
			inputStream = getInputStreamForName(context, fileName);
			while ((len = inputStream.read(buf)) != -1)
			{
				outputStream.write(buf, 0, len);
			}
			outputStream.close();
			inputStream.close();
		} catch (IOException e)
		{
			TALogger.d(TAG, e.getMessage());
		}
		return outputStream.toString();
	}

	/**
	 * ͨ���ļ�����Assets�л����Դ����λͼ����ʽ����
	 * 
	 * @param context
	 * @param fileName
	 *            �ļ���ӦΪassets�ļ����ؾ���·��
	 * @return ��λͼ����ʽ����
	 */
	public static Bitmap getBitmapForName(Context context, String fileName)
	{
		Bitmap bitmap = null;
		InputStream inputStream = null;
		try
		{
			inputStream = getInputStreamForName(context, fileName);
			bitmap = BitmapFactory.decodeStream(inputStream);
			inputStream.close();
		} catch (IOException e)
		{
			TALogger.d(TAG, e.getMessage());
		}
		return bitmap;
	}
	
	

}
