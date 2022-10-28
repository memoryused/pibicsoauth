package com.sit.pibics.oauth.util;

public class PibicsOAuthUtil {
	
	/**
	 * @param inputValue
	 * @return <br>
	 * true เมื่อมีค่าเช่น A, B <br>
	 * false เมื่อเป็น null หรือ empty 
	 */
	public static boolean hasValue(Object inputValue) {
		if (inputValue == null) {
			return false;
		} else {
			return !String.valueOf(inputValue).trim().isEmpty();
		}
	}
}
