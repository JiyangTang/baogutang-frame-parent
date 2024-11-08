/*
 *      Copyright (c) 2018-2028, DreamLu All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: DreamLu 卢春梦 (596392912@qq.com)
 */
package com.baogutang.frame.common.utils;


import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 数字类型工具类
 *
 */
public class NumberUtil extends org.springframework.util.NumberUtils {

	//-----------------------------------------------------------------------

	/**
	 * <p>Convert a <code>String</code> to an <code>int</code>, returning
	 * <code>zero</code> if the conversion fails.</p>
	 *
	 * <p>If the string is <code>null</code>, <code>zero</code> is returned.</p>
	 *
	 * <pre>
	 *   NumberUtil.toInt(null) = 0
	 *   NumberUtil.toInt("")   = 0
	 *   NumberUtil.toInt("1")  = 1
	 * </pre>
	 *
	 * @param str the string to convert, may be null
	 * @return the int represented by the string, or <code>zero</code> if
	 * conversion fails
	 */
	public static int toInt(final String str) {
		return toInt(str, -1);
	}

	/**
	 * <p>Convert a <code>String</code> to an <code>int</code>, returning a
	 * default value if the conversion fails.</p>
	 *
	 * <p>If the string is <code>null</code>, the default value is returned.</p>
	 *
	 * <pre>
	 *   NumberUtil.toInt(null, 1) = 1
	 *   NumberUtil.toInt("", 1)   = 1
	 *   NumberUtil.toInt("1", 0)  = 1
	 * </pre>
	 *
	 * @param str          the string to convert, may be null
	 * @param defaultValue the default value
	 * @return the int represented by the string, or the default if conversion fails
	 */
	public static int toInt(@Nullable final String str, final int defaultValue) {
		if (str == null) {
			return defaultValue;
		}
		try {
			return Integer.valueOf(str);
		} catch (final NumberFormatException nfe) {
			return defaultValue;
		}
	}

	/**
	 * <p>Convert a <code>String</code> to a <code>long</code>, returning
	 * <code>zero</code> if the conversion fails.</p>
	 *
	 * <p>If the string is <code>null</code>, <code>zero</code> is returned.</p>
	 *
	 * <pre>
	 *   NumberUtil.toLong(null) = 0L
	 *   NumberUtil.toLong("")   = 0L
	 *   NumberUtil.toLong("1")  = 1L
	 * </pre>
	 *
	 * @param str the string to convert, may be null
	 * @return the long represented by the string, or <code>0</code> if
	 * conversion fails
	 */
	public static long toLong(final String str) {
		return toLong(str, 0L);
	}

	/**
	 * <p>Convert a <code>String</code> to a <code>long</code>, returning a
	 * default value if the conversion fails.</p>
	 *
	 * <p>If the string is <code>null</code>, the default value is returned.</p>
	 *
	 * <pre>
	 *   NumberUtil.toLong(null, 1L) = 1L
	 *   NumberUtil.toLong("", 1L)   = 1L
	 *   NumberUtil.toLong("1", 0L)  = 1L
	 * </pre>
	 *
	 * @param str          the string to convert, may be null
	 * @param defaultValue the default value
	 * @return the long represented by the string, or the default if conversion fails
	 */
	public static long toLong(@Nullable final String str, final long defaultValue) {
		if (str == null) {
			return defaultValue;
		}
		try {
			return Long.valueOf(str);
		} catch (final NumberFormatException nfe) {
			return defaultValue;
		}
	}

	/**
	 * <p>Convert a <code>String</code> to a <code>Double</code>
	 *
	 * @param value value
	 * @return double value
	 */
	public static Double toDouble(String value) {
		return toDouble(value, null);
	}

	/**
	 * <p>Convert a <code>String</code> to a <code>Double</code>
	 *
	 * @param value value
	 * @param defaultValue 默认值
	 * @return double value
	 */
	public static Double toDouble(@Nullable String value, Double defaultValue) {
		if (value != null) {
			return Double.valueOf(value.trim());
		}
		return defaultValue;
	}

	/**
	 * <p>Convert a <code>String</code> to a <code>Double</code>
	 *
	 * @param value value
	 * @return double value
	 */
	public static Float toFloat(String value) {
		return toFloat(value, null);
	}

	/**
	 * <p>Convert a <code>String</code> to a <code>Double</code>
	 *
	 * @param value value
	 * @param defaultValue 默认值
	 * @return double value
	 */
	public static Float toFloat(@Nullable String value, Float defaultValue) {
		if (value != null) {
			return Float.valueOf(value.trim());
		}
		return defaultValue;
	}

	/**
	 * All possible chars for representing a number as a String
	 */
	private final static char[] DIGITS = {
		'0', '1', '2', '3', '4', '5',
		'6', '7', '8', '9', 'a', 'b',
		'c', 'd', 'e', 'f', 'g', 'h',
		'i', 'j', 'k', 'l', 'm', 'n',
		'o', 'p', 'q', 'r', 's', 't',
		'u', 'v', 'w', 'x', 'y', 'z',
		'A', 'B', 'C', 'D', 'E', 'F',
		'G', 'H', 'I', 'J', 'K', 'L',
		'M', 'N', 'O', 'P', 'Q', 'R',
		'S', 'T', 'U', 'V', 'W', 'X',
		'Y', 'Z'
	};

	/**
	 * 将 long 转短字符串 为 62 进制
	 *
	 * @param i 数字
	 * @return 短字符串
	 */
	public static String to62String(long i) {
		int radix = DIGITS.length;
		char[] buf = new char[65];
		int charPos = 64;
		i = -i;
		while (i <= -radix) {
			buf[charPos--] = DIGITS[(int) (-(i % radix))];
			i = i / radix;
		}
		buf[charPos] = DIGITS[(int) (-i)];

		return new String(buf, charPos, (65 - charPos));
	}

	/**
	 * 单位进位，中文默认为4位即（万、亿）
	 */
	private static final int UNIT_STEP = 4;
	/**
	 * 单位
	 */
	private static final String[] CN_UNITS = new String[]{
			"个", "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千", "万"
	};
	/**
	 * 汉字
	 */
	private static final String[] CN_CHARS = new String[]{
			"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"
	};

	/**
	 * 对于int, long类型的数据处理
	 * @param num 需要转换的数值
	 * @param isColloquial 是否口语化。例如12转换为'十二'而不是'一十二'。
	 * @return
	 */
	public static String toUpperCase(int num, boolean isColloquial) {
		String[] result = convert(num, isColloquial);
		StringBuilder builder = new StringBuilder(32);
		for (String str : result) {
			builder.append(str);
		}
		return builder.toString();
	}

	/**
	 * 将数值转换为中文
	 * @param num 需要转换的数值
	 * @param isColloquial 是否口语化。例如12转换为'十二'而不是'一十二'。
	 * @return 数据
	 */
	private static String[] convert(int num, boolean isColloquial) {
		// 10以下直接返回对应汉字
		if (num < 10) {
			// ASCII2int
			return new String[]{CN_CHARS[(int) num]};
		}

		char[] chars = String.valueOf(num).toCharArray();
		// 超过单位表示范围的返回空
		if (chars.length > CN_UNITS.length) {
			return new String[]{};
		}

		// 记录上次单位进位
		boolean isLastUnitStep = false;
		// 创建数组，将数字填入单位对应的位置
		List<String> cnchars = new ArrayList<>(chars.length * 2);
		// 从低位向高位循环
		for (int pos = chars.length - 1; pos >= 0; pos--) {
			char ch = chars[pos];
			// ascii2int 汉字
			String cnChar = CN_CHARS[ch - '0'];
			// 对应的单位坐标
			int unitPos = chars.length - pos - 1;
			// 单位
			String cnUnit = CN_UNITS[unitPos];
			// 是否为0
			boolean isZero = (ch == '0');
			// 是否低位为0
			boolean isZeroLow = (pos + 1 < chars.length && chars[pos + 1] == '0');
			// 当前位是否需要单位进位
			boolean isUnitStep = (unitPos >= UNIT_STEP && (unitPos % UNIT_STEP == 0));
			// 去除相邻的上一个单位进位
			if (isUnitStep && isLastUnitStep) {
				int size = cnchars.size();
				cnchars.remove(size - 1);
				// 补0
				if (!CN_CHARS[0].equals(cnchars.get(size - 2))) {
					cnchars.add(CN_CHARS[0]);
				}
			}

			// 单位进位(万、亿)，或者非0时加上单位
			if (isUnitStep || !isZero) {
				cnchars.add(cnUnit);
				isLastUnitStep = isUnitStep;
			}
			// 当前位为0低位为0，或者当前位为0并且为单位进位时进行省略
			if (isZero && (isZeroLow || isUnitStep)) {
				continue;
			}
			cnchars.add(cnChar);
			isLastUnitStep = false;
		}

		Collections.reverse(cnchars);
		// 清除最后一位的0
		int chSize = cnchars.size();
		String chEnd = cnchars.get(chSize - 1);
		if (CN_CHARS[0].equals(chEnd) || CN_UNITS[0].equals(chEnd)) {
			cnchars.remove(chSize - 1);
		}

		// 口语化处理
		if (isColloquial) {
			String chFirst = cnchars.get(0);
			String chSecond = cnchars.get(1);
			// 是否以'一'开头，紧跟'十'
			if (chFirst.equals(CN_CHARS[1]) && chSecond.startsWith(CN_UNITS[1])) {
				cnchars.remove(0);
			}
		}
		return cnchars.toArray(new String[]{});
	}
}
