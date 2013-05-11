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

import java.util.ArrayList;
import java.util.List;

import com.ta.exception.TADBException;
import com.ta.exception.TADBNotOpenException;
import com.ta.util.TALogger;
import com.ta.util.db.entity.TAArrayList;
import com.ta.util.db.entity.TADBMasterEntity;
import com.ta.util.db.entity.TAHashMap;
import com.ta.util.db.entity.TAMapArrayList;
import com.ta.util.db.util.TADBUtils;
import com.ta.util.db.util.TASqlBuilderFactory;
import com.ta.util.db.util.sql.TASqlBuilder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * @Title TASQLiteDatabase
 * @Package com.ta.util.db
 * @Description ���ݿ�����࣬ͨ������������ݿ�Ĳ���
 * @author ��è
 * @date 2013-1-6
 * @version V1.0
 */
public class TASQLiteDatabase
{
	// ���ݿ�Ĭ������
	private final static String DB_NAME = "think_android.db"; // Ĭ�����ݿ�����
	private final static int DB_VERSION = 1;// Ĭ�����ݿ�汾
	// ��ǰSQLָ��
	private String queryStr = "";
	// ������Ϣ
	private String error = "";
	// ��ǰ��ѯCursor
	private Cursor queryCursor = null;
	// �Ƿ��Ѿ��������ݿ�
	private Boolean isConnect = false;
	// ִ��oepn�����ݿ�ʱ�����淵�ص����ݿ����
	private SQLiteDatabase mSQLiteDatabase = null;
	private TADBHelper mDatabaseHelper = null;
	private TADBUpdateListener mTadbUpdateListener;

	public TASQLiteDatabase(Context context)
	{
		TADBParams params = new TADBParams();
		this.mDatabaseHelper = new TADBHelper(context, params.getDbName(),
				null, params.getDbVersion());
	}

	/**
	 * ���캯��
	 * 
	 * @param context
	 *            ������
	 * @param params
	 *            ���ݲ�����Ϣ
	 */
	public TASQLiteDatabase(Context context, TADBParams params)
	{
		this.mDatabaseHelper = new TADBHelper(context, params.getDbName(),
				null, params.getDbVersion());
	}

	/**
	 * ���������ĵļ�����
	 * 
	 * @param dbUpdateListener
	 */
	public void setOnDbUpdateListener(TADBUpdateListener dbUpdateListener)
	{
		this.mTadbUpdateListener = dbUpdateListener;
		if (mTadbUpdateListener != null)
		{
			mDatabaseHelper.setOndbUpdateListener(mTadbUpdateListener);
		}
	}

	/**
	 * �����ݿ������ isWriteΪtrue,�������ʱ�׳�����
	 * 
	 * @param isWrite
	 * @return
	 */
	public SQLiteDatabase openDatabase(TADBUpdateListener dbUpdateListener,
			Boolean isWrite)
	{

		if (isWrite)
		{
			mSQLiteDatabase = openWritable(mTadbUpdateListener);
		} else
		{
			mSQLiteDatabase = openReadable(mTadbUpdateListener);
		}
		return mSQLiteDatabase;

	}

	/**
	 * �Զ�д��ʽ�����ݿ⣬һ�����ݿ�Ĵ��̿ռ����ˣ����ݿ�Ͳ�����ֻ�ܶ�������д�׳�����
	 * 
	 * @param dbUpdateListener
	 * @return
	 */
	public SQLiteDatabase openWritable(TADBUpdateListener dbUpdateListener)
	{
		if (dbUpdateListener != null)
		{
			this.mTadbUpdateListener = dbUpdateListener;
		}
		if (mTadbUpdateListener != null)
		{
			mDatabaseHelper.setOndbUpdateListener(mTadbUpdateListener);
		}
		try
		{
			mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
			isConnect = true;
			// ע�����ݿ�����������Ϣ
			// ��ʱ��д
		} catch (Exception e)
		{
			// TODO: handle exception
			isConnect = false;
		}

		return mSQLiteDatabase;
	}

	/**
	 * ���� TASQLiteDatabase�Ƿ����
	 * 
	 * @return
	 */
	public Boolean testSQLiteDatabase()
	{
		if (isConnect)
		{
			if (mSQLiteDatabase.isOpen())
			{
				return true;
			} else
			{
				return false;
			}
		} else
		{
			return false;
		}
	}

	/**
	 * �Զ�д��ʽ�����ݿ⣬������ݿ�Ĵ��̿ռ����ˣ��ͻ��ʧ�ܣ�����ʧ�ܺ�����������ֻ����ʽ�����ݿ⡣���������ɹ������
	 * ��ֻ�����ݿ����ͻ�رգ�Ȼ�󷵻�һ���ɶ�д�����ݿ����
	 * 
	 * @param dbUpdateListener
	 * @return
	 */
	public SQLiteDatabase openReadable(TADBUpdateListener dbUpdateListener)
	{
		if (dbUpdateListener != null)
		{
			this.mTadbUpdateListener = dbUpdateListener;
		}
		if (mTadbUpdateListener != null)
		{
			mDatabaseHelper.setOndbUpdateListener(mTadbUpdateListener);
		}
		try
		{
			mSQLiteDatabase = mDatabaseHelper.getReadableDatabase();
			isConnect = true;
			// ע�����ݿ�����������Ϣ
			// ��ʱ��д
		} catch (Exception e)
		{
			// TODO: handle exception
			isConnect = false;
		}

		return mSQLiteDatabase;
	}

	/**
	 * ִ�в�ѯ����Ҫ��SELECT, SHOW ��ָ�� �������ݼ�
	 * 
	 * @param sql
	 *            sql���
	 * @param selectionArgs
	 * @return
	 */
	public ArrayList<TAHashMap<String>> query(String sql, String[] selectionArgs)
	{
		TALogger.i(TASQLiteDatabase.this, sql);
		if (testSQLiteDatabase())
		{
			if (sql != null && !sql.equalsIgnoreCase(""))
			{
				this.queryStr = sql;
			}
			free();
			this.queryCursor = mSQLiteDatabase.rawQuery(sql, selectionArgs);
			if (queryCursor != null)
			{
				return getQueryCursorData();
			} else
			{
				TALogger.e(TASQLiteDatabase.this, "ִ��" + sql + "����");
			}
		} else
		{
			TALogger.e(TASQLiteDatabase.this, "���ݿ�δ�򿪣�");
		}
		return null;
	}

	/**
	 * ִ�в�ѯ����Ҫ��SELECT, SHOW ��ָ�� �������ݼ�
	 * 
	 * @param clazz
	 * @param distinct
	 *            �����ظ������Ϊtrue������,false���ù�
	 * @param where
	 *            where���
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
	@SuppressWarnings("unchecked")
	public <T> List<T> query(Class<?> clazz, boolean distinct, String where,
			String groupBy, String having, String orderBy, String limit)
	{

		if (testSQLiteDatabase())
		{
			List<T> list = null;
			TASqlBuilder getSqlBuilder = TASqlBuilderFactory.getInstance()
					.getSqlBuilder(TASqlBuilderFactory.SELECT);
			getSqlBuilder.setClazz(clazz);
			getSqlBuilder.setCondition(distinct, where, groupBy, having,
					orderBy, limit);
			try
			{
				String sqlString = getSqlBuilder.getSqlStatement();
				TALogger.i(TASQLiteDatabase.this, "ִ��" + sqlString);
				free();
				this.queryCursor = mSQLiteDatabase.rawQuery(sqlString, null);
				list = (List<T>) TADBUtils.getListEntity(clazz,
						this.queryCursor);
			} catch (IllegalArgumentException e)
			{
				// TODO Auto-generated catch block
				TALogger.e(TASQLiteDatabase.this, e.getMessage());
				e.printStackTrace();

			} catch (TADBException e)
			{
				// TODO Auto-generated catch block
				TALogger.e(TASQLiteDatabase.this, e.getMessage());
				e.printStackTrace();
			} catch (IllegalAccessException e)
			{
				// TODO Auto-generated catch block
				TALogger.e(TASQLiteDatabase.this, e.getMessage());
				e.printStackTrace();
			}
			return list;
		} else
		{
			return null;
		}

	}

	/**
	 * ��ѯ��¼
	 * 
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
	 * @return
	 */
	public ArrayList<TAHashMap<String>> query(String table, String[] columns,
			String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy)
	{
		if (testSQLiteDatabase())
		{
			this.queryCursor = mSQLiteDatabase.query(table, columns, selection,
					selectionArgs, groupBy, having, orderBy);
			if (queryCursor != null)
			{
				return getQueryCursorData();
			} else
			{
				TALogger.e(TASQLiteDatabase.this, "��ѯ" + table + "����");
			}
		} else
		{
			TALogger.e(TASQLiteDatabase.this, "���ݿ�δ�򿪣�");
		}
		return null;
	}

	/**
	 * ��ѯ��¼
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
	public ArrayList<TAHashMap<String>> query(String table, boolean distinct,
			String[] columns, String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy, String limit)
	{
		if (testSQLiteDatabase())
		{
			free();
			this.queryCursor = mSQLiteDatabase.query(distinct, table, columns,
					selection, selectionArgs, groupBy, having, orderBy, limit);
			if (queryCursor != null)
			{
				return getQueryCursorData();
			} else
			{
				TALogger.e(TASQLiteDatabase.this, "��ѯ" + table + "����");
			}
		} else
		{
			TALogger.e(TASQLiteDatabase.this, "���ݿ�δ�򿪣�");
		}
		return null;
	}

	/**
	 * ��ѯ��¼
	 * 
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
	public ArrayList<TAHashMap<String>> query(String table, String[] columns,
			String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy, String limit)
	{

		if (testSQLiteDatabase())
		{
			free();
			this.queryCursor = mSQLiteDatabase.query(table, columns, selection,
					selectionArgs, groupBy, having, orderBy, limit);
			if (queryCursor != null)
			{
				return getQueryCursorData();
			} else
			{
				TALogger.e(TASQLiteDatabase.this, "��ѯ" + table + "����");
			}
		} else
		{
			TALogger.e(TASQLiteDatabase.this, "���ݿ�δ�򿪣�");
		}
		return null;
	}

	/**
	 * ��ѯ��¼
	 * 
	 * @param cursorFactory
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
	public ArrayList<TAHashMap<String>> queryWithFactory(
			CursorFactory cursorFactory, boolean distinct, String table,
			String[] columns, String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy, String limit)
	{
		if (testSQLiteDatabase())
		{
			free();
			this.queryCursor = mSQLiteDatabase.queryWithFactory(cursorFactory,
					distinct, table, columns, selection, selectionArgs,
					groupBy, having, orderBy, limit);
			if (queryCursor != null)
			{
				return getQueryCursorData();
			} else
			{
				TALogger.e(TASQLiteDatabase.this, "��ѯ" + table + "����");
			}
		} else
		{
			TALogger.e(TASQLiteDatabase.this, "���ݿ�δ�򿪣�");
		}
		return null;

	}

	/**
	 * INSERT, UPDATE �Լ�DELETE
	 * 
	 * @param sql
	 *            ���
	 * @param bindArgs
	 * @throws TADBNotOpenException
	 */
	public void execute(String sql, String[] bindArgs)
			throws TADBNotOpenException
	{
		TALogger.i(TASQLiteDatabase.this, "׼��ִ��SQL[" + sql + "]���");
		if (testSQLiteDatabase())
		{
			if (sql != null && !sql.equalsIgnoreCase(""))
			{
				this.queryStr = sql;
				if (bindArgs != null)
				{
					mSQLiteDatabase.execSQL(sql, bindArgs);
				} else
				{
					mSQLiteDatabase.execSQL(sql);
				}

			}

		} else
		{
			throw new TADBNotOpenException("���ݿ�δ�򿪣�");
		}

	}

	/**
	 * ִ��INSERT, UPDATE �Լ�DELETE����
	 * 
	 * @param getSqlBuilder
	 *            Sql��乹����
	 * @return
	 */
	public Boolean execute(TASqlBuilder getSqlBuilder)
	{
		Boolean isSuccess = false;
		String sqlString;
		try
		{
			sqlString = getSqlBuilder.getSqlStatement();
			execute(sqlString, null);
			isSuccess = true;
		} catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			isSuccess = false;
			e.printStackTrace();

		} catch (TADBException e)
		{
			// TODO Auto-generated catch block
			isSuccess = false;
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			isSuccess = false;
			e.printStackTrace();
		} catch (TADBNotOpenException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			isSuccess = false;
		}
		return isSuccess;
	}

	/**
	 * ������еĲ�ѯ���ݼ��е�����
	 * 
	 * @return
	 */
	public TAMapArrayList<String> getQueryCursorData()
	{
		TAMapArrayList<String> arrayList = null;
		if (queryCursor != null)
		{
			try
			{
				arrayList = new TAMapArrayList<String>();
				queryCursor.moveToFirst();
				while (queryCursor.moveToNext())
				{
					arrayList.add(TADBUtils.getRowData(queryCursor));
				}
			} catch (Exception e)
			{
				e.printStackTrace();
				TALogger.e(TASQLiteDatabase.this, "��ǰ���ݼ���ȡʧ�ܣ�");
			}
		} else
		{
			TALogger.e(TASQLiteDatabase.this, "��ǰ���ݼ������ڣ�");
		}
		return arrayList;
	}

	/**
	 * ȡ�����ݿ�ı���Ϣ
	 * 
	 * @return
	 */
	public ArrayList<TADBMasterEntity> getTables()
	{
		ArrayList<TADBMasterEntity> tadbMasterArrayList = new ArrayList<TADBMasterEntity>();
		String sql = "select * from sqlite_master where type='table' order by name";
		TALogger.i(TASQLiteDatabase.this, sql);
		if (testSQLiteDatabase())
		{
			if (sql != null && !sql.equalsIgnoreCase(""))
			{
				this.queryStr = sql;
				free();
				queryCursor = mSQLiteDatabase
						.rawQuery(
								"select * from sqlite_master where type='table' order by name",
								null);

				if (queryCursor != null)
				{
					while (queryCursor.moveToNext())
					{
						if (queryCursor != null
								&& queryCursor.getColumnCount() > 0)
						{
							TADBMasterEntity tadbMasterEntity = new TADBMasterEntity();
							tadbMasterEntity.setType(queryCursor.getString(0));
							tadbMasterEntity.setName(queryCursor.getString(1));
							tadbMasterEntity.setTbl_name(queryCursor
									.getString(2));
							tadbMasterEntity.setRootpage(queryCursor.getInt(3));
							tadbMasterEntity.setSql(queryCursor.getString(4));
							tadbMasterArrayList.add(tadbMasterEntity);
						}
					}
				} else
				{
					TALogger.e(TASQLiteDatabase.this, "���ݿ�δ�򿪣�");
				}
			}
		} else
		{
			TALogger.e(TASQLiteDatabase.this, "���ݿ�δ�򿪣�");
		}
		return tadbMasterArrayList;
	}

	/**
	 * �ж��Ƿ����ĳ����,Ϊtrue����ڣ����򲻴���
	 * 
	 * @param clazz
	 * @return true����ڣ����򲻴���
	 */
	public boolean hasTable(Class<?> clazz)
	{
		String tableName = TADBUtils.getTableName(clazz);
		return hasTable(tableName);
	}

	/**
	 * �ж��Ƿ����ĳ����,Ϊtrue����ڣ����򲻴���
	 * 
	 * @param tableName
	 *            ��Ҫ�жϵı���
	 * @return true����ڣ����򲻴���
	 */
	public boolean hasTable(String tableName)
	{
		if (tableName != null && !tableName.equalsIgnoreCase(""))
		{
			if (testSQLiteDatabase())
			{
				tableName = tableName.trim();
				String sql = "select count(*) as c from Sqlite_master  where type ='table' and name ='"
						+ tableName + "' ";
				if (sql != null && !sql.equalsIgnoreCase(""))
				{
					this.queryStr = sql;
				}
				free();
				queryCursor = mSQLiteDatabase.rawQuery(sql, null);
				if (queryCursor.moveToNext())
				{
					int count = queryCursor.getInt(0);
					if (count > 0)
					{
						return true;
					}
				}
			} else
			{
				TALogger.e(TASQLiteDatabase.this, "���ݿ�δ�򿪣�");
			}
		} else
		{
			TALogger.e(TASQLiteDatabase.this, "�ж����ݱ�������Ϊ�գ�");
		}
		return false;
	}

	/**
	 * ������
	 * 
	 * @param clazz
	 * @return Ϊtrue�����ɹ���Ϊfalse����ʧ��
	 */
	public Boolean creatTable(Class<?> clazz)
	{
		Boolean isSuccess = false;
		if (testSQLiteDatabase())
		{
			try
			{
				String sqlString = TADBUtils.creatTableSql(clazz);
				execute(sqlString, null);
				isSuccess = true;
			} catch (TADBException e)
			{
				// TODO Auto-generated catch block
				isSuccess = false;
				e.printStackTrace();
				TALogger.e(TASQLiteDatabase.this, e.getMessage());
			} catch (TADBNotOpenException e)
			{
				// TODO Auto-generated catch block
				isSuccess = false;
				e.printStackTrace();
				TALogger.e(TASQLiteDatabase.this, e.getMessage());
			}
		} else
		{
			TALogger.e(TASQLiteDatabase.this, "���ݿ�δ�򿪣�");
			return false;
		}
		return isSuccess;
	}

	public Boolean dropTable(Class<?> clazz)
	{
		String tableName = TADBUtils.getTableName(clazz);
		return dropTable(tableName);
	}

	/**
	 * ɾ����
	 * 
	 * @param tableName
	 * @return Ϊtrue�����ɹ���Ϊfalse����ʧ��
	 */
	public Boolean dropTable(String tableName)
	{
		Boolean isSuccess = false;
		if (tableName != null && !tableName.equalsIgnoreCase(""))
		{
			if (testSQLiteDatabase())
			{
				try
				{
					String sqlString = "DROP TABLE " + tableName;
					execute(sqlString, null);
					isSuccess = true;
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					isSuccess = false;
					e.printStackTrace();
					TALogger.e(TASQLiteDatabase.this, e.getMessage());
				}
			} else
			{
				TALogger.e(TASQLiteDatabase.this, "���ݿ�δ�򿪣�");
				return false;
			}
		} else
		{
			TALogger.e(TASQLiteDatabase.this, "ɾ�����ݱ�������Ϊ�գ�");
		}
		return isSuccess;
	}

	/**
	 * ���±����ڶ�ʵ���޸�ʱ���ı�� ��ʱ��д
	 * 
	 * @param tableName
	 * @return
	 */
	public Boolean alterTable(String tableName)
	{
		return false;
	}

	/**
	 * ���ݿ������Ϣ ����ʾ��ǰ��SQL���
	 * 
	 * @return
	 */
	public String error()
	{
		if (this.queryStr != null && !queryStr.equalsIgnoreCase(""))
		{
			error = error + "\n [ SQL��� ] : " + queryStr;
		}
		TALogger.e(TASQLiteDatabase.this, error);
		return error;
	}

	/**
	 * �����¼
	 * 
	 * @param entity
	 *            �����ʵ��
	 * @return
	 */
	public Boolean insert(Object entity)
	{
		return insert(entity, null);
	}

	/**
	 * �����¼
	 * 
	 * @param table
	 *            ��Ҫ���뵽�ı�
	 * @param nullColumnHack
	 *            ������Ϊ�յ���
	 * @param values
	 *            �����ֵ
	 * @return
	 */
	public Boolean insert(String table, String nullColumnHack,
			ContentValues values)
	{
		if (testSQLiteDatabase())
		{
			return mSQLiteDatabase.insert(table, nullColumnHack, values) > 0;
		} else
		{
			TALogger.e(TASQLiteDatabase.this, "���ݿ�δ�򿪣�");
			return false;
		}
	}

	/**
	 * �����¼
	 * 
	 * @param table
	 *            ��Ҫ���뵽�ı�
	 * @param nullColumnHack
	 *            ������Ϊ�յ���
	 * @param values
	 *            �����ֵ
	 * @return
	 */
	public Boolean insertOrThrow(String table, String nullColumnHack,
			ContentValues values)
	{
		if (testSQLiteDatabase())
		{
			return mSQLiteDatabase.insertOrThrow(table, nullColumnHack, values) > 0;
		} else
		{
			TALogger.e(TASQLiteDatabase.this, "���ݿ�δ�򿪣�");
			return false;
		}
	}

	/**
	 * �����¼
	 * 
	 * @param entity
	 *            ��������ʵ��
	 * @param updateFields
	 *            ���뵽���ֶ�,������Ϊ��
	 * @return ����trueִ�гɹ�������ִ��ʧ��
	 */
	public Boolean insert(Object entity, TAArrayList updateFields)
	{

		TASqlBuilder getSqlBuilder = TASqlBuilderFactory.getInstance()
				.getSqlBuilder(TASqlBuilderFactory.INSERT);
		getSqlBuilder.setEntity(entity);
		getSqlBuilder.setUpdateFields(updateFields);
		return execute(getSqlBuilder);
	}

	/**
	 * ɾ����¼
	 * 
	 * @param table
	 *            ��ɾ���ı���
	 * @param whereClause
	 *            ���õ�WHERE�Ӿ�ʱ��ɾ��ָ�������� ,���null��ɾ�����е��С�
	 * @param whereArgs
	 * 
	 * @return ����trueִ�гɹ�������ִ��ʧ��
	 */
	public Boolean delete(String table, String whereClause, String[] whereArgs)
	{
		if (testSQLiteDatabase())
		{
			return mSQLiteDatabase.delete(table, whereClause, whereArgs) > 0;

		} else
		{
			TALogger.e(TASQLiteDatabase.this, "���ݿ�δ�򿪣�");
			return false;
		}
	}

	/**
	 * ɾ����¼
	 * 
	 * @param clazz
	 * @param where
	 *            where���
	 * @return ����trueִ�гɹ�������ִ��ʧ��
	 */
	public Boolean delete(Class<?> clazz, String where)
	{
		if (testSQLiteDatabase())
		{
			TASqlBuilder getSqlBuilder = TASqlBuilderFactory.getInstance()
					.getSqlBuilder(TASqlBuilderFactory.DELETE);
			getSqlBuilder.setClazz(clazz);
			getSqlBuilder.setCondition(false, where, null, null, null, null);
			return execute(getSqlBuilder);
		} else
		{
			return false;
		}

	}

	/**
	 * ɾ����¼
	 * 
	 * @param entity
	 * @return ����trueִ�гɹ�������ִ��ʧ��
	 */
	public Boolean delete(Object entity)
	{
		if (testSQLiteDatabase())
		{
			TASqlBuilder getSqlBuilder = TASqlBuilderFactory.getInstance()
					.getSqlBuilder(TASqlBuilderFactory.DELETE);
			getSqlBuilder.setEntity(entity);
			return execute(getSqlBuilder);
		} else
		{
			return false;
		}

	}

	/**
	 * ���¼�¼
	 * 
	 * @param table
	 *            ������
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 * @return ����trueִ�гɹ�������ִ��ʧ��
	 */
	public Boolean update(String table, ContentValues values,
			String whereClause, String[] whereArgs)
	{
		if (testSQLiteDatabase())
		{
			return mSQLiteDatabase
					.update(table, values, whereClause, whereArgs) > 0;
		} else
		{
			TALogger.e(TASQLiteDatabase.this, "���ݿ�δ�򿪣�");
			return false;
		}
	}

	/**
	 * ���¼�¼ ���ָ��·�ʽֻ�в�������������������¿���
	 * 
	 * @param entity
	 *            ���µ�����
	 * @return ����trueִ�гɹ�������ִ��ʧ��
	 */
	public Boolean update(Object entity)
	{
		return update(entity, null);
	}

	/**
	 * ���¼�¼
	 * 
	 * @param entity
	 *            ���µ�����
	 * @param where
	 *            where���
	 * @return
	 */
	public Boolean update(Object entity, String where)
	{
		if (testSQLiteDatabase())
		{
			TASqlBuilder getSqlBuilder = TASqlBuilderFactory.getInstance()
					.getSqlBuilder(TASqlBuilderFactory.UPDATE);
			getSqlBuilder.setEntity(entity);
			getSqlBuilder.setCondition(false, where, null, null, null, null);
			return execute(getSqlBuilder);
		} else
		{
			return false;
		}

	}

	/**
	 * ��ȡ���һ�β�ѯ��sql���
	 * 
	 * @return sql ���
	 */
	public String getLastSql()
	{
		return queryStr;
	}

	/**
	 * ��õ�ǰ��ѯ���ݼ���
	 * 
	 * @return
	 */
	public Cursor getQueryCursor()
	{
		return queryCursor;
	}

	/**
	 * �ر����ݿ�
	 */
	public void close()
	{
		mSQLiteDatabase.close();
	}

	/**
	 * �ͷŲ�ѯ���
	 */
	public void free()
	{
		if (queryCursor != null)
		{
			try
			{
				this.queryCursor.close();
			} catch (Exception e)
			{
				// TODO: handle exception
			}
		}

	}

	/**
	 * ���ݿ����ò���
	 */
	public static class TADBParams
	{
		private String dbName = DB_NAME;
		private int dbVersion = DB_VERSION;

		public TADBParams()
		{
		}

		public TADBParams(String dbName, int dbVersion)
		{
			this.dbName = dbName;
			this.dbVersion = dbVersion;
		}

		public String getDbName()
		{
			return dbName;
		}

		public void setDbName(String dbName)
		{
			this.dbName = dbName;
		}

		public int getDbVersion()
		{
			return dbVersion;
		}

		public void setDbVersion(int dbVersion)
		{
			this.dbVersion = dbVersion;
		}
	}

	/**
	 * Interface ���ݿ������ص�
	 */
	public interface TADBUpdateListener
	{
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
	}
}
