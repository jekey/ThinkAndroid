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
package com.ta.util.db.util.sql;

import java.util.regex.Pattern;

import com.ta.exception.TADBException;

import android.text.TextUtils;

/**
 * @Title TASqlBuilder
 * @Package com.ta.util.db.util.sql
 * @Description ��ѯsql��乹������
 * @author ��è
 * @date 2013-1-20
 * @version V1.0
 */
public class TAQuerySqlBuilder extends TASqlBuilder
{
	protected Pattern sLimitPattern = Pattern
			.compile("\\s*\\d+\\s*(,\\s*\\d+\\s*)?");

	@Override
	public String buildSql() throws TADBException, IllegalArgumentException,
			IllegalAccessException
	{
		// TODO Auto-generated method stub
		return buildQueryString();
	}

	/**
	 * ������ѯ���ֶ�
	 * 
	 * @param distinct
	 *            �����ظ������Ϊtrue������,false���ù�
	 * @param table
	 *            ����
	 * @param columns
	 *            ��Ҫ��ѯ����
	 * @param selection
	 *            ��ʽ������Ϊ SQL WHERE�Ӿ�(����WHERE����)�� ����null���ظ�����������С�
	 * @param selectionArgs
	 * @param groupBy
	 *            groupBy���
	 * @param having
	 *            having���
	 * @param orderBy
	 *            orderBy���
	 * @param limit
	 *            limit���
	 * @return
	 */
	public String buildQueryString()
	{
		if (TextUtils.isEmpty(groupBy) && !TextUtils.isEmpty(having))
		{
			throw new IllegalArgumentException(
					"HAVING clauses are only permitted when using a groupBy clause");
		}
		if (!TextUtils.isEmpty(limit)
				&& !sLimitPattern.matcher(limit).matches())
		{
			throw new IllegalArgumentException("invalid LIMIT clauses:" + limit);
		}

		StringBuilder query = new StringBuilder(120);
		query.append("SELECT ");
		if (distinct)
		{
			query.append("DISTINCT ");
		}
		query.append("* ");
		query.append("FROM ");
		query.append(tableName);
		appendClause(query, " WHERE ", where);
		appendClause(query, " GROUP BY ", groupBy);
		appendClause(query, " HAVING ", having);
		appendClause(query, " ORDER BY ", orderBy);
		appendClause(query, " LIMIT ", limit);
		return query.toString();
	}
}
