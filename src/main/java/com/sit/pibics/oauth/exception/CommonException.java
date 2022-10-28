package com.sit.pibics.oauth.exception;

public class CommonException extends Exception {

	private static final long serialVersionUID = 461924630018745907L;

	private String loggingCode;

	public CommonException() {
		super(getDefaultMessage());
	}

	public CommonException(String message) {
		super(message);
	}

	public CommonException(Throwable cause) {
		super(cause);
	}

	public CommonException(String message, Throwable cause) {
		super(message, cause);
	}

	public String getErrorCode() {
		return ErrorCode.DEFAULT.getErrorCode();
	}

	public String getErrorDesc() {
		return ErrorCode.DEFAULT.getErrorDesc();
	}

	public String getLoggingCode() {
		return loggingCode;
	}

	public void setLoggingCode(String loggingCode) {
		this.loggingCode = loggingCode;
	}

	public static String getDefaultMessage() {
		return DefaultExceptionMessage.DEFAULT;
	}
	
}
