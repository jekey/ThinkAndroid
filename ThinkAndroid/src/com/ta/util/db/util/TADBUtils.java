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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.ta.common.TAStringUtils;
import com.ta.exception.TADBException;
import com.ta.util.db.annotation.TAColumn;
import com.ta.util.db.annotation.TAPrimaryKey;
import com.ta.util.db.annotation.TATableName;
import com.ta.util.db.annotation.TATransient;
import com.ta.util.db.entity.TAHashMap;
import com.ta.util.db.entity.TAPKProperyEntity;
import com.ta.util.db.entity.TAPropertyEntity;
import com.ta.util.db.entity.TATableInfoEntity;

import android.database.Cursor;

/**
 * @Title TASqlBuilder
 * @Package com.ta.util.db.util.sql
 * @Description ���ݿ��һЩ����
 * @author ��è
 * @date 2013-1-20
 * @version V1.0
 */
public class TADBUtils
{
	/**
	 * ͨ��Cursor��ȡһ��ʵ������
	 * 
	 * @param clazz
	 *            ʵ������
	 * @param cursor
	 *            ���ݼ���
	 * @return ��Ӧʵ��List����
	 */
	public static <T> List<T> getListEntity(Class<T> clazz, Cursor cursor)
	{
		List<T> queryList = TAEntityBuilder.buildQueryList(clazz, cursor);
		return queryList;
	}

	/**
	 * �������ݱ���һ�е�����
	 * 
	 * @param cursor
	 *            ���ݼ���
	 * @return TAHashMap��������
	 */
	public static TAHashMap<String> getRowData(Cursor cursor)
	{
		if (cursor != null && cursor.getColumnCount() > 0)
		{
			TAHashMap<String> hashMap = new TAHashMap<String>();
			int columnCount = cursor.getColumnCount();
			for (int i = 0; i < columnCount; i++)
			{
				hashMap.put(cursor.getColumnName(i), cursor.getString(i));
			}
			return hashMap;
		}
		return null;
	}

	/**
	 * ����ʵ���� ��� ʵ�����Ӧ�ı���
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getTableName(Class<?> clazz)
	{
		TATableName table = (TATableName) clazz
				.getAnnotation(TATableName.class);
		if (table == null || TAStringUtils.isEmpty(table.name()))
		{
			// ��û��ע���ʱ��Ĭ�������������Ϊ����,���ѵ㣨.���滻Ϊ�»���(_)
			return clazz.getName().toLowerCase().replace('.', '_');
		}
		return table.name();

	}

	/**
	 * ���������ֶ�
	 * 
	 * @param clazz
	 *            ʵ������
	 * @return
	 */
	public static Field getPrimaryKeyField(Class<?> clazz)
	{
		Field primaryKeyField = null;
		Field[] fields = clazz.getDeclaredFields();
		if (fields != null)
		{

			for (Field field : fields)
			{ // ��ȡIDע��
				if (field.getAnnotation(TAPrimaryKey.class) != null)
				{
					primaryKeyField = field;
					break;
				}
			}
			if (primaryKeyField == null)
			{ // û��IDע��
				for (Field field : fields)
				{
					if ("_id".equals(field.getName()))
					{
						primaryKeyField = field;
						break;
					}
				}
				if (primaryKeyField == null)
				{ // ���û��_id���ֶ�
					for (Field field : fields)
					{
						if ("id".equals(field.getName()))
						{
							primaryKeyField = field;
							break;
						}
					}
				}
			}
		} else
		{
			throw new RuntimeException("this model[" + clazz + "] has no field");
		}
		return primaryKeyField;
	}

	/**
	 * ����������
	 * 
	 * @param clazz
	 *            ʵ������
	 * @return
	 */
	public static String getPrimaryKeyFieldName(Class<?> clazz)
	{
		Field f = getPrimaryKeyField(clazz);
		return f == null ? "id" : f.getName();
	}

	/**
	 * �������ݿ��ֶ�����
	 * 
	 * @param clazz
	 *            ʵ������
	 * @return ���ݿ���ֶ�����
	 */
	public static List<TAPropertyEntity> getPropertyList(Class<?> clazz)
	{

		List<TAPropertyEntity> plist = new ArrayList<TAPropertyEntity>();
		try
		{
			Field[] fields = clazz.getDeclaredFields();
			String primaryKeyFieldName = getPrimaryKeyFieldName(clazz);
			for (Field field : fields)
			{
				if (!TADBUtils.isTransient(field))
				{
					if (TADBUtils.isBaseDateType(field))
					{

						if (field.getName().equals(primaryKeyFieldName)) // ��������
							continue;

						TAPKProperyEntity property = new TAPKProperyEntity();

						property.setColumnName(TADBUtils
								.getColumnByField(field));
						property.setName(field.getName());
						property.setType(field.getType());
						property.setDefaultValue(TADBUtils
								.getPropertyDefaultValue(field));
						plist.add(property);
					}
				}
			}
			return plist;
		} catch (Exception e)
		{
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * �����������sql���
	 * 
	 * @param clazz
	 *            ʵ������
	 * @return �������sql���
	 * @throws TADBException
	 */
	public static String creatTableSql(Class<?> clazz) throws TADBException
	{
		TATableInfoEntity tableInfoEntity = TATableInfofactory.getInstance()
				.getTableInfoEntity(clazz);

		TAPKProperyEntity pkProperyEntity = null;
		pkProperyEntity = tableInfoEntity.getPkProperyEntity();
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("CREATE TABLE IF NOT EXISTS ");
		strSQL.append(tableInfoEntity.getTableName());
		strSQL.append(" ( ");

		if (pkProperyEntity != null)
		{
			Class<?> primaryClazz = pkProperyEntity.getType();
			if (primaryClazz == int.class || primaryClazz == Integer.class)
				if (pkProperyEntity.isAutoIncrement())
				{
					strSQL.append("\"").append(pkProperyEntity.getColumnName())
							.append("\"    ")
							.append("INTEGER PRIMARY KEY AUTOINCREMENT,");
				} else
				{
					strSQL.append("\"").append(pkProperyEntity.getColumnName())
							.append("\"    ").append("INTEGER PRIMARY KEY,");
				}
			else
				strSQL.append("\"").append(pkProperyEntity.getColumnName())
						.append("\"    ").append("TEXT PRIMARY KEY,");
		} else
		{
			strSQL.append("\"").append("id").append("\"    ")
					.append("INTEGER PRIMARY KEY AUTOINCREMENT,");
		}

		Collection<TAPropertyEntity> propertys = tableInfoEntity
				.getPropertieArrayList();
		for (TAPropertyEntity property : propertys)
		{
			strSQL.append("\"").append(property.getColumnName());
			strSQL.append("\",");
		}
		strSQL.deleteCharAt(strSQL.length() - 1);
		strSQL.append(" )");
		return strSQL.toString();
	}

	/**
	 * ��� �ֶ��Ƿ��Ѿ�����עΪ �����ݿ��ֶ�
	 * 
	 * @param field
	 * @return
	 */
	public static boolean isTransient(Field field)
	{
		return field.getAnnotation(TATransient.class) != null;
	}

	/**
	 * ����Ƿ�������
	 * 
	 * @param field
	 * @return
	 */
	public static boolean isPrimaryKey(Field field)
	{
		return field.getAnnotation(TAPrimaryKey.class) != null;
	}

	/**
	 * ����Ƿ�����
	 * 
	 * @param field
	 * @return
	 */
	public static boolean isAutoIncrement(Field field)
	{
		TAPrimaryKey primaryKey = field.getAnnotation(TAPrimaryKey.class);
		if (null != primaryKey)
		{
			return primaryKey.autoIncrement();
		}
		return false;
	}

	/**
	 * �Ƿ�Ϊ��������������
	 * 
	 * @param field
	 * @return
	 */
	public static boolean isBaseDateType(Field field)
	{
		Class<?> clazz = field.getType();
		return clazz.equals(String.class) || clazz.equals(Integer.class)
				|| clazz.equals(Byte.class) || clazz.equals(Long.class)
				|| clazz.equals(Double.class) || clazz.equals(Float.class)
				|| clazz.equals(Character.class) || clazz.equals(Short.class)
				|| clazz.equals(Boolean.class) || clazz.equals(Date.class)
				|| clazz.equals(java.util.Date.class)
				|| clazz.equals(java.sql.Date.class) || clazz.isPrimitive();
	}

	/**
	 * ��ȡĳ����
	 * 
	 * @param field
	 * @return
	 */
	public static String getColumnByField(Field field)
	{
		TAColumn column = field.getAnnotation(TAColumn.class);
		if (column != null && column.name().trim().length() != 0)
		{
			return column.name();
		}
		TAPrimaryKey primaryKey = field.getAnnotation(TAPrimaryKey.class);
		if (primaryKey != null && primaryKey.name().trim().length() != 0)
			return primaryKey.name();

		return field.getName();
	}

	/**
	 * ���Ĭ��ֵ
	 * 
	 * @param field
	 * @return
	 */
	public static String getPropertyDefaultValue(Field field)
	{
		TAColumn column = field.getAnnotation(TAColumn.class);
		if (column != null && column.defaultValue().trim().length() != 0)
		{
			return column.defaultValue();
		}
		return null;
	}
}
