package com.sit.pibics.oauth.core.config.parameter.service;

import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.logging.log4j.Logger;

import com.sit.core.common.domain.CommonSQLPath;
import com.sit.core.common.service.CommonDAO;

import util.database.connection.CCTConnection;
import util.database.connection.CCTConnectionUtil;
import util.sql.SQLUtil;


public class ParameterDAO extends CommonDAO {

	public ParameterDAO(Logger logger, CommonSQLPath sqlPath) {
		super(logger, sqlPath);
	}
	
	/**
	 * ทดสอบ SQL
	 * @param conn
	 * @return
	 * @throws Exception
	 */
protected String testSQL(CCTConnection conn) throws Exception {
		
		String result = null;
		
		String sql = SQLUtil.getSQLString(conn.getSchemas()
				, getSqlPath().getClassName()
				, getSqlPath().getPath()
				, "testSQL");
		
		Statement stmt = null;
		ResultSet rst = null;
		try {
			stmt = createStatement(conn);
			rst = executeQuery(stmt, sql);
			if (rst.next()) {
				result = rst.getString("SYSDATE");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			CCTConnectionUtil.closeAll(rst, stmt);
		}
		return result;
	}
}
