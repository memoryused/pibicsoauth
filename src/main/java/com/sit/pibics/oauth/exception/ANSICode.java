package com.sit.pibics.oauth.exception;

public enum ANSICode {

	INVALID_REQ("invalid_request")
	, INVALID_CLI("invalid_client")
	, INVALID_GR("invalid_grant")
	, UNAUTH_CLI("unauthorized_client")
	, UNSUPPPORT_GR("unsupported_grant_type")
	, INVALID_SCOPE("invalid_scope")
	;
	
	private String value;
	private ANSICode(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
}
