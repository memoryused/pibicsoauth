package com.sit.pibics.oauth.util.auth;

import java.io.Serializable;

public class Payload implements Serializable {

	private static final long serialVersionUID = 8923790673449312562L;

	private long exp;
	private String id;

	public long getExp() {
		return exp;
	}

	public void setExp(long exp) {
		this.exp = exp;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
}
