package com.sit.pibics.oauth.exception;

public enum ErrorCode {
	/** common exception **/
	DEFAULT("00001", "Undefined exception handle.")
	, AUTHENTICATION("10005", "Authentication invalid.")
	, INPUT_VALIDATE("10009", "Invalid XML/JSON.")
	, AUTHORIZATION("10001", "Authorized invalid.")
	
	/** SIT exception **/
	, CUSTOM_VALIDATE("10006", "ClientID invalid")
	, TOKEN_EXPIRE("10008", "Token expired.")
	, TOKEN_VALIDATE("10007", "Token invalid.")
	, DEPRECATED_VERSION("SIT-13000", "Deprecated version. Please update latest version.")
	;
	
	private String errorCode;
	private String errorDesc;

	private ErrorCode(String errorCode, String errorDesc) {
		this.errorCode = errorCode;
		this.errorDesc = errorDesc;
	}
	
	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorDesc() {
		return errorDesc;
	}
}
