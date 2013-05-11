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
package com.ta.util.db;

import com.ta.util.db.TASQLiteDatabase.TADBUpdateListener;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @Title TADBHelper
 * @Package com.ta.util.db
 * @Description �������ݿ�Ĵ����Ͱ汾����
 * @author ��è
 * @date 2013-1-20
 * @version V1.0
 */
public class TADBHelper extends SQLiteOpenHelper
{
	/**
	 * ���ݿ���¼�����
	 */
	private TADBUpdateListener mTadbUpdateListener;

	/**
	 * ���캯��
	 * 
	 * @param context
	 *            ������
	 * @param name
	 *            ���ݿ�����
	 * @param factory
	 *            ��ѡ�����ݿ��α깤���࣬����ѯ(query)���ύʱ���ö���ᱻ������ʵ����һ���α�
	 * @param version
	 *            ���ݿ�汾
	 */
	public TADBHelper(Context context, String name, CursorFactory factory,
			int version)
	{
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	/**
	 * ���캯��
	 * 
	 * @param context
	 *            ������
	 * @param name
	 *            ���ݿ�����
	 * @param factory
	 *            ��ѡ�����ݿ��α깤���࣬����ѯ(query)���ύʱ���ö���ᱻ������ʵ����һ���α�
	 * @param version
	 *            ���ݿ�汾
	 * @param tadbUpdateListener
	 *            ���ݿ���¼�����
	 */
	public TADBHelper(Context context, String name, CursorFactory factory,
			int version, TADBUpdateListener tadbUpdateListener)
	{
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
		this.mTadbUpdateListener = tadbUpdateListener;
	}

	/**
	 * �������ݿ���¼�����
	 * 
	 * @param mTadbUpdateListener
	 *            ���ݿ���¼�����
	 */
	public void setOndbUpdateListener(TADBUpdateListener tadbUpdateListener)
	{
		this.mTadbUpdateListener = tadbUpdateListener;
	}

	public void onCreate(SQLiteDatabase db)
	{

	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		if (mTadbUpdateListener != null)
		{
			mTadbUpdateListener.onUpgrade(db, oldVersion, newVersion);
		}
	}

}
