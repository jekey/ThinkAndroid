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

import android.content.Context;

/**
 * @Title DensityUtils
 * @Package com.ta.util.extend.draw
 * @Description DensityUtils��һ��������dpת���Ĺ���
 * @author ��è
 * @date 2013-1-22 ���� 9:35
 * @version V1.0
 */
public class DensityUtils
{
	/**
	 * �����ֻ��ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����)
	 * 
	 * @param context
	 * @param dpValue
	 *            dpֵ
	 * @return ��������ֵ
	 */
	public static int dipTopx(Context context, float dpValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * �����ֻ��ķֱ��ʴ� px(����) �ĵ�λ ת��Ϊ dp
	 * 
	 * @param context
	 * @param pxValue
	 *            ����ֵ
	 * @return ����dpֵ
	 */
	public static int pxTodip(Context context, float pxValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

}
