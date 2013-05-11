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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Calendar;

/**
 * @Title CheckIDFormat
 * @Package com.ta.core.util.extend.string
 * @Description һ����� ������ݺ����Ƿ�Ϸ���ʽ
 * @author ��è
 * @date 2013-1-22 ���� 14:35
 * @version V1.0
 */
public class CheckIDFormat
{
	/**
	 * 1������Ľṹ ������ݺ�������������룬��ʮ��λ���ֱ������һλУ������ɡ�����˳�������������Ϊ����λ���ֵ�ַ�룬��λ���ֳ��������룬
	 * ��λ����˳�����һλ����У���롣
	 * 
	 * 2����ַ��(ǰ��λ���� ��ʾ�������ס����������(�С��졢��)�������������룬��GB/T2260�Ĺ涨ִ�С�
	 * 
	 * 3�����������루����λ��ʮ��λ�� ��ʾ�������������ꡢ�¡��գ���GB/T7408�Ĺ涨ִ�У��ꡢ�¡��մ���֮�䲻�÷ָ�����
	 * 
	 * 4��˳���루��ʮ��λ��ʮ��λ��
	 * ��ʾ��ͬһ��ַ������ʶ������Χ�ڣ���ͬ�ꡢͬ�¡�ͬ�ճ������˱ඨ��˳��ţ�˳�����������������ԣ�ż�������Ů�ԡ�
	 * 
	 * 5��У���루��ʮ��λ���� ��1��ʮ��λ���ֱ������Ȩ��͹�ʽ S = Sum(Ai * Wi), i = 0, , 16
	 * ���ȶ�ǰ17λ���ֵ�Ȩ��� Ai:��ʾ��iλ���ϵ����֤��������ֵ Wi:��ʾ��iλ���ϵļ�Ȩ���� Wi: 7 9 10 5 8 4 2 1 6
	 * 3 7 9 10 5 8 4 2 ��2������ģ Y = mod(S, 11) ��3��ͨ��ģ�õ���Ӧ��У���� Y: 0 1 2 3 4 5 6 7
	 * 8 9 10 У����: 1 0 X 9 8 7 6 5 4 3 2
	 * 
	 * �������ǾͿ��Դ���дһ��������У���Ƿ���ȷ�ˡ�
	 * */
	/**
	 * ======================================================================
	 * ���ܣ����֤����Ч��֤
	 * 
	 * @param IDStr
	 *            ���֤��
	 * @return ��Ч��true ��Ч��false
	 * @throws ParseException
	 */
	public boolean IDCardValidate(String IDStr) throws ParseException
	{
		String errorInfo = "";// ��¼������Ϣ
		String[] ValCodeArr =
		{ "1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2" };
		String[] Wi =
		{ "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10",
				"5", "8", "4", "2" };
		// String[] Checker = {"1","9","8","7","6","5","4","3","2","1","1"};
		String Ai = "";

		// ================ ����ĳ��� 15λ��18λ ================
		if (IDStr.length() != 15 && IDStr.length() != 18)
		{
			errorInfo = "���볤��Ӧ��Ϊ15λ��18λ��";
			System.out.println(errorInfo);
			return false;
		}
		// =======================(end)========================

		// ================ ���� �������Ϊ��Ϊ���� ================
		if (IDStr.length() == 18)
		{
			Ai = IDStr.substring(0, 17);
		} else if (IDStr.length() == 15)
		{
			Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
		}
		if (isNumeric(Ai) == false)
		{
			errorInfo = "15λ���붼ӦΪ���� ; 18λ��������һλ�⣬��ӦΪ���֡�";
			System.out.println(errorInfo);
			return false;
		}
		// =======================(end)========================

		// ================ ���������Ƿ���Ч ================
		String strYear = Ai.substring(6, 10);// ���
		String strMonth = Ai.substring(10, 12);// �·�
		String strDay = Ai.substring(12, 14);// �·�

		if (isDate(strYear + "-" + strMonth + "-" + strDay) == false)
		{
			errorInfo = "������Ч��";
			System.out.println(errorInfo);
			return false;
		}

		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
		if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
				|| (gc.getTime().getTime() - s.parse(
						strYear + "-" + strMonth + "-" + strDay).getTime()) < 0)
		{
			errorInfo = "���ղ�����Ч��Χ��";
			System.out.println(errorInfo);
			return false;
		}
		if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0)
		{
			errorInfo = "�·���Ч";
			System.out.println(errorInfo);
			return false;
		}
		if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0)
		{
			errorInfo = "������Ч";
			System.out.println(errorInfo);
			return false;
		}
		// =====================(end)=====================

		// ================ ������ʱ����Ч ================
		Hashtable<String, String> h = GetAreaCode();
		if (h.get(Ai.substring(0, 2)) == null)
		{
			errorInfo = "�����������";
			System.out.println(errorInfo);
			return false;
		}
		// ==============================================

		// ================ �ж����һλ��ֵ ================
		int TotalmulAiWi = 0;
		for (int i = 0; i < 17; i++)
		{
			TotalmulAiWi = TotalmulAiWi
					+ Integer.parseInt(String.valueOf(Ai.charAt(i)))
					* Integer.parseInt(Wi[i]);
		}
		int modValue = TotalmulAiWi % 11;
		String strVerifyCode = ValCodeArr[modValue];
		Ai = Ai + strVerifyCode;

		if (IDStr.length() == 18)
		{
			if (Ai.equals(IDStr) == false)
			{
				errorInfo = "���֤��Ч�����һλ��ĸ����";
				System.out.println(errorInfo);
				return false;
			}
		} else
		{
			System.out.println("���ڵ���:" + h.get(Ai.substring(0, 2).toString()));
			System.out.println("�����֤��:" + Ai);
			return true;
		}
		// =====================(end)=====================
		System.out.println("���ڵ���:" + h.get(Ai.substring(0, 2).toString()));
		return true;
	}

	/**
	 * ======================================================================
	 * ���ܣ����õ�������
	 * 
	 * @return Hashtable ����
	 */
	private Hashtable<String, String> GetAreaCode()
	{
		Hashtable<String, String> hashtable = new Hashtable<String, String>();
		hashtable.put("11", "����");
		hashtable.put("12", "���");
		hashtable.put("13", "�ӱ�");
		hashtable.put("14", "ɽ��");
		hashtable.put("15", "���ɹ�");
		hashtable.put("21", "����");
		hashtable.put("22", "����");
		hashtable.put("23", "������");
		hashtable.put("31", "�Ϻ�");
		hashtable.put("32", "����");
		hashtable.put("33", "�㽭");
		hashtable.put("34", "����");
		hashtable.put("35", "����");
		hashtable.put("36", "����");
		hashtable.put("37", "ɽ��");
		hashtable.put("41", "����");
		hashtable.put("42", "����");
		hashtable.put("43", "����");
		hashtable.put("44", "�㶫");
		hashtable.put("45", "����");
		hashtable.put("46", "����");
		hashtable.put("50", "����");
		hashtable.put("51", "�Ĵ�");
		hashtable.put("52", "����");
		hashtable.put("53", "����");
		hashtable.put("54", "����");
		hashtable.put("61", "����");
		hashtable.put("62", "����");
		hashtable.put("63", "�ຣ");
		hashtable.put("64", "����");
		hashtable.put("65", "�½�");
		hashtable.put("71", "̨��");
		hashtable.put("81", "���");
		hashtable.put("82", "����");
		hashtable.put("91", "����");
		return hashtable;
	}

	/**
	 * ======================================================================
	 * ���ܣ��ж��ַ����Ƿ�Ϊ����
	 * 
	 * @param str
	 * @return
	 */
	private boolean isNumeric(String str)
	{
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (isNum.matches())
		{
			return true;
		} else
		{
			return false;
		}
		/**//*
			 * �ж�һ���ַ�ʱ��Ϊ���� if(Character.isDigit(str.charAt(0))) { return true; }
			 * else { return false; }
			 */
	}

	/**
	 * ���ܣ��ж��ַ����Ƿ�Ϊ���ڸ�ʽ
	 * 
	 * @param str
	 * @return
	 */
	public boolean isDate(String strDate)
	{
		Pattern pattern = Pattern
				.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
		Matcher m = pattern.matcher(strDate);
		if (m.matches())
		{
			return true;
		} else
		{
			return false;
		}

	}

	/**
	 * ======================================================================
	 * =����:���ж��Ѿ�����ȷ�����֤����֮��,���ҳ����֤���ڵ���
	 * 
	 * @param idCard
	 *            ���֤����
	 * 
	 * @return ���ڵ���
	 */
	public String GetArea(String idCard)
	{
		Hashtable<String, String> ht = GetAreaCode();
		String area = ht.get(idCard.substring(0, 2));
		return area;
	}

	/**
	 * ======================================================================
	 * =����:���ж��Ѿ�����ȷ�����֤����֮��,���ҳ������Ա�
	 * 
	 * @param idCard
	 *            ���֤����
	 * 
	 * @return �л���Ů
	 */
	public String GetSex(String idCard)
	{
		String sex = "";
		if (idCard.length() == 15)
			sex = idCard.substring(idCard.length() - 3, idCard.length());

		if (idCard.length() == 18)
			sex = idCard.substring(idCard.length() - 4, idCard.length() - 1);

		System.out.println(sex);
		int sexNum = Integer.parseInt(sex) % 2;
		if (sexNum == 0)
		{
			return "Ů";
		}
		return "��";
	}

	/**
	 * ======================================================================
	 * =����:���ж��Ѿ�����ȷ�����֤����֮��,���ҳ����˳�������
	 * 
	 * @param idCard
	 *            ���֤����
	 * 
	 * @return �������� XXXX MM-DD
	 */

	public String GetBirthday(String idCard)
	{
		String Ain = "";
		if (idCard.length() == 18)
		{
			Ain = idCard.substring(0, 17);
		} else if (idCard.length() == 15)
		{
			Ain = idCard.substring(0, 6) + "19" + idCard.substring(6, 15);
		}

		// ================ ���������Ƿ���Ч ================
		String strYear = Ain.substring(6, 10);// ���
		String strMonth = Ain.substring(10, 12);// �·�
		String strDay = Ain.substring(12, 14);// ����
		return strYear + "-" + strMonth + "-" + strDay;
	}

}
