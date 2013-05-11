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
package com.ta.util.netstate;

import java.util.ArrayList;
import com.ta.util.TALogger;
import com.ta.util.netstate.TANetWorkUtil.netType;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;

/**
 * @Title NetworkStateReceiver
 * @Package com.ta.util.netstate
 * @Description ��һ���������״̬�ı�ģ���Ҫ���� <receiver
 *              android:name="com.ta.util.netstate.TANetworkStateReceiver" >
 *              <intent-filter> <action
 *              android:name="android.net.conn.CONNECTIVITY_CHANGE" /> <action
 *              android:name="android.gzcpc.conn.CONNECTIVITY_CHANGE" />
 *              </intent-filter> </receiver>
 * 
 *              ��Ҫ����Ȩ�� <uses-permission
 *              android:name="android.permission.CHANGE_NETWORK_STATE" />
 *              <uses-permission
 *              android:name="android.permission.CHANGE_WIFI_STATE" />
 *              <uses-permission
 *              android:name="android.permission.ACCESS_NETWORK_STATE" />
 *              <uses-permission
 *              android:name="android.permission.ACCESS_WIFI_STATE" />
 * @author ��è
 * @date 2013-5-5 ���� 22:47
 * @version V1.2
 */
public class TANetworkStateReceiver extends BroadcastReceiver
{
	private static Boolean networkAvailable = false;
	private static netType netType;
	private static ArrayList<TANetChangeObserver> netChangeObserverArrayList = new ArrayList<TANetChangeObserver>();
	private final static String ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
	public final static String TA_ANDROID_NET_CHANGE_ACTION = "ta.android.net.conn.CONNECTIVITY_CHANGE";
	private static BroadcastReceiver receiver;

	private static BroadcastReceiver getReceiver()
	{
		if (receiver == null)
		{
			receiver = new TANetworkStateReceiver();
		}
		return receiver;
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{
		receiver = TANetworkStateReceiver.this;
		if (intent.getAction().equalsIgnoreCase(ANDROID_NET_CHANGE_ACTION)
				|| intent.getAction().equalsIgnoreCase(
						TA_ANDROID_NET_CHANGE_ACTION))
		{
			TALogger.i(TANetworkStateReceiver.this, "����״̬�ı�.");
			if (!TANetWorkUtil.isNetworkAvailable(context))
			{
				TALogger.i(TANetworkStateReceiver.this, "û����������.");
				networkAvailable = false;
			} else
			{
				TALogger.i(TANetworkStateReceiver.this, "�������ӳɹ�.");
				netType = TANetWorkUtil.getAPNType(context);
				networkAvailable = true;
			}
			notifyObserver();
		}
	}

	/**
	 * ע������״̬�㲥
	 * 
	 * @param mContext
	 */
	public static void registerNetworkStateReceiver(Context mContext)
	{
		IntentFilter filter = new IntentFilter();
		filter.addAction(TA_ANDROID_NET_CHANGE_ACTION);
		filter.addAction(ANDROID_NET_CHANGE_ACTION);
		mContext.getApplicationContext()
				.registerReceiver(getReceiver(), filter);
	}

	/**
	 * �������״̬
	 * 
	 * @param mContext
	 */
	public static void checkNetworkState(Context mContext)
	{
		Intent intent = new Intent();
		intent.setAction(TA_ANDROID_NET_CHANGE_ACTION);
		mContext.sendBroadcast(intent);
	}

	/**
	 * ע������״̬�㲥
	 * 
	 * @param mContext
	 */
	public static void unRegisterNetworkStateReceiver(Context mContext)
	{
		if (receiver != null)
		{
			try
			{
				mContext.getApplicationContext().unregisterReceiver(receiver);
			} catch (Exception e)
			{
				// TODO: handle exception
				TALogger.d("TANetworkStateReceiver", e.getMessage());
			}
		}

	}

	/**
	 * ��ȡ��ǰ����״̬��trueΪ�������ӳɹ���������������ʧ��
	 * 
	 * @return
	 */
	public static Boolean isNetworkAvailable()
	{
		return networkAvailable;
	}

	public static netType getAPNType()
	{
		return netType;
	}

	private void notifyObserver()
	{

		for (int i = 0; i < netChangeObserverArrayList.size(); i++)
		{
			TANetChangeObserver observer = netChangeObserverArrayList.get(i);
			if (observer != null)
			{
				if (isNetworkAvailable())
				{
					observer.onConnect(netType);
				} else
				{
					observer.onDisConnect();
				}
			}
		}

	}

	/**
	 * ע���������ӹ۲���
	 * 
	 * @param observerKey
	 *            observerKey
	 */
	public static void registerObserver(TANetChangeObserver observer)
	{
		if (netChangeObserverArrayList == null)
		{
			netChangeObserverArrayList = new ArrayList<TANetChangeObserver>();
		}
		netChangeObserverArrayList.add(observer);
	}

	/**
	 * ע���������ӹ۲���
	 * 
	 * @param resID
	 *            observerKey
	 */
	public static void removeRegisterObserver(TANetChangeObserver observer)
	{
		if (netChangeObserverArrayList != null)
		{
			netChangeObserverArrayList.remove(observer);
		}
	}

}