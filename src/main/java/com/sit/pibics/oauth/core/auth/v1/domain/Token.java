package com.sit.pibics.oauth.core.auth.v1.domain;

import java.io.Serializable;

/**
 * สำหรับเก็บข้อมูลการ encrypt ในการ request
 *
 */
public class Token implements Serializable {
	private static final long serialVersionUID = 2728506609946671950L;

	private String accessToken;
	private String encrypt;
	private String salt;
	private String secret;

	public String getEncrypt() {
		return encrypt;
	}

	public void setEncrypt(String encrypt) {
		this.encrypt = encrypt;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

}
