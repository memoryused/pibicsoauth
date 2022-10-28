package com.sit.pibics.oauth.core.config.parameter.service;

import org.apache.logging.log4j.Logger;

import com.sit.pibics.oauth.core.config.parameter.domain.Parameter;
import com.sit.pibics.oauth.core.config.parameter.domain.OAuthSQLPath;
import com.sit.core.common.service.CommonService;

import util.database.connection.CCTConnection;
import util.xml.XMLUtil;

public class ParameterService extends CommonService {

	private ParameterDAO dao = null;
	
	public ParameterService(Logger logger) {
		super(logger);
		this.dao = new ParameterDAO(getLogger(), OAuthSQLPath.TEST_SQL.getSqlPath());
	}

	protected Parameter load(String filePath) throws Exception {
		Parameter parameter = new Parameter();
		try {
			getLogger().debug("path :- " + filePath);
			parameter = (Parameter) XMLUtil.xmlToObject(filePath, parameter);
		} catch (Exception e) {
			throw e;
		}
		return parameter;
	}

	protected String testSQL(CCTConnection conn) throws Exception {
		return getDao().testSQL(conn);
	}
	
	public ParameterDAO getDao() {
		return dao;
	}
}
