package com.fb.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

public class CommonUtil {

	protected static Logger logger = Logger.getLogger(CommonUtil.class);

	public static String convertListToString(String[] list, String token, boolean sql) {
		return convertListToString(Arrays.asList(list), token, sql);
	}
	
	@SuppressWarnings("unchecked")
	public static String convertListToString(List list, String token, boolean sql) {
		String quotation = "";
		if (sql == true) quotation = "'";
		
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			if (i > 0) {
				result.append(token);
			}
			result.append(quotation);
			result.append(list.get(i));
			result.append(quotation);
		}
		return result.toString();
	}

	public static List<String> convertStringToList(String str, String token) {
		List<String> result = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(str, token);
		while (st.hasMoreTokens()) {
			result.add(st.nextToken());
		}
		return result;
	}
	
	public static double round(double d) {
		BigDecimal value = BigDecimal.valueOf(d);
		BigDecimal one = new BigDecimal("1");
		return value.divide(one, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static double round(String d) {
		return round(Double.parseDouble(d));
	}
}
