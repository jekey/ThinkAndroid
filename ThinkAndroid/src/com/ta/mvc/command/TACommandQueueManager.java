package com.ta.mvc.command;

import com.ta.util.TALogger;

/**
 * @Title TACommandQueueManager
 * @package com.ta.mvc.command
 * @Description TACommandQueueManager��command���еĹ�����
 * @author ��è
 * @date 2013-1-16 ���� 17:51
 * @version V1.0
 */
public final class TACommandQueueManager
{
	private static TACommandQueueManager instance;
	private boolean initialized = false;
	private TAThreadPool pool;
	private TACommandQueue queue;

	private TACommandQueueManager()
	{
	}

	public static TACommandQueueManager getInstance()
	{
		if (instance == null)
		{
			instance = new TACommandQueueManager();
		}
		return instance;
	}

	public void initialize()
	{
		TALogger.i(TACommandQueueManager.this, "׼����ʼ����");
		if (!initialized)
		{
			TALogger.i(TACommandQueueManager.this, "���ڳ�ʼ����");
			queue = new TACommandQueue();
			pool = TAThreadPool.getInstance();
			TALogger.i(TACommandQueueManager.this, "��ɳ�ʼ����");

			pool.start();
			initialized = true;
		}
		TALogger.i(TACommandQueueManager.this, "��ʼ����ɣ�");
	}

	/**
	 * �Ӷ����л�ȡCommand
	 * 
	 * @return TAICommand
	 */
	public TAICommand getNextCommand()
	{
		TALogger.i(TACommandQueueManager.this, "��ȡCommand��");
		TAICommand cmd = queue.getNextCommand();
		TALogger.i(TACommandQueueManager.this, "��ȡCommand" + cmd + "��ɣ�");
		return cmd;
	}

	/**
	 * ���Command��������
	 */
	public void enqueue(TAICommand cmd)
	{
		TALogger.i(TACommandQueueManager.this, "���" + cmd + "��ʼ");
		queue.enqueue(cmd);
		TALogger.i(TACommandQueueManager.this, "���" + cmd + "���");
	}

	/**
	 * �������
	 */
	public void clear()
	{
		queue.clear();
	}

	/**
	 * �رն���
	 */
	public void shutdown()
	{
		if (initialized)
		{
			queue.clear();
			pool.shutdown();
			initialized = false;
		}
	}
}
