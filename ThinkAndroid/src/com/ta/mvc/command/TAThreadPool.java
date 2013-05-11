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
package com.ta.mvc.command;

import com.ta.util.TALogger;

/**
 * @Title ThreadPool
 * @package com.ta.mvc.command
 * @Description ThreadPool��command���̳߳�
 * @author ��è
 * @date 2013-1-16 ���� 16:51
 * @version V1.0
 */
public class TAThreadPool
{
	// �̵߳��������
	private static final int MAX_THREADS_COUNT = 2;
	private TACommandThread threads[] = null;
	private boolean started = false;
	private static TAThreadPool instance;

	private TAThreadPool()
	{

	}

	public static TAThreadPool getInstance()
	{
		if (instance == null)
		{
			instance = new TAThreadPool();
		}
		return instance;
	}

	public void start()
	{
		if (!started)
		{
			TALogger.i(TAThreadPool.this, "�̳߳ؿ�ʼ���У�");
			int threadCount = MAX_THREADS_COUNT;

			threads = new TACommandThread[threadCount];
			for (int threadId = 0; threadId < threadCount; threadId++)
			{
				threads[threadId] = new TACommandThread(threadId);
				threads[threadId].start();
			}
			started = true;
			TALogger.i(TAThreadPool.this, "�̳߳�������ɣ�");
		}
	}

	public void shutdown()
	{
		TALogger.i(TAThreadPool.this, "�ر������̣߳�");
		if (started)
		{
			for (TACommandThread thread : threads)
			{
				thread.stop();
			}
			threads = null;
			started = false;
		}
		TALogger.i(TAThreadPool.this, "�ر��������̣߳�");
	}
}
