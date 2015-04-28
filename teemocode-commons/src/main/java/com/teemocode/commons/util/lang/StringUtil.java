package com.teemocode.commons.util.lang;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.teemocode.commons.component.json.JSONDecoder;
import com.teemocode.commons.util.Constants;

public class StringUtil {
	public final static int SORT_ASCENDING = 1;

	public final static int SORT_DESCENDING = 2;

	public static String[] removeBlankElement(String[] arr) {
		if(arr == null) {
			return null;
		}
		List<String> list = new ArrayList<String>(Arrays.asList(arr));
		for(Iterator<String> iter = list.iterator(); iter.hasNext();) {
			String s = iter.next();
			if(StringUtils.isBlank(s)) {
				iter.remove();
			}
		}
		return list.toArray(new String[0]);
	}

	public static Long[] parseLongs(String text) {
		List<Long> ids = new ArrayList<Long>();
		if(text != null) {
			for(String sId : StringUtils.split(text, ",;|")) {
				if(StringUtils.isNotBlank(sId)) {
					ids.add(Long.parseLong(StringUtils.trim(sId)));
				}
			}
		}
		return ids.toArray(new Long[0]);
	}

	public static String join(List<Long> ids) {
		return StringUtils.join(ids, Constants.Separator1);
	}

	public static String join(Long[] ids) {
		return StringUtils.join(ids, Constants.Separator1);
	}

	public static Long[] parseIdsString(String text, Long... extIds) {
		return ArrayUtils.toObject(parseIdsString(text, ArrayUtils.toPrimitive(extIds)));
	}

	public static long[] parseIdsString(String text, long... extIds) {
		if(text != null && text.contains("*")) {
			return null;
		}
		return ArrayUtils.addAll(ArrayUtils.toPrimitive(parseLongs(text)), extIds);
	}

	public static Long parseId(Object id) {
		if(id == null) {
			return null;
		}
		if(id instanceof String && !StringUtils.isBlank((String) id)) {
			return Long.parseLong((String) id);
		} else if(id instanceof Number) {
			return ((Number) id).longValue();
		}
		return -1L;
	}

	/**
	 * 保留s1和s2都有值并删除s1没有的值。
	 * @see com.teemocode.commons.util.lang.huaxinsoft.util.StringUtilTest#testRetainAll()
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static String retainAll(String s1, String s2) {
		return retainAll(s1, s2, ",");
	}

	@SuppressWarnings("unchecked")
	public static String retainAll(String s1, String s2, String joinStr) {
		List<Long> ids = (List<Long>) CollectionUtils.retainAll(Arrays.asList(StringUtil.parseLongs(s2)),
				Arrays.asList(StringUtil.parseLongs(s1)));
		return StringUtils.join(ids, joinStr);
	}

	public static boolean match(String text, String regex) {
		regex = StringUtils.replace(regex, "\\", "\\\\");
		regex = StringUtils.replace(regex, ".", "\\.");
		regex = StringUtils.replace(regex, "[", "\\[");
		regex = StringUtils.replace(regex, "]", "\\]");
		regex = StringUtils.replace(regex, "(", "\\(");
		regex = StringUtils.replace(regex, ")", "\\)");

		regex = StringUtils.replace(regex, "?", ".+");
		regex = StringUtils.replace(regex, "*", ".*");
		return text.matches(regex);
	}

	public static String polishParamString(String param){
		String newParam = param;
		if(newParam == null){
			newParam = "";
		}
		newParam = newParam.trim();
		newParam = newParam.replaceAll("[ \t]*\\|", "|");
		newParam = newParam.replaceAll("\\|[ \t]*", "|");
		newParam = newParam.replaceAll("\\|\\|", "|");
		newParam = newParam.replaceAll("，", ",");
		newParam = newParam.replaceAll("；", ",");
		newParam = newParam.replaceAll("、", ",");
		newParam = newParam.replaceAll(";", ",");
		newParam = newParam.replaceAll("[ \t]*,", ",");
		newParam = newParam.replaceAll(",[ \t]*", ",");
		newParam = newParam.replaceAll(",,", ",");
		while(newParam.trim().endsWith("|") || newParam.trim().endsWith(",")){
			newParam = newParam.substring(0, newParam.length() -1);
		}
		return newParam;
	}

	public static String polishNosString(String nos){
		String newNos = polishParamString(nos);
		newNos = newNos.replaceAll(",", "|");
		newNos = polishParamString(newNos);
		if(!"".equals(newNos) && !"*".equals(newNos) && !newNos.endsWith("|")){
			newNos += "|";
		}
		if(!"".equals(newNos) && !"*".equals(newNos) && !newNos.startsWith("|")){
			newNos = "|"+newNos;
		}
		return newNos;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public static Object[] sortObjArray(Object[] array, String prop, int sortType) throws Exception {
		if(array == null || array.length <= 1) {
			return array;
		}
		Object[][] props = new String[array.length][2];
		if(sortType == StringUtil.SORT_ASCENDING) {
			for(int i = 0; i < array.length; i++) {
				Object obj = array[i];
				String prop1 = BeanUtils.getProperty(obj, prop);
				props[i][1] = prop1;
				props[i][0] = String.valueOf(i);
			}

			Arrays.sort(props, new Comparator() {
				@Override
				public int compare(Object ar1, Object ar2) {
					return ((String) ((Object[]) ar1)[1]).toUpperCase().compareTo(((String) ((Object[]) ar2)[1]).toUpperCase());
				}
			});

			List list = new ArrayList();
			for(Object[] prop2 : props) {
				int idx = Integer.valueOf(((String) prop2[0])).intValue();
				list.add(array[idx]);
			}

			return list.toArray();
		} else if(sortType == StringUtil.SORT_DESCENDING) {
			for(int i = 0; i < array.length; i++) {
				Object obj = array[i];
				String prop1 = BeanUtils.getProperty(obj, prop);
				props[i][1] = prop1;
				props[i][0] = String.valueOf(i);
			}

			Arrays.sort(props, new Comparator() {
				@Override
				public int compare(Object ar1, Object ar2) {

					return ((String) ((Object[]) ar2)[1]).toUpperCase().compareTo(((String) ((Object[]) ar1)[1]).toUpperCase());
				}
			});

			List list = new ArrayList();
			for(Object[] prop2 : props) {
				int idx = Integer.valueOf(((String) prop2[0])).intValue();
				list.add(array[idx]);
			}

			return list.toArray();
		} else {
			return array;
		}
	}

	public static String toHtmlString(String src, boolean noSingleQuotes) {
		StringCharacterIterator iter = new StringCharacterIterator(src);
		StringBuffer buf = new StringBuffer();
		for (char c = iter.first(); c != CharacterIterator.DONE; c = iter
				.next()) {
			switch (c) {
			case '\'':
				if (noSingleQuotes) {
					buf.append(c);
				} else {
					//buf.append("&apos;");
					buf.append("&#039;");
				}
				break;
			case '\"':
				buf.append("&quot;");
				break;
			case '<':
				buf.append("&lt;");
				break;
			case '>':
				buf.append("&gt;");
				break;
			case '&':
				buf.append("&amp;");
				break;
			default:
				buf.append(c);
			}
		}
		return buf.toString();
	}

	public static String clearHtml(String str) {
		Pattern p = Pattern.compile("<\\w{1,4}>");
		Matcher m = p.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, " ");
		}
		m.appendTail(sb);
		return sb.toString();
	}

	public static String escape(String str) {
		if (str == null) {
			return str;
		}
		StringBuffer ret = new StringBuffer();
		int len = str.length();
		for (int i = 0; i < len; i++) {
			char c = str.charAt(i);
			if ((c > 47 && c < 58) || //0-9
					(c > 64 && c < 91) || //A-Z
					(c > 96 && c < 123) || //a-z
					c == 95) {
				ret.append(c);
			} else if (c < 16) {
				ret.append("%0");
				ret.append(Integer.toString(c, 16));
			} else if (c >= 16 && c < 256) {
				ret.append('%');
				ret.append(Integer.toString(c, 16));
			} else if (c > 255 && c < 4096) {
				ret.append("%u0");
				ret.append(Integer.toString(c, 16));
			} else if (c > 4095 && c < 65536) {
				ret.append("%u");
				ret.append(Integer.toString(c, 16));
			}
		}
		return ret.toString();
	}

	public static String unescape(String str) {
		if (str == null) {
			return str;
		}

		String temp = str, sub;
		temp = "";
		char ch = ' ';
		int index = str.indexOf("%u");

		while (index >= 0) {
			temp += str.substring(0, index);
			sub = str.substring(index + 2, index + 6);
			str = str.substring(index + 6, str.length());

			try {
				ch = (char) Integer.parseInt(sub, 16);
				temp += new String(new char[] { ch });
			} catch (NumberFormatException e) {
				temp += sub;
			}
			index = str.indexOf("%u");
		}

		temp += str;
//		temp = URLDecoder.decode(temp);

		index = temp.indexOf("%");
		String temp1 = "";
		while (index >= 0) {
			temp1 += temp.substring(0, index);
			sub = temp.substring(index + 1, index + 3);
			temp = temp.substring(index + 3, temp.length());

			try {
				ch = (char) Integer.parseInt(sub, 16);
				temp1 += new String(new char[] { ch });
			} catch (NumberFormatException e) {
				temp1 += sub;
			}
			index = temp.indexOf("%");
		}

		temp1 += temp;
		return temp1;
	}

    public static String[] mergeArrays(String[] strArr1, String[] strArr2) {
    	return org.springframework.util.StringUtils.mergeStringArrays(strArr1, strArr2);
	}

	public static Object getObjFromJsonMap(String json, String key) {
		if(StringUtils.isBlank(json)) {
			return null;
		}
		Map<String, Object> map = JSONDecoder.decode(json);
		return map.get(key);
	}

	public static List<Object> getObjFromJsonArray(String json) {
		if(StringUtils.isBlank(json)) {
			return null;
		}
		List<Object> list = JSONDecoder.decode(json);
		return list;
	}

	/** 剪切文本。如果进行了剪切，则在文本后加上"..."
	 *
	 * @param s
	 *            剪切对象。
	 * @param len
	 *            编码小于256的作为一个字符，大于256的作为两个字符。
	 * @return
	 */
	public static String textCut(String s, int len, String append) {
		if (s == null) {
			return null;
		}
		int slen = s.length();
		if (slen <= len) {
			return s;
		}
		// 最大计数（如果全是英文）
		int maxCount = len * 2;
		int count = 0;
		int i = 0;
		for (; count < maxCount && i < slen; i++) {
			if (s.codePointAt(i) < 256) {
				count++;
			} else {
				count += 2;
			}
		}
		if (i < slen) {
			if (count > maxCount) {
				i--;
			}
			if (!StringUtils.isBlank(append)) {
				if (s.codePointAt(i - 1) < 256) {
					i -= 2;
				} else {
					i--;
				}
				return s.substring(0, i) + append;
			} else {
				return s.substring(0, i);
			}
		} else {
			return s;
		}
	}

	public static String fixNo(String no) {
		String s = StringUtils.isBlank(no) ? null : StringUtils.trim(no.toUpperCase());
		return StringUtils.isBlank(s) ? null : s;
	}

	public static String fixValue(String value) {
		String s = StringUtils.isBlank(value) ? null : StringUtils.trim(value);
		return StringUtils.isBlank(s) ? null : s;
	}

	/**
	 * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[] hexStr2ByteArr(String strIn)
	 * 互为可逆的转换过程
	 * @param arrB
	 *        需要转换的byte数组
	 * @return 转换后的字符串
	 * @throws Exception
	 */
	public static String byteArr2HexStr(byte[] arrB) throws Exception {
		int iLen = arrB.length;

		// 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
		StringBuffer sb = new StringBuffer(iLen * 2);

		for(int i = 0; i < iLen; i++) {
			int intTmp = arrB[i];

			// 把负数转换为正数
			while(intTmp < 0) {
				intTmp = intTmp + 256;
			}

			// 小于0F的数需要在前面补0
			if(intTmp < 16) {
				sb.append("0");
			}

			sb.append(Integer.toString(intTmp, 16));
		}
		return sb.toString();
	}

	/**
	 * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] arrB) 互为可逆的转换过程
	 * @param strIn
	 *        需要转换的字符串
	 * @return 转换后的byte数组
	 * @throws Exception
	 */
	public static byte[] hexStr2ByteArr(String strIn) throws Exception {
		byte[] arrB = strIn.getBytes();
		int iLen = arrB.length;

		// 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
		byte[] arrOut = new byte[iLen / 2];
		for(int i = 0; i < iLen; i = i + 2) {
			String strTmp = new String(arrB, i, 2);
			arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
		}
		return arrOut;
	}
}
