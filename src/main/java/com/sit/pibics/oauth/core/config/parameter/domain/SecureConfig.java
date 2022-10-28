package com.sit.pibics.oauth.core.config.parameter.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SecureConfig implements Serializable {

	private static final long serialVersionUID = 1396851110010390530L;

	private String host;
	private String origin;
	private List<String> allowedDomains = new ArrayList<>();
	private String method;
	private String header;
	private String maxage;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public List<String> getAllowedDomains() {
		return allowedDomains;
	}

	public void setAllowedDomains(List<String> allowedDomains) {
		this.allowedDomains = allowedDomains;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getMaxage() {
		return maxage;
	}

	public void setMaxage(String maxage) {
		this.maxage = maxage;
	}

}
