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
package com.ta.common;

import android.os.Build;

/**
 * @Title AndroidVersionCheckUtils
 * @Package com.ta.core.util.extend.app
 * @Description AndroidVersionCheckUtils ���ڶ�汾���ݼ��
 * @author ��è
 * @date 2013-1-10 ���� 9:53
 * @version V1.0
 */
public class AndroidVersionCheckUtils
{
	private AndroidVersionCheckUtils()
	{
	};

	/**
	 * ��ǰAndroidϵͳ�汾�Ƿ��ڣ� Donut�� Android 1.6������
	 * 
	 * @return
	 */
	public static boolean hasDonut()
	{
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT;
	}

	/**
	 * ��ǰAndroidϵͳ�汾�Ƿ��ڣ� Eclair�� Android 2.0�� ����
	 * 
	 * @return
	 */
	public static boolean hasEclair()
	{
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR;
	}

	/**
	 * ��ǰAndroidϵͳ�汾�Ƿ��ڣ� Froyo�� Android 2.2�� Android 2.2����
	 * 
	 * @return
	 */
	public static boolean hasFroyo()
	{
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	/**
	 * ��ǰAndroidϵͳ�汾�Ƿ��ڣ� Gingerbread�� Android 2.3x�� Android 2.3x ����
	 * 
	 * @return
	 */
	public static boolean hasGingerbread()
	{
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	/**
	 * ��ǰAndroidϵͳ�汾�Ƿ��ڣ� Honeycomb�� Android3.1�� Android3.1����
	 * 
	 * @return
	 */
	public static boolean hasHoneycomb()
	{
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	/**
	 * ��ǰAndroidϵͳ�汾�Ƿ��ڣ� HoneycombMR1�� Android3.1.1�� Android3.1.1����
	 * 
	 * @return
	 */
	public static boolean hasHoneycombMR1()
	{
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	}

	/**
	 * ��ǰAndroidϵͳ�汾�Ƿ��ڣ� IceCreamSandwich�� Android4.0�� Android4.0����
	 * 
	 * @return
	 */
	public static boolean hasIcecreamsandwich()
	{
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
	}

	/*
	 * public static boolean hasJellyBean() { return Build.VERSION.SDK_INT >=
	 * Build.VERSION_CODES.JELLY_BEAN; }
	 */
}
