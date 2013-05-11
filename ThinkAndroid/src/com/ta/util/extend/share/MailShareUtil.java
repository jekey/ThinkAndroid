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
package com.ta.util.extend.share;

import android.content.Context;
import android.content.Intent;

/**
 * @Title MailShareUtil
 * @Package com.ta.core.util.extend.share
 * @Description �ʼ��������
 * @author ��è
 * @date 2013-1-22 ���� 14:57
 * @version V1.0
 */
public class MailShareUtil
{
	/**
	 * �ʼ�����
	 * 
	 * @param mContext
	 * @param title
	 *            �ʼ��ı���
	 * @param text
	 *            �ʼ�������
	 * @return
	 */
	public static Boolean sendMail(Context mContext, String title, String text)
	{
		// ����ϵͳ���ʼ�
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		// �����ı���ʽ
		emailIntent.setType("text/plain");
		// ���öԷ��ʼ���ַ
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, "");
		// ���ñ�������
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
		// �����ʼ��ı�����
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
		mContext.startActivity(Intent.createChooser(emailIntent,
				"Choose Email Client"));
		return null;
	}
}
