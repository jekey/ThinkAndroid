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
package com.ta;

import com.ta.mvc.command.TAIdentityCommand;
import com.ta.mvc.common.TAIResponseListener;
import com.ta.mvc.common.TARequest;
import com.ta.mvc.common.TAResponse;
import com.ta.util.TALogger;
import com.ta.util.netstate.TANetWorkUtil.netType;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public abstract class TAActivity extends Activity
{
	/** ģ������� */
	private String moduleName = "";
	/** �����ļ������� */
	private String layouName = "";
	private static final int DIALOG_ID_PROGRESS_DEFAULT = 0x174980;
	private static final String TAIDENTITYCOMMAND = "taidentitycommand";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		notifiyApplicationActivityCreating();
		onPreOnCreate(savedInstanceState);
		super.onCreate(savedInstanceState);
		getTAApplication().getAppManager().addActivity(this);
		initActivity();
		onAfterOnCreate(savedInstanceState);
		notifiyApplicationActivityCreated();
	}

	public TAApplication getTAApplication()
	{
		return (TAApplication) getApplication();
	}

	private void notifiyApplicationActivityCreating()
	{
		getTAApplication().onActivityCreating(this);
	}

	private void notifiyApplicationActivityCreated()
	{
		getTAApplication().onActivityCreated(this);
	}

	protected void onPreOnCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		getTAApplication().registerCommand(TAIDENTITYCOMMAND,
				TAIdentityCommand.class);
	}

	private void initActivity()
	{
		// ��ʼ��ģ����
		getModuleName();
		// ��ʼ��������
		getLayouName();
		// ������ע����
		initInjector();
		// �Զ�����Ĭ�ϲ���
		loadDefautLayout();
	}

	protected void onAfterOnCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
	}

	/**
	 * ��ʼ��ע����
	 */
	private void initInjector()
	{
		// TODO Auto-generated method stub
		getTAApplication().getInjector().injectResource(this);
		getTAApplication().getInjector().inject(this);
	}

	/**
	 * �Զ�����Ĭ�ϲ���
	 */
	private void loadDefautLayout()
	{
		try
		{
			int layoutResID = getTAApplication().getLayoutLoader().getLayoutID(
					layouName);
			setContentView(layoutResID);
		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	public void setContentView(int layoutResID)
	{
		// TODO Auto-generated method stub
		super.setContentView(layoutResID);
		// ����view��������ͼ����֮�����ע��
		getTAApplication().getInjector().injectView(this);
		onAfterSetContentView();
	}

	public void setContentView(View view, LayoutParams params)
	{
		super.setContentView(view, params);
		// ����view��������ͼ����֮�����ע��
		getTAApplication().getInjector().injectView(this);
		onAfterSetContentView();
	}

	public void setContentView(View view)
	{
		super.setContentView(view);
		// ����view��������ͼ����֮�����ע��
		getTAApplication().getInjector().injectView(this);
		onAfterSetContentView();
	}

	protected void onAfterSetContentView()
	{
		// TODO Auto-generated method stub

	}

	/**
	 * ��ȡģ�������
	 */
	public String getModuleName()
	{
		String moduleName = this.moduleName;
		if (moduleName == null || moduleName.equalsIgnoreCase(""))
		{
			moduleName = getClass().getName().substring(0,
					getClass().getName().length() - 8);
			String arrays[] = moduleName.split("\\.");
			this.moduleName = moduleName = arrays[arrays.length - 1]
					.toLowerCase();
		}
		return moduleName;
	}

	/**
	 * ����ģ�������
	 */
	public void setModuleName(String moduleName)
	{
		this.moduleName = moduleName;
	}

	/**
	 * ��ȡ�����ļ���
	 * 
	 * @return�����ļ���
	 */
	public String getLayouName()
	{
		String layouName = this.layouName;
		if (layouName == null || layouName.equalsIgnoreCase(""))
		{
			this.layouName = this.moduleName;
		}
		return layouName;
	}

	/**
	 * ���ò����ļ���
	 */
	protected void setLayouName(String layouName)
	{
		this.layouName = layouName;
	}

	public void preProcessData(TAResponse response)
	{

	}

	public void processData(TAResponse response)
	{

	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		switch (id)
		{
		case DIALOG_ID_PROGRESS_DEFAULT:
			ProgressDialog dlg = new ProgressDialog(this);
			// dlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dlg.setMessage("���ڼ���...");
			dlg.setCancelable(true);
			return dlg;
		default:
			return super.onCreateDialog(id);

		}
	}

	public final void doCommand(int resId, TARequest request)
	{
		String commandKey = getString(resId);
		doCommand(commandKey, request, null, true);
	}

	public final void doCommand(String commandKey, TARequest request)
	{
		doCommand(commandKey, request, null, true);
	}

	public final void doCommand(int resId, TARequest request,
			TAIResponseListener listener)
	{
		String commandKey = getString(resId);
		doCommand(commandKey, request, listener, true);
	}

	public final void doCommand(String commandKey, TARequest request,
			TAIResponseListener listener)
	{
		doCommand(commandKey, request, listener, true);
	}

	public final void doCommand(int resId, TARequest request,
			TAIResponseListener listener, boolean showProgress)
	{
		String commandKey = getString(resId);
		TALogger.i(TAActivity.this, "go with cmdid=" + commandKey
				+ ", request: " + request);
		doCommand(commandKey, request, listener, showProgress, true);
	}

	public final void doCommand(String commandKey, TARequest request,
			TAIResponseListener listener, boolean showProgress)
	{
		TALogger.i(TAActivity.this, "go with cmdid=" + commandKey
				+ ", request: " + request);
		doCommand(commandKey, request, listener, showProgress, true);
	}

	public final void doCommand(int resId, TARequest request,
			TAIResponseListener listener, boolean showProgress, boolean record)
	{
		String commandKey = getString(resId);
		TALogger.i(TAActivity.this, "go with cmdid=" + commandKey
				+ ", record: " + record + ", request: " + request);
		doCommand(commandKey, request, listener, showProgress, record, false);
	}

	public final void doCommand(String commandKey, TARequest request,
			TAIResponseListener listener, boolean showProgress, boolean record)
	{
		TALogger.i(TAActivity.this, "go with cmdid=" + commandKey
				+ ", record: " + record + ", request: " + request);
		doCommand(commandKey, request, listener, showProgress, record, false);
	}

	public final void doCommand(int resId, TARequest request,
			TAIResponseListener listener, boolean showProgress, boolean record,
			boolean resetStack)
	{
		String commandKey = getString(resId);
		doCommand(commandKey, request, listener, showProgress, record,
				resetStack);
	}

	public final void doCommand(String commandKey, TARequest request,
			TAIResponseListener listener, boolean showProgress, boolean record,
			boolean resetStack)
	{
		if (showProgress)
		{
			showProgress();
		}
		getTAApplication().doCommand(commandKey, request, listener, record,
				resetStack);
	}

	/**
	 * ����
	 */
	public final void back()
	{
		getTAApplication().back();
	}

	/**
	 * ��Ҫ�Զ��������������д
	 */
	protected void showProgress()
	{
		showDialog(DIALOG_ID_PROGRESS_DEFAULT);
	}

	/**
	 * ���ؽ���������Ҫ��д������д
	 */
	protected void hideProgress()
	{
		try
		{
			removeDialog(DIALOG_ID_PROGRESS_DEFAULT);
		} catch (IllegalArgumentException iae)
		{
		}
	}

	/**
	 * ������������ʱ����
	 */
	public void onConnect(netType type)
	{

	}

	/**
	 * ��ǰû����������
	 */
	public void onDisConnect()
	{

	}

	@Override
	public void finish()
	{
		// TODO Auto-generated method stub
		getTAApplication().getAppManager().removeActivity(this);
		super.finish();
	}

	/**
	 * �˳�Ӧ�ó���
	 * 
	 * @param isBackground
	 *            �Ƿ񿪿�����̨����,���Ϊtrue��Ϊ��̨����
	 */
	public void exitApp(Boolean isBackground)
	{
		getTAApplication().exitApp(isBackground);
	}

	/**
	 * �˳�Ӧ�ó���
	 * 
	 */
	public void exitApp()
	{
		getTAApplication().exitApp(false);
	}

	/**
	 * �˳�Ӧ�ó������ں�̨����
	 * 
	 */
	public void exitAppToBackground()
	{
		getTAApplication().exitApp(true);
	}

	/**
	 * ����activity
	 * 
	 * @param activityResID
	 */
	public final void doActivity(int activityResID)
	{
		doActivity(activityResID, null);
	}

	public final void doActivity(int activityResID, Bundle bundle)
	{
		TARequest request = new TARequest();
		request.setData(bundle);
		request.setActivityKeyResID(activityResID);
		// ����activity
		doCommand(TAIDENTITYCOMMAND, request);
	}

	@Override
	public void onBackPressed()
	{
		back();
	}
}
