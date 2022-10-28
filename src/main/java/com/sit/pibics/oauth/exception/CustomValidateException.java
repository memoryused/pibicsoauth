package com.sit.pibics.oauth.exception;

public class CustomValidateException extends CommonException {

	private static final long serialVersionUID = 5324359581691805266L;

	private String code = null;
	
	public CustomValidateException() {
		super(getDefaultMessage());
	}

	public CustomValidateException(String message) {
		super(message);
	}
	
	public CustomValidateException(String code, String message) {
		super(message);
		this.setCode(code);
	}
	
	public CustomValidateException(Throwable cause) {
		super(cause);
	}
	
	public CustomValidateException(String message, Throwable cause) {
		super(message, cause);
	}
	
	@Override
	public String getErrorCode() {
		return this.getCode();
	}

	@Override
	public String getErrorDesc() {
		return this.getMessage();
	}

	public static String getDefaultMessage() {
		return DefaultExceptionMessage.CLIENT_VALIDATE;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
