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
package com.ta.util.config;

/**
 * @Title TAIConfig
 * @Package com.ta.util.config
 * @Description �������Ľӿ�
 * @author ��è
 * @date 2013-4-3 ���� 9:35
 * @version V1.0
 */
public interface TAIConfig
{
	/**
	 * ����������
	 * 
	 */
	void loadConfig();

	/**
	 * �ж��Ƿ��Ѿ�����������
	 * 
	 * @return �����Ƿ���ص���Ϣ��false����û�м��أ�true�������
	 */
	Boolean isLoadConfig();

	/**
	 * ����������
	 */
	void open();

	/**
	 * �ر�������
	 */
	void close();

	/**
	 * �ж��������Ƿ�ر�
	 * 
	 * @return ���Ϊtrue��رգ����Ϊfalse����
	 */
	boolean isClosed();

	/**
	 * ����String���͵�����ֵ
	 * 
	 * @param key
	 *            ������
	 * @param value
	 *            ����ֵ
	 */
	void setString(String key, String value);

	/**
	 * ����int���͵�����ֵ
	 * 
	 * @param key
	 *            ������
	 * @param value
	 *            ����ֵ
	 */
	void setInt(String key, int value);

	/**
	 * ����Boolean���͵�����ֵ
	 * 
	 * @param key
	 *            ������
	 * @param value
	 *            ����ֵ
	 */
	void setBoolean(String key, Boolean value);

	/**
	 * ����Byte���͵�����ֵ
	 * 
	 * @param key
	 *            ������
	 * @param value
	 *            ����ֵ
	 */
	void setByte(String key, byte[] value);

	/**
	 * ����Short���͵�����ֵ
	 * 
	 * @param key
	 *            ������
	 * @param value
	 *            ����ֵ
	 */
	void setShort(String key, short value);

	/**
	 * ����Long���͵�����ֵ
	 * 
	 * @param key
	 *            ������
	 * @param value
	 *            ����ֵ
	 */
	void setLong(String key, long value);

	/**
	 * ����Float���͵�����ֵ
	 * 
	 * @param key
	 *            ������
	 * @param value
	 *            ����ֵ
	 */
	void setFloat(String key, float value);

	/**
	 * ����Double���͵�����ֵ
	 * 
	 * @param key
	 *            ������
	 * @param value
	 *            ����ֵ
	 */
	void setDouble(String key, double value);

	// ����Դ����
	/**
	 * ����String���͵�����ֵ
	 * 
	 * @param resID
	 *            ��ԴID
	 * @param value
	 *            ����ֵ
	 */
	void setString(int resID, String value);

	/**
	 * ����Int���͵�����ֵ
	 * 
	 * @param resID
	 *            ��ԴID
	 * @param value
	 *            ����ֵ
	 */
	void setInt(int resID, int value);

	/**
	 * ����Boolean���͵�����ֵ
	 * 
	 * @param resID
	 *            ��ԴID
	 * @param value
	 *            ����ֵ
	 */
	void setBoolean(int resID, Boolean value);

	/**
	 * ����Byte���͵�����ֵ
	 * 
	 * @param resID
	 *            ��ԴID
	 * @param value
	 *            ����ֵ
	 */
	void setByte(int resID, byte[] value);

	/**
	 * ����Short���͵�����ֵ
	 * 
	 * @param resID
	 *            ��ԴID
	 * @param value
	 *            ����ֵ
	 */
	void setShort(int resID, short value);

	/**
	 * ����Long���͵�����ֵ
	 * 
	 * @param resID
	 *            ��ԴID
	 * @param value
	 *            ����ֵ
	 */
	void setLong(int resID, long value);

	/**
	 * ����Float���͵�����ֵ
	 * 
	 * @param resID
	 *            ��ԴID
	 * @param value
	 *            ����ֵ
	 */
	void setFloat(int resID, float value);

	/**
	 * ����Double���͵�����ֵ
	 * 
	 * @param resID
	 *            ��ԴID
	 * @param value
	 *            ����ֵ
	 */
	void setDouble(int resID, double value);

	/**
	 * �Զ������������ã�һ������ֵ
	 * 
	 * @param entity
	 *            �����ʵ��
	 * 
	 */
	void setConfig(Object entity);

	// ����Ϊget����

	/**
	 * ����String���͵�����ֵ
	 * 
	 * @param key
	 *            ������
	 * @return ��������ֵ����ֵ
	 */
	String getString(String key, String defaultValue);

	/**
	 * ����int���͵�����ֵ
	 * 
	 * @param key
	 *            ������
	 * @return ��������ֵ����ֵ
	 */
	int getInt(String key, int defaultValue);

	/**
	 * ����Boolean���͵�����ֵ
	 * 
	 * @param key
	 *            ������
	 * @return ��������ֵ����ֵ
	 */
	boolean getBoolean(String key, Boolean defaultValue);

	/**
	 * ����Byte���͵�����ֵ
	 * 
	 * @param key
	 *            ������
	 * @return ��������ֵ����ֵ
	 */
	byte[] getByte(String key, byte[] defaultValue);

	/**
	 * ����Short���͵�����ֵ
	 * 
	 * @param key
	 *            ������
	 * @return ��������ֵ����ֵ
	 */
	short getShort(String key, Short defaultValue);

	/**
	 * ����Long���͵�����ֵ
	 * 
	 * @param key
	 *            ������
	 * @return ��������ֵ����ֵ
	 */
	long getLong(String key, Long defaultValue);

	/**
	 * ����Float���͵�����ֵ
	 * 
	 * @param key
	 *            ������
	 * @return ��������ֵ����ֵ
	 */
	float getFloat(String key, Float defaultValue);

	/**
	 * ����Double���͵�����ֵ
	 * 
	 * @param key
	 *            ������
	 * @return ��������ֵ����ֵ
	 */
	double getDouble(String key, Double defaultValue);

	// ��Դ�ͻ�ȡ����Ϊget����
	/**
	 * ����String���͵�����ֵ
	 * 
	 * @param resID
	 *            ��ԴID
	 * @return ��������ֵ����ֵ
	 */
	String getString(int resID, String defaultValue);

	/**
	 * ����int���͵�����ֵ
	 * 
	 * @param resID
	 *            ��ԴID
	 * @return ��������ֵ����ֵ
	 */
	int getInt(int resID, int defaultValue);

	/**
	 * ����Boolean���͵�����ֵ
	 * 
	 * @param resID
	 *            ��ԴID
	 * @return ��������ֵ����ֵ
	 */
	boolean getBoolean(int resID, Boolean defaultValue);

	/**
	 * ����Byte���͵�����ֵ
	 * 
	 * @param resID
	 *            ��ԴID
	 * @return ��������ֵ����ֵ
	 */
	byte[] getByte(int resID, byte[] defaultValue);

	/**
	 * ����Short���͵�����ֵ
	 * 
	 * @param resID
	 *            ��ԴID
	 * @return ��������ֵ����ֵ
	 */
	short getShort(int resID, Short defaultValue);

	/**
	 * ����Long���͵�����ֵ
	 * 
	 * @param resID
	 *            ��ԴID
	 * @return ��������ֵ����ֵ
	 */
	long getLong(int resID, Long defaultValue);

	/**
	 * ����Float���͵�����ֵ
	 * 
	 * @param resID
	 *            ��ԴID
	 * @return ��������ֵ����ֵ
	 */
	float getFloat(int resID, Float defaultValue);

	/**
	 * ����Double���͵�����ֵ
	 * 
	 * @param resID
	 *            ��ԴID
	 * @return ��������ֵ����ֵ
	 */
	double getDouble(int resID, Double defaultValue);

	/**
	 * ����Double���͵�����ֵ
	 * 
	 * @param clazz
	 *            ������Դ������
	 * @return ���ش�����ֵ�Ķ���
	 */
	<T extends Object> T getConfig(Class<T> clazz);

	/**
	 * ɾ������ֵ
	 * 
	 * @param key
	 */
	void remove(String key);

	/**
	 * ɾ��һ������ֵ
	 * 
	 * @param key
	 */
	void remove(String... key);

	/**
	 * �����������
	 */
	void clear();
}
