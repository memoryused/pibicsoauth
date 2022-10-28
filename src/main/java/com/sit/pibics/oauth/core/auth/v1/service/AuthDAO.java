package com.sit.pibics.oauth.core.auth.v1.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.logging.log4j.Logger;

import com.sit.core.common.domain.CommonSQLPath;
import com.sit.core.common.service.CommonDAO;
import com.sit.pibics.oauth.core.auth.v1.domain.Authorize;
import com.sit.pibics.oauth.core.auth.v1.domain.CredentialRequest;

import util.database.connection.CCTConnection;
import util.database.connection.CCTConnectionUtil;
import util.sql.SQLParameterizedDebugUtil;
import util.sql.SQLParameterizedUtil;
import util.string.StringUtil;

public class AuthDAO extends CommonDAO {

	public AuthDAO(Logger logger, CommonSQLPath sqlPath) {
		super(logger, sqlPath);
	}

	/**
	 * SQL : ตรวจสอบ clientID _SQL 
	 * @param conn
	 * @param clientId
	 * @return
	 * @throws Exception
	 */
	protected long searchCountAuthorizeByClientId(CCTConnection conn, String clientId) throws Exception {
		getLogger().info("");
		
		long count = 0l;
		
		int paramIndex = 0;
		Object[] params = new Object[1];
		params[paramIndex++] = clientId;
				
		String sql = SQLParameterizedUtil.getSQLString(conn.getSchemas()
				, getSqlPath().getClassName()
				, getSqlPath().getPath()
				, "searchCountAuthorizeByClientId"
				, params
				);
		
		if(getLogger().isDebugEnabled()) {
			getLogger().debug("SQL : searchCountAuthorizeByClientId \n {}", SQLParameterizedDebugUtil.debugReplaceParameter(sql, params));
		}
		
		PreparedStatement stmt = null;
		ResultSet rst = null;
		
		try {
			stmt = SQLParameterizedUtil.createPrepareStatement(conn.getConn(), sql,params);
			rst = stmt.executeQuery();
			
			if (rst.next()) {
				count = rst.getLong("CNT");
			}
		} catch (Exception e) {
			getLogger().error("");
			throw e;
		} finally {
			CCTConnectionUtil.closeAll(rst, stmt);
		}
		
		return count;
	}
	
	/**
	 * SQL : ตรวจสอบค่าใน SEC_AUTHORIZE2 _SQL
	 * @param conn
	 * @param clientId
	 * @return
	 * @throws Exception
	 */
	protected long searchCountAuthorizeByAuthorizeKey(CCTConnection conn, String authorizeKey) throws Exception {
		getLogger().info("");
		
		long count = 0l;
		
		int paramIndex = 0;
		Object[] params = new Object[1];
		params[paramIndex++] = authorizeKey;
		
		String sql = SQLParameterizedUtil.getSQLString(conn.getSchemas()
				, getSqlPath().getClassName()
				, getSqlPath().getPath()
				, "searchCountAuthorizeByAuthorizeKey"
				, params
				);
		
		if(getLogger().isDebugEnabled()) {
			getLogger().debug("SQL : searchCountAuthorizeByAuthorizeKey \n {}", SQLParameterizedDebugUtil.debugReplaceParameter(sql, params));
		}
		
		PreparedStatement stmt = null;
		ResultSet rst = null;
		
		try {
			stmt = SQLParameterizedUtil.createPrepareStatement(conn.getConn(), sql,params);
			rst = stmt.executeQuery();
			
			if (rst.next()) {
				count = rst.getLong("CNT");
			}
		} catch (Exception e) {
			getLogger().error("");
			throw e;
		} finally {
			CCTConnectionUtil.closeAll(rst, stmt);
		}
		
		return count;
	}
	
	/**
	 * SQL : ตรวจสอบค่าใน SEC_AUTHORIZE _SQL
	 * @param conn
	 * @param clientId
	 * @return
	 * @throws Exception
	 */
	protected Authorize searchAuthorizeByClientId(CCTConnection conn, Authorize authorizeReq) throws Exception {
		getLogger().info("");
		
		Authorize authorize = null;
		
		int paramIndex = 0;
		Object[] params = new Object[3];
		params[paramIndex++] = StringUtil.stringToNull(authorizeReq.getClientId());
		params[paramIndex++] = StringUtil.stringToNull(authorizeReq.getSecret());
		params[paramIndex] = StringUtil.stringToNull(authorizeReq.getGrantType());
				
		String sql = SQLParameterizedUtil.getSQLString(conn.getSchemas()
				, getSqlPath().getClassName()
				, getSqlPath().getPath()
				, "searchAuthorizeByClientId"
				, params
				);
		
		if(getLogger().isDebugEnabled()) {
			getLogger().debug("SQL : searchAuthorizeByClientId \n {}", SQLParameterizedDebugUtil.debugReplaceParameter(sql, params));
		}
		
		PreparedStatement stmt = null;
		ResultSet rst = null;
		
		try {
			stmt = SQLParameterizedUtil.createPrepareStatement(conn.getConn(), sql,params);
			rst = stmt.executeQuery();
			
			if (rst.next()) {
				authorize = new Authorize();
				authorize.setAuthorizeKey(StringUtil.nullToString(rst.getString("AUTHORIZE_KEY")));
				authorize.setEncData(StringUtil.nullToString(rst.getString("ENCRYPT_DATA")));
				authorize.setSalt(StringUtil.nullToString(rst.getString("SALT")));
				
				authorize.setClientId(authorizeReq.getClientId());
				authorize.setSecret(authorizeReq.getSecret());
				authorize.setGrantType(authorizeReq.getGrantType());
			}
		} catch (Exception e) {
			getLogger().error("");
			throw e;
		} finally {
			CCTConnectionUtil.closeAll(rst, stmt);
		}
		
		return authorize;
	}
	
	/**
	 * 
	 * @param conn
	 * @param encryptData
	 * @return
	 * @throws Exception
	 */
	protected Authorize searchAuthorizeByEncryptData(CCTConnection conn, String encryptData) throws Exception {
		getLogger().info("");
		
		Authorize authorize = null;
		
		int paramIndex = 0;
		Object[] params = new Object[1];
		params[paramIndex++] = encryptData;
				
		String sql = SQLParameterizedUtil.getSQLString(conn.getSchemas()
				, getSqlPath().getClassName()
				, getSqlPath().getPath()
				, "searchAuthorizeByEncryptData"
				, params
				);
		
		if(getLogger().isDebugEnabled()) {
			getLogger().debug("SQL : searchAuthorizeByEncryptData \n {}", SQLParameterizedDebugUtil.debugReplaceParameter(sql, params));
		}
		
		PreparedStatement stmt = null;
		ResultSet rst = null;
		
		try {
			stmt = SQLParameterizedUtil.createPrepareStatement(conn.getConn(), sql,params);
			rst = stmt.executeQuery();
			
			if (rst.next()) {
				authorize = new Authorize();
				authorize.setSecret(StringUtil.nullToString(rst.getString("SECRET_KEY")));
				authorize.setSalt(StringUtil.nullToString(rst.getString("SALT")));
				authorize.setToken(StringUtil.nullToString(rst.getString("TOKEN")));
				authorize.setGrantType(StringUtil.nullToString(rst.getString("GRANT_TYPE")));
				authorize.setEncData(StringUtil.nullToString(rst.getString("ENCRYPT_DATA")));
				authorize.setClientId(StringUtil.nullToString(rst.getString("CLIENT_ID")));
			}
		} catch (Exception e) {
			getLogger().error("");
			throw e;
		} finally {
			CCTConnectionUtil.closeAll(rst, stmt);
		}
		
		return authorize;
	}
	
	//******************************************************************************
	
	/**
	 * @deprecated
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	protected String searchSeqAuthKey(CCTConnection conn) throws Exception {
		getLogger().info("");
		
		long seq = 0l;
		String sql = SQLParameterizedUtil.getSQLString(conn.getSchemas()
				, getSqlPath().getClassName()
				, getSqlPath().getPath()
				, "searchSeqAuthKey");
		
		if (getLogger().isDebugEnabled()) {
			getLogger().debug("SQL: \n{}", sql);
		}

		PreparedStatement stmt = null;
		ResultSet rst = null;
		
		try {
			stmt = SQLParameterizedUtil.createPrepareStatement(conn.getConn(), sql);
			rst = stmt.executeQuery();
			
			if (rst.next()) {
				seq = rst.getLong("AUTHORIZE_KEY");
			}
		} 
		catch (Exception e) {
			getLogger().error("");
			throw e;
		} 
		finally {
			CCTConnectionUtil.closeAll(rst, stmt);
		}
		
		return String.valueOf(seq);
	}
	
	/**
	 * SQL : บันทึกค่า client id และ secret key _SQL 
	 * @param conn
	 * @param auth
	 * @param credentialRequest
	 * @throws Exception
	 */
	protected void registerAuth(CCTConnection conn, Authorize auth, CredentialRequest credentialRequest) throws Exception {
		getLogger().debug("");
		
		int paramIndex = 0;
		Object[] params = new Object[8];
		params[paramIndex++] = auth.getAuthorizeKey();
		params[paramIndex++] = auth.getClientId();
		params[paramIndex++] = auth.getSecret();
		params[paramIndex++] = auth.getSalt();
		params[paramIndex++] = auth.getEncData();
		params[paramIndex++] = auth.getGrantType();
		params[paramIndex++] = credentialRequest.getSystemName();
		params[paramIndex] = credentialRequest.getClientName();
		
		String sql = SQLParameterizedUtil.getSQLString(conn.getSchemas()
				, getSqlPath().getClassName()
				, getSqlPath().getPath()
				, "registerAuth"
				, params
				);
		
		if (getLogger().isDebugEnabled()) {
			getLogger().debug("SQL : registerAuth \n {}", SQLParameterizedDebugUtil.debugReplaceParameter(sql, params));
		}
		
		PreparedStatement stmt = null;
		try {
			stmt = SQLParameterizedUtil.createPrepareStatement(conn.getConn(), sql, params);
			stmt.executeUpdate();
			
		} catch (Exception e) {
			getLogger().error("");
			throw e;
			
		} finally {
			CCTConnectionUtil.closeStatement(stmt);
		}	    
	}

	/**
	 * SQL : บันทึกยกเลิกใช้งาน _SQL
	 * @param conn
	 * @param authorizeKey
	 * @throws Exception
	 */
	protected void updateCancelAuthorize(CCTConnection conn, String authorizeKey) throws Exception {
		getLogger().debug("");
		
		int paramIndex = 0;
		Object[] params = new Object[1];
		params[paramIndex] = authorizeKey;
		
		String sql = SQLParameterizedUtil.getSQLString(conn.getSchemas()
				, getSqlPath().getClassName()
				, getSqlPath().getPath()
				, "updateCancelAuthorize"
				, params
				);
		
		if (getLogger().isDebugEnabled()) {
			getLogger().debug("SQL : updateCancelAuthorize \n {}", SQLParameterizedDebugUtil.debugReplaceParameter(sql, params));
		}
		
		PreparedStatement stmt = null;
		try {
			stmt = SQLParameterizedUtil.createPrepareStatement(conn.getConn(), sql, params);
			stmt.executeUpdate();
			
		} catch (Exception e) {
			getLogger().error("");
			throw e;
			
		} finally {
			CCTConnectionUtil.closeStatement(stmt);
		}	    
	}
	
	/**
	 * SQL : บันทึกค่า token _SQL 
	 * @param conn
	 * @param authorizeKey
	 * @throws Exception
	 */
	protected void updateToken(CCTConnection conn, String token, String authorizeKey) throws Exception {
		getLogger().debug("");
		
		int paramIndex = 0;
		Object[] params = new Object[2];
		params[paramIndex++] = token;
		params[paramIndex] = authorizeKey;
		
		String sql = SQLParameterizedUtil.getSQLString(conn.getSchemas()
				, getSqlPath().getClassName()
				, getSqlPath().getPath()
				, "updateToken"
				, params
				);
		
		if (getLogger().isDebugEnabled()) {
			getLogger().debug("SQL : updateToken \n {}", SQLParameterizedDebugUtil.debugReplaceParameter(sql, params));
		}
		
		PreparedStatement stmt = null;
		try {
			stmt = SQLParameterizedUtil.createPrepareStatement(conn.getConn(), sql, params);
			stmt.executeUpdate();
			
		} catch (Exception e) {
			getLogger().error("");
			throw e;
			
		} finally {
			CCTConnectionUtil.closeStatement(stmt);
		}	    
	}
}
