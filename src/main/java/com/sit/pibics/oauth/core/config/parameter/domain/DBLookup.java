package com.sit.pibics.oauth.core.config.parameter.domain;

public enum DBLookup {
	pibicsoauth("pibicsoauth");

	private String lookup;

	private DBLookup(String lookup) {
		this.lookup = lookup;
	}

	public String getLookup() {
		return lookup;
	}
}