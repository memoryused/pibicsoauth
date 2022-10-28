package com.sit.pibics.oauth.common;

import java.io.Serializable;

public class CommonResponse implements Serializable {

	private static final long serialVersionUID = -5167469020561219477L;
	
	private String messageCode; // message code
	private String messageDesc; // message description
    private transient Object data; // process result
	private CommonError error; // error

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public String getMessageDesc() {
		return messageDesc;
	}

	public void setMessageDesc(String messageDesc) {
		this.messageDesc = messageDesc;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public CommonError getError() {
		return error;
	}

	public void setError(CommonError error) {
		this.error = error;
	}
}