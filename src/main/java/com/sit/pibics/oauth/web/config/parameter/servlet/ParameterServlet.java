package com.sit.pibics.oauth.web.config.parameter.servlet;

import java.io.IOException;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;

import com.sit.pibics.oauth.core.config.parameter.domain.DBLookup;
import com.sit.pibics.oauth.core.config.parameter.domain.ParameterConfig;
import com.sit.pibics.oauth.core.config.parameter.service.ParameterManager;
import com.sit.pibics.oauth.util.database.CCTConnectionProvider;

import util.database.connection.CCTConnection;
import util.database.connection.CCTConnectionUtil;
import util.log4j2.DefaultLogUtil;
import util.sql.SQLParameterizedDebugUtil;

public class ParameterServlet extends HttpServlet {

	private static final long serialVersionUID = -2473461747972119759L;

	private Logger logger = DefaultLogUtil.INITIAL;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		init();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		init();
	}

	public void init() throws ServletException {
		getLogger().info("");
		
		ParameterManager parameterManager = new ParameterManager(getLogger());
		try {
			getLogger().info("Locale.getDefault Before :{} ", Locale.getDefault());
			Locale.setDefault(Locale.getDefault());
			getLogger().info("Locale.getDefault After :{} ", Locale.getDefault());
			
			getLogger().info("Parameter load...");
			String parameterFile = getServletContext().getRealPath(getInitParameter("parameterfile"));
			getLogger().info(" parameter path : {} ", parameterFile);
			ParameterConfig.init(parameterFile);
			getLogger().info("Parameter load...completed");
			
			getLogger().info("Secure load...");
			String secureFile = getServletContext().getRealPath(getInitParameter("securefile"));
			getLogger().debug("Secure path : {}", secureFile);			
			parameterManager.loadSecre(secureFile);
			
			// เปิดใช้งาน feature แทน parameter ที่ sql
			if (ParameterConfig.getApplication().isEnableSQLParameterizedDebug()) {
				System.setProperty(SQLParameterizedDebugUtil.PARAMETERZIZED_DEBUG, "Y");
			}
			
			// 2020CAAT-2622 : เพิ่ม config ให้ print แต่ parameter ที่ส่งเข้าไปใน sql ได้
			if (ParameterConfig.getApplication().isEnableSQLParameterizedParamDebug()) {
				System.setProperty(SQLParameterizedDebugUtil.PARAMETERZIZED_PARAMS_DEBUG, "Y");
			}
			
		} catch (Exception e) {
			getLogger().info("Parameter load...error", e);
		}
		
		try {
			getLogger().info("Database test...");
			parameterManager.testDBConnection(ParameterConfig.getDatabase());
			getLogger().info("Database test...ok");
		} catch (Exception e) {
			getLogger().error("Database test...error.", e);
		}
		
		CCTConnection conn = null;
		try {
			conn = new CCTConnectionProvider().getConnection(conn, DBLookup.pibicsoauth.getLookup());
			getLogger().info("SQL test...");
			//getLogger().info("SQL test [" + parameterManager.testSQL(conn) + "]...ok");		
		} catch (Exception e) {
			getLogger().error("SQL test...error.", e);
		} finally {
			CCTConnectionUtil.close(conn);
		}
		
		/*
		try {
			getLogger().debug("Initial JAXB for payment.");
			// new PaymentManager(getLogger()).initXML();
			getLogger().debug("Initial JAXB for payment completed.");

		} catch (Exception e) {
			getLogger().error("Can't initial JAXB for payment!!!", e);
		}
		*/
	}

	@Override
	public void destroy() {
		getLogger().info("");
		// This manually deregisters JDBC driver
		// , which prevents Tomcat 7 from complaining about memory leaks wrto this class
        Enumeration<java.sql.Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                getLogger().debug(String.format("deregistering jdbc driver: %s", driver));
            } catch (SQLException e) {
            	getLogger().error(String.format("Error deregistering driver %s", driver), e);
            }
        }
	}
	
	public Logger getLogger() {
		return logger;
	}

}
