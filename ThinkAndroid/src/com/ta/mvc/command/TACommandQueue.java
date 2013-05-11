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

import java.util.concurrent.LinkedBlockingQueue;

import com.ta.util.TALogger;

/**
 * @Title TACommandQueue
 * @package com.ta.core.mvc.command
 * @Description TACommandQueueά��һ��Command
 * @author ��è
 * @date 2013-1-16 ���� 16:51
 * @version V1.0
 */
public class TACommandQueue
{
	private LinkedBlockingQueue<TAICommand> theQueue = new LinkedBlockingQueue<TAICommand>();

	public TACommandQueue()
	{
		TALogger.i(TACommandQueue.this, "��ʼ��Command����");
	}

	public void enqueue(TAICommand cmd)
	{
		TALogger.i(TACommandQueue.this, "���Command������");
		theQueue.add(cmd);
	}

	public synchronized TAICommand getNextCommand()
	{
		TALogger.i(TACommandQueue.this, "��ȡCommand");
		TAICommand cmd = null;
		try
		{
			TALogger.i(TACommandQueue.this, "CommandQueue::to-take");
			cmd = theQueue.take();
			TALogger.i(TACommandQueue.this, "CommandQueue::taken");
		} catch (InterruptedException e)
		{
			TALogger.i(TACommandQueue.this, "û�л�ȡ��Command");
			e.printStackTrace();
		}
		TALogger.i(TACommandQueue.this, "����Command" + cmd);
		return cmd;
	}

	public synchronized void clear()
	{
		TALogger.i(TACommandQueue.this, "�������Command");
		theQueue.clear();
	}
}
