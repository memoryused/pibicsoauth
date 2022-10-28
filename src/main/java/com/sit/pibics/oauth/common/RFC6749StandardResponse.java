package com.sit.pibics.oauth.common;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RFC6749StandardResponse implements Serializable {

	private static final long serialVersionUID = 8600904529203939286L;

	@JsonProperty("access_token")
	private String accessToken; 
	
	@JsonProperty("token_type")
	private String tokenType; 
	
	@JsonProperty("expires_in")
	private long expiresIn;
	
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	public long getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	} 
}
