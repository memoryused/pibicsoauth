package com.sit.pibics.oauth.core.config.parameter.domain;

import java.io.Serializable;

public class Application implements Serializable {

	private static final long serialVersionUID = -6791131125526807670L;

	private boolean enableSQLParameterizedDebug;
	private boolean enableSQLParameterizedParamDebug;
	private long tokenExpireTime;
	
	public boolean isEnableSQLParameterizedDebug() {
		return enableSQLParameterizedDebug;
	}
	public void setEnableSQLParameterizedDebug(boolean enableSQLParameterizedDebug) {
		this.enableSQLParameterizedDebug = enableSQLParameterizedDebug;
	}
	public boolean isEnableSQLParameterizedParamDebug() {
		return enableSQLParameterizedParamDebug;
	}
	public void setEnableSQLParameterizedParamDebug(boolean enableSQLParameterizedParamDebug) {
		this.enableSQLParameterizedParamDebug = enableSQLParameterizedParamDebug;
	}
	public long getTokenExpireTime() {
		return tokenExpireTime;
	}
	public void setTokenExpireTime(long tokenExpireTime) {
		this.tokenExpireTime = tokenExpireTime;
	}
}
