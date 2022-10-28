package com.sit.pibics.oauth.core.config.parameter.domain;

import java.io.Serializable;

import com.sit.pibics.oauth.core.config.parameter.service.ParameterManager;

import util.database.Database;
import util.log4j2.DefaultLogUtil;

public class ParameterConfig implements Serializable {

	private static final long serialVersionUID = -2187894195556282622L;
	
	private static Parameter parameter;
	private static SecureConfig secure;

	public static void init(String parameterFile) {
		try {
			DefaultLogUtil.INITIAL.debug("Parameter path :- " + parameterFile);
			parameter = new ParameterManager(DefaultLogUtil.INITIAL).get(parameterFile);
		} catch (Exception e) {
			DefaultLogUtil.INITIAL.error("Can't load Parameter!!!", e);
		}
	}

	public static Application getApplication() {
		return parameter.getApplication();
	}

	public static Database[] getDatabase() {
		return parameter.getDatabase();
	}

	public static Certification getCertification() {
		return parameter.getCertification();
	}

	public static SecureConfig getSecure() {
		return secure;
	}

	public static void setSecure(SecureConfig secure) {
		ParameterConfig.secure = secure;
	}
	
}
