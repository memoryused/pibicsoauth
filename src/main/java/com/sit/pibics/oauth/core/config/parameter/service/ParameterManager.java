package com.sit.pibics.oauth.core.config.parameter.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.logging.log4j.Logger;

import com.sit.pibics.oauth.core.config.parameter.domain.Parameter;
import com.sit.pibics.oauth.core.config.parameter.domain.ParameterConfig;
import com.sit.pibics.oauth.core.config.parameter.domain.SecureConfig;
import com.sit.core.common.service.CommonManager;

import util.database.Database;
import util.database.connection.CCTConnection;
import util.database.connection.CCTConnectionUtil;
import util.database.enums.DbType;
import util.log4j2.DefaultLogUtil;


public class ParameterManager extends CommonManager {

	public static final String XML_PATH = System.getProperty("user.dir") + "/parameter.xml";
	
	private ParameterService service = null;
	
	public ParameterManager(Logger logger) {
		super(logger);
		this.service = new ParameterService(getLogger());
	}

	public Parameter get(String xmlPath) throws Exception {
		Parameter parameter = null;
		try {
			parameter = getService().load(xmlPath);
		} catch (Exception e) {
			throw e;
		}
		return parameter;
	}

	public void testDBConnection(Database[] dbConfig) {
		getLogger().debug("DB Connection test...");
		for (Database database : dbConfig) {
			CCTConnection conn = null;
			try {
				conn = getTestDBConnection(database);
				getLogger().debug(database.getKey() + " > " + database.getLookup() + " > is ok.");
			} catch (Exception e) {
				getLogger().error(database.getKey() + " > " + database.getLookup() + " > is error.", e);
			} finally {
				CCTConnectionUtil.close(conn);
			}
		}
	}
	
	public CCTConnection getTestDBConnection(Database db) throws Exception {
		CCTConnection conn = null;
		Context context = new InitialContext();
		if (db.isJndi()) {
			context = (Context) context.lookup("java:comp/env");
		}
		DataSource ddss = (DataSource) context.lookup(db.getLookup());
		
		try {
			if (conn == null) {
				conn = new CCTConnection();
			}

			if ((conn.getConn() == null) || conn.getConn().isClosed()) {
				conn.setConn(ddss.getConnection());
				
				conn.getSchemas().clear();
				conn.getSchemas().putAll(db.getSchemasMap());

				getLogger().debug(db.getDatabaseType().toUpperCase());
				conn.setDbType(DbType.valueOf(db.getDatabaseType().toUpperCase()));
			}
		} catch (Exception e) {
			getLogger().catching(e);
			throw e;
		}
		return conn;
	}
	
	public String testSQL(CCTConnection conn) throws Exception {
		return getService().testSQL(conn);
		
	}
	
	public void loadSecre(String securePath) throws IOException {

		// REVIEW : Code analysis by anusorn.l : Try-with-resources should be used
		try (InputStream inputStream = new FileInputStream(securePath)) {
			if (new File(securePath).exists()) {
				SecureConfig secure = new SecureConfig();
				Properties prop = new Properties();
				prop.load(inputStream);

				String origin = prop.getProperty("origin");
				secure.setOrigin(origin);

				if (origin != null && !origin.equals("")) {
					String[] arrOrigin = origin.split(",");
					for (int i = 0; i < arrOrigin.length; i++) {
						secure.getAllowedDomains().add(arrOrigin[i]);
					}
				}

				secure.setMethod(prop.getProperty("method"));
				secure.setHeader(prop.getProperty("header"));
				secure.setMaxage(prop.getProperty("maxage"));
				secure.setHost(prop.getProperty("host"));
				ParameterConfig.setSecure(secure);
			}
		} catch (Exception e) {
			// REVIEW-14 : Code Analysis by Anusorn.l : Rethrow the exception
			getLogger().error(e.getCause());
			throw e;
		}
	}

	
	public static void main(String[] args) {
		ParameterManager m = new ParameterManager(DefaultLogUtil.INITIAL);
		try {
			Parameter parameter = m.get(XML_PATH);
			m.testDBConnection(parameter.getDatabase());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ParameterService getService() {
		return service;
	}
}
