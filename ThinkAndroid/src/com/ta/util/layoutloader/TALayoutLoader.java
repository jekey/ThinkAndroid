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
package com.ta.util.layoutloader;

import java.lang.reflect.Field;

import com.ta.exception.TANoSuchNameLayoutException;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

/**
 * @Title TASystemResLoader
 * @Package com.ta.util.layoutloader
 * @Description TASystemResLoader�ǻ�ȡϵͳ��Դ������
 * @author ��è
 * @date 2013-1-6
 * @version V1.0
 */
public class TALayoutLoader implements TAILayoutLoader
{

	private static TALayoutLoader instance;
	private Context mContext;

	private TALayoutLoader(Context context)
	{
		this.mContext = context;
	}

	/**
	 * ���ϵͳ��Դ��
	 * 
	 * @return TASystemResLoader����
	 */

	public static TALayoutLoader getInstance(Context context)
	{
		if (instance == null)
		{
			instance = new TALayoutLoader(context);
		}
		return instance;
	}

	/**
	 * ��ò���ID
	 * 
	 * @param context
	 * @param cls
	 * @param resIDName
	 * @return
	 * @throws NameNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws TAResIDNotFoundException
	 */
	public int getLayoutID(String resIDName) throws NameNotFoundException,
			ClassNotFoundException, IllegalArgumentException,
			IllegalAccessException, TANoSuchNameLayoutException
	{
		int resID = readResID("layout", resIDName);
		if (resID == 0)
		{
			throw new TANoSuchNameLayoutException();
		}
		return resID;
	}

	/**
	 * @param context
	 * @param resIDName
	 *            ��Դid����
	 * @return
	 * @throws NameNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public int readResID(String type, String resIDName)
			throws NameNotFoundException, ClassNotFoundException,
			IllegalArgumentException, IllegalAccessException
	{
		String packageName;
		PackageManager pm = mContext.getPackageManager();
		PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), 0);
		packageName = pi.packageName;
		if (packageName == null || packageName.equalsIgnoreCase(""))
		{
			throw new NameNotFoundException("û�л�ȡ��ϵͳ������");
		}
		packageName = packageName + ".R";
		Class<?> clazz = Class.forName(packageName);
		Class<?> cls = readResClass(clazz, packageName + "$" + type);
		if (cls == null)
		{
			throw new NameNotFoundException("û������Դ������");
		}
		return readResID(cls, resIDName);

	}

	/**
	 * ������Դ��R�ļ������ɵ���
	 * 
	 * @param cls
	 *            ��Դ����
	 * @param respackageName
	 *            ��Դ�İ���
	 * @return ������Դ��R�ļ������ɵ���
	 */
	public Class<?> readResClass(Class<?> cls, String respackageName)
	{
		Class<?>[] classes = cls.getDeclaredClasses();
		for (int i = 0; i < classes.length; i++)
		{
			Class<?> tempClass = classes[i];
			Log.v("TAReadSystemRes", tempClass.getName());
			if (tempClass.getName().equalsIgnoreCase(respackageName))
			{
				return tempClass;
			}
		}
		return null;
	}

	/**
	 * ��ȡR��Դ�ļ�
	 * 
	 * @param cls
	 * @param string
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public int readResID(Class<?> cls, String resIDName)
			throws IllegalArgumentException, IllegalAccessException
	{

		Field[] fields = cls.getDeclaredFields();
		for (int j = 0; j < fields.length; j++)
		{
			if (fields[j].getName().equalsIgnoreCase(resIDName))
			{
				return fields[j].getInt(cls);
			}
		}
		return 0;
	}

}
