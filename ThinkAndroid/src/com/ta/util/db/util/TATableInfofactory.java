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
import java.util.HashMap;
import java.util.List;

import com.ta.exception.TADBException;
import com.ta.util.db.entity.TAPKProperyEntity;
import com.ta.util.db.entity.TAPropertyEntity;
import com.ta.util.db.entity.TATableInfoEntity;

/**
 * @Title TATableInfofactory
 * @Package com.ta.util.db.util.sql
 * @Description ���ݿ������
 * @author ��è
 * @date 2013-1-20
 * @version V1.0
 */
public class TATableInfofactory
{
	/**
	 * ����Ϊ��������ϢΪֵ��HashMap
	 */
	private static final HashMap<String, TATableInfoEntity> tableInfoEntityMap = new HashMap<String, TATableInfoEntity>();

	private TATableInfofactory()
	{

	}

	private static TATableInfofactory instance;

	/**
	 * ������ݿ����
	 * 
	 * @return ���ݿ����
	 */
	public static TATableInfofactory getInstance()
	{
		if (instance == null)
		{
			instance = new TATableInfofactory();
		}
		return instance;
	}

	/**
	 * ��ñ���Ϣ
	 * 
	 * @param clazz
	 *            ʵ������
	 * @return ����Ϣ
	 * @throws TADBException
	 */
	public TATableInfoEntity getTableInfoEntity(Class<?> clazz)
			throws TADBException
	{
		if (clazz == null)
			throw new TADBException("����Ϣ��ȡʧ�ܣ�ӦΪclassΪnull");
		TATableInfoEntity tableInfoEntity = tableInfoEntityMap.get(clazz
				.getName());
		if (tableInfoEntity == null)
		{
			tableInfoEntity = new TATableInfoEntity();
			tableInfoEntity.setTableName(TADBUtils.getTableName(clazz));
			tableInfoEntity.setClassName(clazz.getName());
			Field idField = TADBUtils.getPrimaryKeyField(clazz);
			if (idField != null)
			{
				TAPKProperyEntity pkProperyEntity = new TAPKProperyEntity();
				pkProperyEntity.setColumnName(TADBUtils
						.getColumnByField(idField));
				pkProperyEntity.setName(idField.getName());
				pkProperyEntity.setType(idField.getType());
				pkProperyEntity.setAutoIncrement(TADBUtils
						.isAutoIncrement(idField));
				tableInfoEntity.setPkProperyEntity(pkProperyEntity);
			} else
			{
				tableInfoEntity.setPkProperyEntity(null);
			}
			List<TAPropertyEntity> propertyList = TADBUtils
					.getPropertyList(clazz);
			if (propertyList != null)
			{
				tableInfoEntity.setPropertieArrayList(propertyList);
			}

			tableInfoEntityMap.put(clazz.getName(), tableInfoEntity);
		}
		if (tableInfoEntity == null
				|| tableInfoEntity.getPropertieArrayList() == null
				|| tableInfoEntity.getPropertieArrayList().size() == 0)
		{
			throw new TADBException("���ܴ���+" + clazz + "�ı���Ϣ");
		}
		return tableInfoEntity;
	}
}
