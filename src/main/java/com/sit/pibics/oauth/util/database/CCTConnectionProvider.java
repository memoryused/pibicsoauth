package com.sit.pibics.oauth.util.database;

import com.sit.pibics.oauth.core.config.parameter.domain.ParameterConfig;

import util.database.connection.WebCommonConnectionProvider;

public class CCTConnectionProvider extends WebCommonConnectionProvider {

	public CCTConnectionProvider() throws Exception {
		super(ParameterConfig.getDatabase());
	}
}
