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

import java.lang.reflect.Field;

import org.apache.http.NameValuePair;

import com.ta.common.TAStringUtils;
import com.ta.exception.TADBException;
import com.ta.util.db.annotation.TAPrimaryKey;
import com.ta.util.db.entity.TAArrayList;
import com.ta.util.db.util.TADBUtils;

/**
 * @Title TASqlBuilder
 * @Package com.ta.util.db.util.sql
 * @Description ����sql��乹������
 * @author ��è
 * @date 2013-1-20
 * @version V1.0
 */
public class TAInsertSqlBuilder extends TASqlBuilder
{
	@Override
	public void onPreGetStatement() throws TADBException,
			IllegalArgumentException, IllegalAccessException
	{
		// TODO Auto-generated method stub
		if (getUpdateFields() == null)
		{
			setUpdateFields(getFieldsAndValue(entity));
		}
		super.onPreGetStatement();
	}

	@Override
	public String buildSql() throws TADBException, IllegalArgumentException,
			IllegalAccessException
	{
		// TODO Auto-generated method stub
		StringBuilder columns = new StringBuilder(256);
		StringBuilder values = new StringBuilder(256);
		columns.append("INSERT INTO ");
		columns.append(tableName).append(" (");
		values.append("(");
		TAArrayList updateFields = getUpdateFields();
		if (updateFields != null)
		{
			for (int i = 0; i < updateFields.size(); i++)
			{
				NameValuePair nameValuePair = updateFields.get(i);
				columns.append(nameValuePair.getName());
				values.append(TAStringUtils
						.isNumeric(nameValuePair.getValue() != null ? nameValuePair
								.getValue().toString() : "") ? nameValuePair
						.getValue() : "'" + nameValuePair.getValue() + "'");
				if (i + 1 < updateFields.size())
				{
					columns.append(", ");
					values.append(", ");
				}
			}
		} else
		{
			throw new TADBException("������������");
		}
		columns.append(") values ");
		values.append(")");
		columns.append(values);
		return columns.toString();
	}

	/**
	 * ��ʵ�����,���µ�����
	 * 
	 * @return
	 * @throws TADBException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static TAArrayList getFieldsAndValue(Object entity)
			throws TADBException, IllegalArgumentException,
			IllegalAccessException
	{
		// TODO Auto-generated method stub
		TAArrayList arrayList = new TAArrayList();
		if (entity == null)
		{
			throw new TADBException("û�м���ʵ���࣡");
		}
		Class<?> clazz = entity.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields)
		{

			if (!TADBUtils.isTransient(field))
			{
				if (TADBUtils.isBaseDateType(field))
				{
					TAPrimaryKey annotation = field
							.getAnnotation(TAPrimaryKey.class);
					if (annotation != null && annotation.autoIncrement())
					{

					} else
					{
						String columnName = TADBUtils.getColumnByField(field);
						field.setAccessible(true);
						arrayList
								.add((columnName != null && !columnName
										.equals("")) ? columnName : field
										.getName(),
										field.get(entity) == null ? null
												: field.get(entity).toString());
					}

				}
			}
		}
		return arrayList;
	}
}
