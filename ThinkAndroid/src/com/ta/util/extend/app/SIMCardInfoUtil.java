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
package com.ta.util.extend.app;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * @Title SIMCardInfoUtil
 * @Package com.ta.util.extend.app
 * @Description �û����ؿͻ���SIM����һЩ��Ϣ ��Ҫ���� <uses-permission
 *              android:name="android.permission.READ_PHONE_STATE"/>
 * @author ��è
 * @date 2013-1-10 ���� 13:53
 * @version V1.0
 */
public class SIMCardInfoUtil
{
	/**
	 * ���ر����ֻ����룬������벻һ���ܻ�ȡ��
	 * 
	 * @param context
	 * @return
	 */
	public static String getNativePhoneNumber(Context context)
	{
		TelephonyManager telephonyManager;
		telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String NativePhoneNumber = null;
		NativePhoneNumber = telephonyManager.getLine1Number();
		return NativePhoneNumber;
	}

	/**
	 * �����ֻ�����������
	 * 
	 * @param context
	 * @return
	 */
	public static String getProvidersName(Context context)
	{
		String ProvidersName = null;
		// ����Ψһ���û�ID;�������ſ��ı�������
		String IMSI = getIMSI(context);
		// IMSI��ǰ��3λ460�ǹ��ң������ź���2λ00 02���й��ƶ���01���й���ͨ��03���й����š�
		System.out.println(IMSI);
		if (IMSI.startsWith("46000") || IMSI.startsWith("46002"))
		{
			ProvidersName = "�й��ƶ�";
		} else if (IMSI.startsWith("46001"))
		{
			ProvidersName = "�й���ͨ";
		} else if (IMSI.startsWith("46003"))
		{
			ProvidersName = "�й�����";
		} else
		{
			ProvidersName = "����������";
		}
		return ProvidersName;
	}

	/**
	 * �����ֻ�IMSI����
	 * 
	 * @param context
	 * @return
	 */
	public static String getIMSI(Context context)
	{
		TelephonyManager telephonyManager;
		telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		// ����Ψһ���û�ID;�������ſ���IMSI���
		return telephonyManager.getSubscriberId();
	}
}