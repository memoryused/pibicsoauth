package com.sit.pibics.oauth.enums;

public enum Authorization {
	BASIC("Basic"), BEARER ("Bearer");

	private String value;

	private Authorization(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
