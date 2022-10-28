package com.sit.pibics.oauth.exception;

/**
 * ใช้สำหรับยืนยันตัวตน
 *
 */
public class AuthenticateException extends CommonException {

	private static final long serialVersionUID = -7351163217961819178L;

	public AuthenticateException() {
		super(getDefaultMessage());
	}

	public AuthenticateException(String message) {
		super(message);
	}
	
	public AuthenticateException(Throwable cause) {
		super(cause);
	}

	public AuthenticateException(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public String getErrorCode() {
		return ErrorCode.AUTHENTICATION.getErrorCode();
	}

	@Override
	public String getErrorDesc() {
		return ErrorCode.AUTHENTICATION.getErrorDesc();
	}
	
	public static String getDefaultMessage() {
		return DefaultExceptionMessage.AUTHENTICATION;
	}

}