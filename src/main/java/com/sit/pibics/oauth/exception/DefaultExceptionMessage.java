package com.sit.pibics.oauth.exception;

/**
 * ค่า Default Exception Message ต่างๆ ที่จัดเตรียมไว้ให้
 * @author sittipol.m
 *
 */
public class DefaultExceptionMessage {

	public static String DEFAULT = "Internal Server Error.";
	public static String CUSTOM;
	public static String AUTHENTICATION = "Authentication invalid.";
	public static String AUTHORIZATION = "Authorized invalid.";
	public static String SERVER_VALIDATE = "Invalid XML/JSON";
	public static String INPUT_VALIDATE = "Invalid XML/JSON";
	public static String TOKEN_VALIDATE = "Token invalid.";
	public static String CLIENT_VALIDATE = "ClientID invalid.";
	
	public static String TOKEN_EXPIRE = "Token expired.";
	public static String DEPRECATED_VERSION = "Deprecated version. Please update latest version.";
	
}
