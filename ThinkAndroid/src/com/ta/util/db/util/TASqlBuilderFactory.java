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
package com.ta.util.db.util;

import com.ta.util.db.util.sql.TADeleteSqlBuilder;
import com.ta.util.db.util.sql.TAInsertSqlBuilder;
import com.ta.util.db.util.sql.TAQuerySqlBuilder;
import com.ta.util.db.util.sql.TASqlBuilder;
import com.ta.util.db.util.sql.TAUpdateSqlBuilder;

/**
 * @Title TASqlBuilder
 * @Package com.ta.util.db.util.sql
 * @Description Sql����������,����sql��乹����
 * @author ��è
 * @date 2013-1-20
 * @version V1.0
 */
public class TASqlBuilderFactory
{
	private static TASqlBuilderFactory instance;
	/**
	 * ����getSqlBuilder(int operate)���ز���sql��乹��������Ĳ���
	 */
	public static final int INSERT = 0;
	/**
	 * ����getSqlBuilder(int operate)���ز�ѯsql��乹��������Ĳ���
	 */
	public static final int SELECT = 1;
	/**
	 * ����getSqlBuilder(int operate)����ɾ��sql��乹��������Ĳ���
	 */
	public static final int DELETE = 2;
	/**
	 * ����getSqlBuilder(int operate)���ظ���sql��乹��������Ĳ���
	 */
	public static final int UPDATE = 3;

	/**
	 * ����ģʽ���Sql����������
	 * 
	 * @return sql������
	 */
	public static TASqlBuilderFactory getInstance()
	{
		if (instance == null)
		{
			instance = new TASqlBuilderFactory();
		}
		return instance;
	}

	/**
	 * ���sql������
	 * 
	 * @param operate
	 * @return ������
	 */
	public synchronized TASqlBuilder getSqlBuilder(int operate)
	{
		TASqlBuilder sqlBuilder = null;
		switch (operate)
		{
		case INSERT:
			sqlBuilder = new TAInsertSqlBuilder();
			break;
		case SELECT:
			sqlBuilder = new TAQuerySqlBuilder();
			break;
		case DELETE:
			sqlBuilder = new TADeleteSqlBuilder();
			break;
		case UPDATE:
			sqlBuilder = new TAUpdateSqlBuilder();
			break;
		default:
			break;
		}
		return sqlBuilder;
	}
}
