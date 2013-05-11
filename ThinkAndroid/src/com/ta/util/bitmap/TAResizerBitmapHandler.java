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

import java.io.FileDescriptor;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class TAResizerBitmapHandler extends TAProcessBitmapHandler
{

	protected int mImageWidth;
	protected int mImageHeight;
	protected Resources mResources;

	/**
	 * ��ʼ��һ��Ŀ���ṩͼ��Ŀ�Ⱥ͸߶ȵ�������ͼ��
	 * 
	 * @param context
	 * @param imageWidth
	 * @param imageHeight
	 */
	public TAResizerBitmapHandler(Context context, int imageWidth,
			int imageHeight)
	{
		mResources = context.getResources();
		setImageSize(imageWidth, imageHeight);
	}

	/**
	 * ��ʼ���ṩ��һĿ��ͼ���С(���ڿ�Ⱥ͸߶�);
	 * 
	 * @param context
	 * @param imageSize
	 */
	public TAResizerBitmapHandler(Context context, int imageSize)
	{
		mResources = context.getResources();
		setImageSize(imageSize);
	}

	/**
	 * ����Ŀ��ͼƬ�Ŀ�ȸ߶�
	 * 
	 * @param width
	 * @param height
	 */
	public void setImageSize(int width, int height)
	{
		mImageWidth = width;
		mImageHeight = height;
	}

	/**
	 * ����Ŀ��ͼƬ�Ŀ�ȸ߶�
	 * 
	 * @param size
	 */
	public void setImageSize(int size)
	{
		setImageSize(size, size);
	}

	/**
	 * һ����Ҫ�Ĵ��������������ں�̨ʱ, ���������������ֻ�ǳ�ȡ����һ��λͼ����������Դ.
	 * 
	 * @param resId
	 * @return
	 */
	private Bitmap processBitmap(int resId)
	{
		/*
		 * if (BuildConfig.DEBUG) { Log.d(TAG, "processBitmap - " + resId); }
		 */
		return decodeSampledBitmapFromResource(mResources, resId, mImageWidth,
				mImageHeight);
	}

	@Override
	protected Bitmap processBitmap(Object data)
	{
		return processBitmap(Integer.parseInt(String.valueOf(data)));
	}

	/**
	 * ����һ���Ŀ��������Դ����ͳ�ȡһ��λͼ��
	 * 
	 * @param res
	 *            ��Դ����,���а�����ͼ������
	 * @param ��Դid��ͼ������
	 * @param reqWidth
	 *            ����Ŀ�Ȳ�����λͼ
	 * @param reqHeight
	 *            ����ĸ߶Ȳ�����λͼ
	 * @return
	 * 
	 */
	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight)
	{

		// ��һ�����inJustDecodeBounds = true�����ά��
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// ����inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// ����inSampleSize����λͼ
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	/**
	 * ����һ���Ŀ�������ļ�����ͳ�ȡһ��λͼ��
	 * 
	 * @param filename
	 *            �ļ���������·���ļ�������
	 * @param reqWidth
	 *            Ŀ��ͼƬ���
	 * @param reqHeight
	 *            Ŀ��ͼƬ�ĸ߶�
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromFile(String filename,
			int reqWidth, int reqHeight)
	{

		// ��һ�����inJustDecodeBounds = true�����ά��
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filename, options);

		// ����inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// ����inSampleSize����λͼ
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filename, options);
	}

	/**
	 * ����һ���Ŀ����������������ͳ�ȡһ��λͼ��
	 * 
	 * @param fileDescriptor
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromDescriptor(
			FileDescriptor fileDescriptor, int reqWidth, int reqHeight)
	{

		// ��һ�����inJustDecodeBounds = true�����ά��
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);

		// ����inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// ����inSampleSize����λͼ
		options.inJustDecodeBounds = false;
		return BitmapFactory
				.decodeFileDescriptor(fileDescriptor, null, options);
	}

	/**
	 * Calculate an inSampleSize for use in a {@link BitmapFactory.Options}
	 * object when decoding bitmaps using the decode* methods from
	 * {@link BitmapFactory}. This implementation calculates the closest
	 * inSampleSize that will result in the final decoded bitmap having a width
	 * and height equal to or larger than the requested width and height. This
	 * implementation does not ensure a power of 2 is returned for inSampleSize
	 * which can be faster when decoding but results in a larger bitmap which
	 * isn't as useful for caching purposes.
	 * 
	 * @param options
	 *            An options object with out* params already populated (run
	 *            through a decode* method with inJustDecodeBounds==true
	 * @param reqWidth
	 *            The requested width of the resulting bitmap
	 * @param reqHeight
	 *            The requested height of the resulting bitmap
	 * @return The value to be used for inSampleSize
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight)
	{
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth)
		{
			if (width > height)
			{
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else
			{
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}

			// This offers some additional logic in case the image has a strange
			// aspect ratio. For example, a panorama may have a much larger
			// width than height. In these cases the total pixels might still
			// end up being too large to fit comfortably in memory, so we should
			// be more aggressive with sample down the image (=larger
			// inSampleSize).

			final float totalPixels = width * height;

			// Anything more than 2x the requested pixels we'll sample down
			// further.
			final float totalReqPixelsCap = reqWidth * reqHeight * 2;

			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap)
			{
				inSampleSize++;
			}
		}
		return inSampleSize;
	}

}
