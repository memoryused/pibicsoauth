package com.sit.pibics.oauth.exception;

public class AuthorizationException extends CommonException {

	private static final long serialVersionUID = 3457404600878463484L;

	public AuthorizationException() {
		super(getDefaultMessage());
	}

	public AuthorizationException(String message) {
		super(message);
	}
	
	public AuthorizationException(Throwable cause) {
		super(cause);
	}
	
	public AuthorizationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	@Override
	public String getErrorCode() {
		return ErrorCode.AUTHORIZATION.getErrorCode();
	}

	@Override
	public String getErrorDesc() {
		return ErrorCode.AUTHORIZATION.getErrorDesc();
	}

	public static String getDefaultMessage() {
		return DefaultExceptionMessage.AUTHORIZATION;
	}
	
}
