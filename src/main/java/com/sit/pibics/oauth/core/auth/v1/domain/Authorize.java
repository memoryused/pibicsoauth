package com.sit.pibics.oauth.core.auth.v1.domain;

import java.io.Serializable;

public class Authorize implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String authorizeKey;
	private String clientId;
	private String secret;
	private String salt;
	private String token;
	private String grantType;
	private String encData;
	private String active;
	
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getEncData() {
		return encData;
	}
	public void setEncData(String encData) {
		this.encData = encData;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getGrantType() {
		return grantType;
	}
	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}
	public String getAuthorizeKey() {
		return authorizeKey;
	}
	public void setAuthorizeKey(String authorizeKey) {
		this.authorizeKey = authorizeKey;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
}
