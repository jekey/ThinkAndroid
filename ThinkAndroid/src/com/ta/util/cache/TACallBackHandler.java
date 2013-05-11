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

/**
 * @Title TACallBackHandler
 * @Package com.ta.util.cache
 * @Description �������Ļص���
 * @author ��è
 * @date 2013-1-20
 * @version V1.0
 */
public class TACallBackHandler<T>
{
	/**
	 * �������п�ʼ
	 * 
	 * @param t
	 *            ��Ӧ�Ķ���
	 * @param data
	 *            ����Ψһ��ʶ
	 */
	public void onStart(T t, Object data)
	{
	}

	/**
	 * �������п�ʼ
	 * 
	 * @param t
	 *            ��Ӧ�Ķ���
	 * @param data
	 *            ����Ψһ��ʶ
	 * @param inputStream
	 *            ��ʶ��Ӧ����Ӧ����
	 */
	public void onSuccess(T t, Object data, byte[] buffer)
	{
	}

	/**
	 * ��������ʧ��
	 * 
	 * @param t
	 *            ��Ӧ�Ķ���
	 * @param data
	 *            ����Ψһ��ʶ
	 */
	public void onFailure(T t, Object data)
	{

	}
}
