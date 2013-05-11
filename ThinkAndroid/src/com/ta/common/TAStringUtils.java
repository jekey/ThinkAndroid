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
package com.ta.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.util.Log;

/**
 * �ַ����������߰�
 * 
 * @author ��è
 * @version 1.0
 * @created 2012-3-21
 */
public class TAStringUtils
{
	private final static Pattern emailer = Pattern
			.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>()
	{
		@Override
		protected SimpleDateFormat initialValue()
		{
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	/**
	 * �ַ�����ȡ
	 * 
	 * @param str
	 * @param length
	 * @return
	 * @throws Exception
	 */
	public static String subString(String str, int length) throws Exception
	{

		byte[] bytes = str.getBytes("Unicode");
		int n = 0; // ��ʾ��ǰ���ֽ���
		int i = 2; // Ҫ��ȡ���ֽ������ӵ�3���ֽڿ�ʼ
		for (; i < bytes.length && n < length; i++)
		{
			// ����λ�ã���3��5��7�ȣ�ΪUCS2�����������ֽڵĵڶ����ֽ�
			if (i % 2 == 1)
			{
				n++; // ��UCS2�ڶ����ֽ�ʱn��1
			} else
			{
				// ��UCS2����ĵ�һ���ֽڲ�����0ʱ����UCS2�ַ�Ϊ���֣�һ�������������ֽ�
				if (bytes[i] != 0)
				{
					n++;
				}
			}
		}
		// ���iΪ����ʱ�������ż��
		if (i % 2 == 1)
		{
			// ��UCS2�ַ��Ǻ���ʱ��ȥ�������һ��ĺ���
			if (bytes[i - 1] != 0)
				i = i - 1;
			// ��UCS2�ַ�����ĸ�����֣��������ַ�
			else
				i = i + 1;
		}
		return new String(bytes, 0, i, "Unicode");
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	public static String toDBC(String input)
	{
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++)
		{
			if (c[i] == 12288)
			{
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	/**
	 * ����΢�����ݵĳ��� 1������ == ����Ӣ����ĸ��ռ�ĳ��� ����������Ӣ�ĺ�����
	 * 
	 * @param c
	 *            ��Ҫͳ�Ƶ��ַ�����
	 * @return �����ַ����м���ĳ���
	 */
	public static long calculateWeiboLength(CharSequence c)
	{

		double len = 0;
		for (int i = 0; i < c.length(); i++)
		{
			int temp = (int) c.charAt(i);
			if (temp > 0 && temp < 127)
			{
				len += 0.5;
			} else
			{
				len++;
			}
		}
		return Math.round(len);
	}

	/**
	 * �ָ��ַ���
	 * 
	 * @param str
	 *            String ԭʼ�ַ���
	 * @param splitsign
	 *            String �ָ���
	 * @return String[] �ָ����ַ�������
	 */
	public static String[] split(String str, String splitsign)
	{
		int index;
		if (str == null || splitsign == null)
			return null;
		ArrayList<String> al = new ArrayList<String>();
		while ((index = str.indexOf(splitsign)) != -1)
		{
			al.add(str.substring(0, index));
			str = str.substring(index + splitsign.length());
		}
		al.add(str);
		return (String[]) al.toArray(new String[0]);
	}

	/**
	 * �滻�ַ���
	 * 
	 * @param from
	 *            String ԭʼ�ַ���
	 * @param to
	 *            String Ŀ���ַ���
	 * @param source
	 *            String ĸ�ַ���
	 * @return String �滻����ַ���
	 */
	public static String replace(String from, String to, String source)
	{
		if (source == null || from == null || to == null)
			return null;
		StringBuffer bf = new StringBuffer("");
		int index = -1;
		while ((index = source.indexOf(from)) != -1)
		{
			bf.append(source.substring(0, index) + to);
			source = source.substring(index + from.length());
			index = source.indexOf(from);
		}
		bf.append(source);
		return bf.toString();
	}

	/**
	 * �滻�ַ��������ܹ���HTMLҳ����ֱ����ʾ(�滻˫���ź�С�ں�)
	 * 
	 * @param str
	 *            String ԭʼ�ַ���
	 * @return String �滻����ַ���
	 */
	public static String htmlencode(String str)
	{
		if (str == null)
		{
			return null;
		}

		return replace("\"", "&quot;", replace("<", "&lt;", str));
	}

	/**
	 * �滻�ַ��������������ת����ԭʼ�루�滻��˫���ź�С�ںţ�
	 * 
	 * @param str
	 *            String
	 * @return String
	 */
	public static String htmldecode(String str)
	{
		if (str == null)
		{
			return null;
		}

		return replace("&quot;", "\"", replace("&lt;", "<", str));
	}

	private static final String _BR = "<br/>";

	/**
	 * ��ҳ����ֱ����ʾ�ı����ݣ��滻С�ںţ��ո񣬻س���TAB
	 * 
	 * @param str
	 *            String ԭʼ�ַ���
	 * @return String �滻����ַ���
	 */
	public static String htmlshow(String str)
	{
		if (str == null)
		{
			return null;
		}

		str = replace("<", "&lt;", str);
		str = replace(" ", "&nbsp;", str);
		str = replace("\r\n", _BR, str);
		str = replace("\n", _BR, str);
		str = replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;", str);
		return str;
	}

	/**
	 * ����ָ���ֽڳ��ȵ��ַ���
	 * 
	 * @param str
	 *            String �ַ���
	 * @param length
	 *            int ָ������
	 * @return String ���ص��ַ���
	 */
	public static String toLength(String str, int length)
	{
		if (str == null)
		{
			return null;
		}
		if (length <= 0)
		{
			return "";
		}
		try
		{
			if (str.getBytes("GBK").length <= length)
			{
				return str;
			}
		} catch (Exception ex)
		{
		}
		StringBuffer buff = new StringBuffer();

		int index = 0;
		char c;
		length -= 3;
		while (length > 0)
		{
			c = str.charAt(index);
			if (c < 128)
			{
				length--;
			} else
			{
				length--;
				length--;
			}
			buff.append(c);
			index++;
		}
		buff.append("...");
		return buff.toString();
	}

	/**
	 * ��ȡurl�ĺ�׺��
	 * 
	 * @param str
	 * @return
	 */
	public static String getUrlFileName(String urlString)
	{
		String fileName = urlString.substring(urlString.lastIndexOf("/"));
		fileName = fileName.substring(1, fileName.length());
		if (fileName.equalsIgnoreCase(""))
		{
			Calendar c = Calendar.getInstance();
			fileName = c.get(Calendar.YEAR) + "" + c.get(Calendar.MONTH) + ""
					+ c.get(Calendar.DAY_OF_MONTH) + ""
					+ c.get(Calendar.MINUTE);

		}
		return fileName;
	}

	public static String replaceSomeString(String str)
	{
		String dest = "";
		try
		{
			if (str != null)
			{
				str = str.replaceAll("\r", "");
				str = str.replaceAll("&gt;", ">");
				str = str.replaceAll("&ldquo;", "��");
				str = str.replaceAll("&rdquo;", "��");
				str = str.replaceAll("&#39;", "'");
				str = str.replaceAll("&nbsp;", "");
				str = str.replaceAll("<br\\s*/>", "\n");
				str = str.replaceAll("&quot;", "\"");
				str = str.replaceAll("&lt;", "<");
				str = str.replaceAll("&lsquo;", "��");
				str = str.replaceAll("&rsquo;", "��");
				str = str.replaceAll("&middot;", "��");
				str = str.replace("&mdash;", "��");
				str = str.replace("&hellip;", "��");
				str = str.replace("&amp;", "��");
				str = str.replaceAll("\\s*", "");
				str = str.trim();
				str = str.replaceAll("<p>", "\n      ");
				str = str.replaceAll("</p>", "");
				str = str.replaceAll("<div.*?>", "\n      ");
				str = str.replaceAll("</div>", "");
				dest = str;
			}
		} catch (Exception e)
		{
			// TODO: handle exception
		}

		return dest;
	}

	/**
	 * ����ı������HTML��ǩ
	 * 
	 * @param htmlStr
	 * @return
	 */
	public static String delHTMLTag(String htmlStr)
	{
		String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // ����script��������ʽ
		String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // ����style��������ʽ
		String regEx_html = "<[^>]+>"; // ����HTML��ǩ��������ʽ
		Log.v("htmlStr", htmlStr);
		try
		{
			Pattern p_script = Pattern.compile(regEx_script,
					Pattern.CASE_INSENSITIVE);
			Matcher m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // ����script��ǩ

			Pattern p_style = Pattern.compile(regEx_style,
					Pattern.CASE_INSENSITIVE);
			Matcher m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // ����style��ǩ

			Pattern p_html = Pattern.compile(regEx_html,
					Pattern.CASE_INSENSITIVE);
			Matcher m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // ����html��ǩ
		} catch (Exception e)
		{
			// TODO: handle exception
		}

		return htmlStr; // �����ı��ַ���
	}

	public static String delSpace(String str)
	{
		if (str != null)
		{
			str = str.replaceAll("\r", "");
			str = str.replaceAll("\n", "");
			str = str.replace(" ", "");
		}
		return str;
	}

	/**
	 * ����ַ����Ƿ����ֵ�����Ϊtrue,
	 * 
	 * @param str
	 *            ��������ַ���
	 * @return �� str ��Ϊ null �� "" �ͷ��� true
	 */
	public static boolean isNotNull(String str)
	{
		return (str != null && !"".equalsIgnoreCase(str.trim()));
	}

	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>()
	{
		@Override
		protected SimpleDateFormat initialValue()
		{
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};

	/**
	 * ���ַ���תλ��������
	 * 
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate)
	{
		try
		{
			return dateFormater.get().parse(sdate);
		} catch (ParseException e)
		{
			return null;
		}
	}

	/**
	 * ���Ѻõķ�ʽ��ʾʱ��
	 * 
	 * @param sdate
	 * @return
	 */
	public static String friendly_time(String sdate)
	{
		Date time = toDate(sdate);
		if (time == null)
		{
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();

		// �ж��Ƿ���ͬһ��
		String curDate = dateFormater2.get().format(cal.getTime());
		String paramDate = dateFormater2.get().format(time);
		if (curDate.equals(paramDate))
		{
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "����ǰ";
			else
				ftime = hour + "Сʱǰ";
			return ftime;
		}

		long lt = time.getTime() / 86400000;
		long ct = cal.getTimeInMillis() / 86400000;
		int days = (int) (ct - lt);
		if (days == 0)
		{
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "����ǰ";
			else
				ftime = hour + "Сʱǰ";
		} else if (days == 1)
		{
			ftime = "����";
		} else if (days == 2)
		{
			ftime = "ǰ��";
		} else if (days > 2 && days <= 10)
		{
			ftime = days + "��ǰ";
		} else if (days > 10)
		{
			ftime = dateFormater2.get().format(time);
		}
		return ftime;
	}

	public static String trimmy(String str)
	{
		String dest = "";
		if (str != null)
		{
			str = str.replaceAll("-", "");
			str = str.replaceAll("\\+", "");
			dest = str;
		}
		return dest;
	}

	public static String replaceBlank(String str)
	{

		String dest = "";
		if (str != null)
		{
			Pattern p = Pattern.compile("\r");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	/**
	 * �жϸ����ַ���ʱ���Ƿ�Ϊ����
	 * 
	 * @param sdate
	 * @return boolean
	 */
	public static boolean isToday(String sdate)
	{
		boolean b = false;
		Date time = toDate(sdate);
		Date today = new Date();
		if (time != null)
		{
			String nowDate = dateFormater2.get().format(today);
			String timeDate = dateFormater2.get().format(time);
			if (nowDate.equals(timeDate))
			{
				b = true;
			}
		}
		return b;
	}

	/**
	 * �жϸ����ַ����Ƿ�հ״��� �հ״���ָ�ɿո��Ʊ�����س��������з���ɵ��ַ��� �������ַ���Ϊnull����ַ���������true
	 * 
	 * @param input
	 * @return boolean �������ַ���Ϊnull����ַ���������true
	 */
	public static boolean isEmpty(String input)
	{
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++)
		{
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n')
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * �ж��ǲ���һ���Ϸ��ĵ����ʼ���ַ
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email)
	{
		if (email == null || email.trim().length() == 0)
			return false;
		return emailer.matcher(email).matches();
	}

	/**
	 * �ַ���ת����
	 * 
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static int toInt(String str, int defValue)
	{
		try
		{
			return Integer.parseInt(str);
		} catch (Exception e)
		{
		}
		return defValue;
	}

	/**
	 * ����ת����
	 * 
	 * @param obj
	 * @return ת���쳣���� 0
	 */
	public static int toInt(Object obj)
	{
		if (obj == null)
			return 0;
		return toInt(obj.toString(), 0);
	}

	/**
	 * ����ת����
	 * 
	 * @param obj
	 * @return ת���쳣���� 0
	 */
	public static long toLong(String obj)
	{
		try
		{
			return Long.parseLong(obj);
		} catch (Exception e)
		{
		}
		return 0;
	}

	/**
	 * �ַ���ת����ֵ
	 * 
	 * @param b
	 * @return ת���쳣���� false
	 */
	public static boolean toBool(String b)
	{
		try
		{
			return Boolean.parseBoolean(b);
		} catch (Exception e)
		{
		}
		return false;
	}

	/**
	 * �ж��ǲ��ǺϷ��ֻ� handset �ֻ�����
	 */
	public static boolean isHandset(String handset)
	{
		try
		{
			if (!handset.substring(0, 1).equals("1"))
			{
				return false;
			}
			if (handset == null || handset.length() != 11)
			{
				return false;
			}
			String check = "^[0123456789]+$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(handset);
			boolean isMatched = matcher.matches();
			if (isMatched)
			{
				return true;
			} else
			{
				return false;
			}
		} catch (RuntimeException e)
		{
			return false;
		}
	}

	/**
	 * �ж�������ַ����Ƿ�Ϊ������
	 * 
	 * @param str
	 *            ������ַ���
	 * @return ����Ǵ����ַ���true,���򷵻�false
	 */
	public static boolean isChinese(String str)
	{
		Pattern pattern = Pattern.compile("[\u0391-\uFFE5]+$");
		return pattern.matcher(str).matches();
	}

	/**
	 * �ж��Ƿ�Ϊ����
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str)
	{
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches())
		{
			return false;
		}
		return true;
	}

	/**
	 * �ж��Ƿ�Ϊ����
	 * 
	 * @param str
	 *            ������ַ���
	 * @return ����������true,���򷵻�false
	 */
	public static boolean isInteger(String str)
	{
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

	/**
	 * �ж��Ƿ�Ϊ������������double��float
	 * 
	 * @param str
	 *            ������ַ���
	 * @return �Ǹ���������true,���򷵻�false
	 */
	public static boolean isDouble(String str)
	{
		Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
		return pattern.matcher(str).matches();
	}

	/**
	 * �Ƿ�Ϊ�հ�,����null��""
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str)
	{
		return str == null || str.trim().length() == 0;
	}

	/**
	 * �ж��Ƿ���ָ�����ȵ��ַ���
	 * 
	 * @param text
	 *            �ַ���
	 * @param lenght
	 *            �Զ��ĳ���
	 * @return
	 */
	public static boolean isLenghtStrLentht(String text, int lenght)
	{
		if (text.length() <= lenght)
			return true;
		else
			return false;
	}

	/**
	 * �Ƿ��Ƕ��ŵĳ���
	 * 
	 * @param text
	 * @return
	 */
	public static boolean isSMSStrLentht(String text)
	{
		if (text.length() <= 70)
			return true;
		else
			return false;
	}

	/**
	 * �ж��ֻ������Ƿ���ȷ
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public static boolean isPhoneNumberValid(String phoneNumber)
	{
		phoneNumber = trimmy(phoneNumber);
		MobileFormat mobile = new MobileFormat(phoneNumber);
		return mobile.isLawful();
	}

	// �ж��Ƿ�Ϊurl
	public static boolean checkEmail(String email)
	{

		Pattern pattern = Pattern
				.compile("^\\w+([-.]\\w+)*@\\w+([-]\\w+)*\\.(\\w+([-]\\w+)*\\.)*[a-z]{2,3}$");
		Matcher matcher = pattern.matcher(email);
		if (matcher.matches())
		{
			return true;
		}
		return false;
	}

	// �ж�΢�������Ƿ�Ϊ�Ƿ�Ϊ120��
	public static boolean isShareStrLentht(String text, int lenght)
	{
		if (text.length() <= 120)
			return true;
		else
			return false;
	}

	public static String getFileNameFromUrl(String url)
	{

		// ���ֲ���ֻ�����
		// ͨ�� ������ �� ��/�� �ж��ļ���
		String extName = "";
		String filename;
		int index = url.lastIndexOf('?');
		if (index > 1)
		{
			extName = url.substring(url.lastIndexOf('.') + 1, index);
		} else
		{
			extName = url.substring(url.lastIndexOf('.') + 1);
		}
		filename = hashKeyForDisk(url) + "." + extName;
		return filename;
		/*
		 * int index = url.lastIndexOf('?'); String filename; if (index > 1) {
		 * filename = url.substring(url.lastIndexOf('/') + 1, index); } else {
		 * filename = url.substring(url.lastIndexOf('/') + 1); }
		 * 
		 * if (filename == null || "".equals(filename.trim())) {// �����ȡ�����ļ�����
		 * filename = UUID.randomUUID() + ".apk";// Ĭ��ȡһ���ļ��� } return filename;
		 */
	}

	/**
	 * һ��ɢ�з���,�ı�һ���ַ���(��URL)��һ��ɢ���ʺ�ʹ����Ϊһ�������ļ�����
	 */
	public static String hashKeyForDisk(String key)
	{
		String cacheKey;
		try
		{
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(key.getBytes());
			cacheKey = bytesToHexString(mDigest.digest());
		} catch (NoSuchAlgorithmException e)
		{
			cacheKey = String.valueOf(key.hashCode());
		}
		return cacheKey;
	}

	private static String bytesToHexString(byte[] bytes)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++)
		{
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1)
			{
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}

}
