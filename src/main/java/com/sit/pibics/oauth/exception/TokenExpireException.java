package com.sit.pibics.oauth.exception;

public class TokenExpireException extends CommonException {

	private static final long serialVersionUID = 5324359581691805266L;

	public TokenExpireException() {
		super(getDefaultMessage());
	}

	public TokenExpireException(String message) {
		super(message);
	}
	
	public TokenExpireException(Throwable cause) {
		super(cause);
	}
	
	public TokenExpireException(String message, Throwable cause) {
		super(message, cause);
	}
	
	@Override
	public String getErrorCode() {
		return ErrorCode.TOKEN_EXPIRE.getErrorCode();
	}

	@Override
	public String getErrorDesc() {
		return ErrorCode.TOKEN_EXPIRE.getErrorDesc();
	}

	public static String getDefaultMessage() {
		return DefaultExceptionMessage.TOKEN_EXPIRE;
	}
}
