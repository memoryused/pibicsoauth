package com.sit.pibics.oauth.exception;

public class TokenValidateException extends CommonException {

	private static final long serialVersionUID = 5324359581691805266L;

	public TokenValidateException() {
		super(getDefaultMessage());
	}

	public TokenValidateException(String message) {
		super(message);
	}
	
	public TokenValidateException(Throwable cause) {
		super(cause);
	}
	
	public TokenValidateException(String message, Throwable cause) {
		super(message, cause);
	}
	
	@Override
	public String getErrorCode() {
		return ErrorCode.TOKEN_VALIDATE.getErrorCode();
	}

	@Override
	public String getErrorDesc() {
		return ErrorCode.TOKEN_VALIDATE.getErrorDesc();
	}

	public static String getDefaultMessage() {
		return DefaultExceptionMessage.TOKEN_VALIDATE;
	}
}
