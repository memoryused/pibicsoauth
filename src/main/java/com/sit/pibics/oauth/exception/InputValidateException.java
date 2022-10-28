package com.sit.pibics.oauth.exception;

/**
 * ข้อมูลที่ได้รับไม่ถูกต้อง
 *
 */
public class InputValidateException extends CommonException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2418835834743232665L;

	public InputValidateException() {
		super(getDefaultMessage());
	}
	
	public InputValidateException(String message) {
		super(message);
	}
	
	public InputValidateException(Throwable cause) {
		super(cause);
	}
	
	public InputValidateException(String message, Throwable cause) {
		super(message, cause);
	}
	
	@Override
	public String getErrorCode() {
		return ErrorCode.INPUT_VALIDATE.getErrorCode();
	}

	@Override
	public String getErrorDesc() {
		return ErrorCode.INPUT_VALIDATE.getErrorDesc();
	}
	
	public static String getDefaultMessage() {
		return DefaultExceptionMessage.INPUT_VALIDATE;
	}
	
}
