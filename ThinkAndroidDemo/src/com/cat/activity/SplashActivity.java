package com.cat.activity;

import com.cat.command.TestMVCCommand;
import com.ta.TAApplication;
import com.ta.util.cache.TAFileCache;
import com.ta.util.cache.TAFileCache.TACacheParams;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

/**
 * @Title: �û�����������
 * @Package com.cat.activity
 * @Description: �û�����������ʱ���һ������ҳ�����һЩ��ʼ������
 * @author ��è
 * @date 2013-5-6
 * @version V1.0
 */
public class SplashActivity extends ThinkAndroidBaseActivity
{
	private static final String SYSTEMCACHE = "thinkandroid";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// TANetworkStateReceiver.registerNetworkStateReceiver(this);
		final View view = View.inflate(this, R.layout.splash, null);
		setContentView(view);
		// ����չʾ������
		AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
		aa.setDuration(5000);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener()
		{
			@Override
			public void onAnimationEnd(Animation arg0)
			{
				startMain();
			}

			@Override
			public void onAnimationRepeat(Animation animation)
			{
			}

			@Override
			public void onAnimationStart(Animation animation)
			{
			}

		});

	}

	@Override
	protected void onPreOnCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onPreOnCreate(savedInstanceState);
		TAApplication application = (TAApplication) getApplication();
		// ����ϵͳ�Ļ���,����ѡ���Ե�����
		TACacheParams cacheParams = new TACacheParams(this, SYSTEMCACHE);
		TAFileCache fileCache = new TAFileCache(cacheParams);
		application.setFileCache(fileCache);
		// ע��activity
		getTAApplication().registerCommand(R.string.testmvccommand,
				TestMVCCommand.class);
		// ע��activity
		getTAApplication().registerActivity(R.string.thinkandroidmainactivity,
				ThinkAndroidMainActivity.class);
		// ע��activity
		getTAApplication().registerActivity(R.string.thinkandroidcacheactivtiy,
				ThinkAndroidCacheActivtiy.class);
		// ע��activity
		getTAApplication().registerActivity(R.string.thinkandroiddbactivtiy,
				ThinkAndroidDBActivtiy.class);
		// ע��activity
		getTAApplication().registerActivity(
				R.string.thinkandroidimagecacheactivtiy,
				ThinkAndroidImageCacheActivtiy.class);
		// ע��activity
		getTAApplication().registerActivity(R.string.thinkandroidmvcactivtiy,
				ThinkAndroidMvcActivtiy.class);
		// ע��activity
		getTAApplication().registerActivity(R.string.thinkandroidhttpactivtiy,
				ThinkAndroidHttpActivtiy.class);
		// ע��activity
		getTAApplication().registerActivity(
				R.string.thinkandroidsimpledwonloadactivtiy,
				ThinkAndroidSimpleDwonLoadActivtiy.class);
		// ע��activity
		getTAApplication().registerActivity(
				R.string.thinkandroidsimpletwodwonloadactivtiy,
				ThinkAndroidSimpleTwoDwonLoadActivtiy.class);
		// ע��activity
		getTAApplication().registerActivity(
				R.string.thinkandroiddwonloadactivtiy,
				ThinkAndroidDwonLoadActivtiy.class);
		// ע��activity
		getTAApplication().registerActivity(
				R.string.thinkandroidmultithreaddwonloadactivtiy,
				ThinkAndroidMultiThreadDwonLoadActivtiy.class);

		// ע��activity
		getTAApplication().registerActivity(R.string.thinkandroidotheractivtiy,
				ThinkAndroidOtherActivtiy.class);

	}

	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onAfterOnCreate(savedInstanceState);
	}

	private void startMain()
	{
		// TODO Auto-generated method stu
		doActivity(R.string.thinkandroidmainactivity);
	}

}
