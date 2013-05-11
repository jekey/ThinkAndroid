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
package com.ta.util.extend.draw;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;

/**
 * @Title ImageUtils
 * @Package com.ta.util.extend.draw
 * @Description ����ͼƬ�Ĺ�����.
 * @author ��è
 * @date 2013-1-22 ���� 9:35
 * @version V1.0
 */
public class ImageUtils
{
	private static float[] carray = new float[20];

	/**
	 * ͼƬȥɫ,���ػҶ�ͼƬ
	 * 
	 * @param bmpOriginal
	 *            �����ͼƬ
	 * @return ȥɫ���ͼƬ
	 */
	public static Bitmap toGrayscale(Bitmap bmpOriginal)
	{

		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();
		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		paint.setColorFilter(null);
		c.drawBitmap(bmpGrayscale, 0, 0, paint);
		ColorMatrix cm = new ColorMatrix();
		getValueBlackAndWhite();
		cm.set(carray);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}

	public static void getValueSaturation()
	{
		// �߱��Ͷ�
		carray[0] = 5;
		carray[1] = 0;
		carray[2] = 0;
		carray[3] = 0;
		carray[4] = -254;
		carray[5] = 0;
		carray[6] = 5;
		carray[7] = 0;
		carray[8] = 0;
		carray[9] = -254;
		carray[10] = 0;
		carray[11] = 0;
		carray[12] = 5;
		carray[13] = 0;
		carray[14] = -254;
		carray[15] = 0;
		carray[16] = 0;
		carray[17] = 0;
		carray[18] = 5;
		carray[19] = -254;

	}

	private static void getValueBlackAndWhite()
	{

		// �ڰ�
		carray[0] = (float) 0.308;
		carray[1] = (float) 0.609;
		carray[2] = (float) 0.082;
		carray[3] = 0;
		carray[4] = 0;
		carray[5] = (float) 0.308;
		carray[6] = (float) 0.609;
		carray[7] = (float) 0.082;
		carray[8] = 0;
		carray[9] = 0;
		carray[10] = (float) 0.308;
		carray[11] = (float) 0.609;
		carray[12] = (float) 0.082;
		carray[13] = 0;
		carray[14] = 0;
		carray[15] = 0;
		carray[16] = 0;
		carray[17] = 0;
		carray[18] = 1;
		carray[19] = 0;
	}

	/***/
	/**
	 * ȥɫͬʱ��Բ��
	 * 
	 * @param bmpOriginal
	 *            ԭͼ
	 * @param pixels
	 *            Բ�ǻ���
	 * @return �޸ĺ��ͼƬ
	 */
	public static Bitmap toGrayscale(Bitmap bmpOriginal, int pixels)
	{
		return toRoundCorner(toGrayscale(bmpOriginal), pixels);
	}

	/**
	 * ��ͼƬ���Բ��
	 * 
	 * @param bitmap
	 *            ��Ҫ�޸ĵ�ͼƬ
	 * @param pixels
	 *            Բ�ǵĻ���
	 * @return Բ��ͼƬ
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels)
	{

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * ʹԲ�ǹ���֧��BitampDrawable
	 * 
	 * @param bitmapDrawable
	 * @param pixels
	 * @return
	 */
	public static BitmapDrawable toRoundCorner(BitmapDrawable bitmapDrawable,
			int pixels)
	{
		Bitmap bitmap = bitmapDrawable.getBitmap();
		bitmapDrawable = new BitmapDrawable(toRoundCorner(bitmap, pixels));
		return bitmapDrawable;
	}

	/**
	 * ����ͼƬ��Ӱ
	 * 
	 * @param originalImage
	 * @return
	 */
	public static Bitmap createReflectedImage(Bitmap originalImage)
	{

		final int reflectionGap = 4;
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
				height / 2, width, height / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(height + height / 2), Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);

		canvas.drawBitmap(originalImage, 0, 0, null);

		Paint defaultPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);

		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0,
				originalImage.getHeight(), 0, bitmapWithReflection.getHeight()
						+ reflectionGap, 0x70ffffff, 0x00ffffff,
				TileMode.MIRROR);

		paint.setShader(shader);

		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));

		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);

		return bitmapWithReflection;
	}
}