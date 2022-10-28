package com.sit.pibics.oauth.core.config.parameter.domain;

import java.io.Serializable;

public class Certification implements Serializable{

	private static final long serialVersionUID = 7510748926132246210L;
	
	private String publicKeyFile;
	private String privateKeyFile;
	private String alias;
	private String pwd;
	
	public String getPublicKeyFile() {
		return publicKeyFile;
	}
	public void setPublicKeyFile(String publicKeyFile) {
		this.publicKeyFile = publicKeyFile;
	}
	public String getPrivateKeyFile() {
		return privateKeyFile;
	}
	public void setPrivateKeyFile(String privateKeyFile) {
		this.privateKeyFile = privateKeyFile;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
}
