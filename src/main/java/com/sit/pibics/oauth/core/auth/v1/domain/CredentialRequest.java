package com.sit.pibics.oauth.core.auth.v1.domain;

import java.io.Serializable;

public class CredentialRequest implements Serializable {

	private static final long serialVersionUID = 6901647779360855882L;
	
	private String id;
	private String systemName;
	private String clientName;
	private String grantType;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getGrantType() {
		return grantType;
	}
	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

}
