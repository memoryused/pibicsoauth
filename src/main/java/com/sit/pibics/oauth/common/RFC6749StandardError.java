package com.sit.pibics.oauth.common;

import java.io.Serializable;

public class RFC6749StandardError implements Serializable {

	private static final long serialVersionUID = 2351595205398968778L;

	private String error;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	} 
}
