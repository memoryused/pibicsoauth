package com.sit.pibics.oauth.enums;

public enum SystemKey {
	APPS("APPS"), TM30("TM30");

	private String value;

	private SystemKey(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
