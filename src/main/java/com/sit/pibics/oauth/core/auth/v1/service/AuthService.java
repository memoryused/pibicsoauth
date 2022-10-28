package com.sit.pibics.oauth.core.auth.v1.service;

import org.apache.logging.log4j.Logger;

import com.sit.core.common.service.CommonService;
import com.sit.pibics.oauth.core.auth.v1.domain.Authorize;
import com.sit.pibics.oauth.core.auth.v1.domain.CredentialRequest;
import com.sit.pibics.oauth.core.config.parameter.domain.OAuthSQLPath;
import com.sit.pibics.oauth.enums.Authorization;
import com.sit.pibics.oauth.exception.AuthorizationException;
import com.sit.pibics.oauth.exception.CustomValidateException;
import com.sit.pibics.oauth.exception.DefaultExceptionMessage;
import com.sit.pibics.oauth.exception.ErrorCode;
import com.sit.pibics.oauth.util.PibicsOAuthUtil;
import com.sit.pibics.oauth.util.auth.SecurityUtil;

import util.database.connection.CCTConnection;

public class AuthService extends CommonService {
	
	private AuthDAO dao;

	public AuthService(Logger logger) {
		super(logger);
		this.dao = new AuthDAO(logger, OAuthSQLPath.TEST_SQL.getSqlPath());
	}
	
	/**
	 * Search Authorize By Encrypt Data
	 * @param conn
	 * @param encryptData
	 * @return
	 * @throws Exception
	 */
	protected Authorize searchAuthorizeByEncryptData(CCTConnection conn, String encryptData) throws Exception {
		return dao.searchAuthorizeByEncryptData(conn, encryptData);
	}
	
	/**
	 * Validate Get Access Token Request
	 * @param authorizationHeader
	 * @return
	 * @throws Exception
	 */
	protected Authorize validateGetAccessTokenRequest(CCTConnection conn, Authorize authorizeReq) throws Exception {
		getLogger().info("");
		
		Authorize authorizeRes = null;
		
		try {
			// 1. ตรวจสอบว่ามี Client Id นี้ในฐานข้อมูล
			authorizeRes = dao.searchAuthorizeByClientId(conn, authorizeReq);
			if(authorizeRes == null) {
				getLogger().error("Not found client id in database.");
				throw new AuthorizationException(DefaultExceptionMessage.AUTHORIZATION);
			} 
		} catch (Exception e) {
			getLogger().error("");
			throw e;
		}
		return authorizeRes;
	}
	
	/**
	 * Validate Header Create Credential Request
	 * @param conn
	 * @param authorizationHeader
	 * @throws Exception
	 */
	protected void validateHeaderCreateCredentialRequest(CCTConnection conn, String clientId) throws Exception {
		getLogger().info("");
		
		try {
			// ตรวจสอบว่ามี Client Id นี้ในฐานข้อมูล ?
			long count = dao.searchCountAuthorizeByClientId(conn, clientId);
			if(count == 0) {
				getLogger().error("Not found clientId in database.");
				throw new CustomValidateException(ErrorCode.CUSTOM_VALIDATE.getErrorCode(), ErrorCode.CUSTOM_VALIDATE.getErrorDesc());
			}
			
		} catch (Exception e) {
			getLogger().error("");
			throw e;
		}
	}
	
	/**
	 * Validate Data Create Credential Request
	 * @param credentialRequest
	 * @throws AuthorizationException
	 */
	protected void validateDataCreateCredentialRequest(CredentialRequest credentialRequest) throws AuthorizationException{
		getLogger().info("");
		
		//มีอย่างน้อย 1 ค่าก็ยังดี
		if (credentialRequest == null || !PibicsOAuthUtil.hasValue(credentialRequest.getId()) || !PibicsOAuthUtil.hasValue(credentialRequest.getSystemName()) 
				|| !PibicsOAuthUtil.hasValue(credentialRequest.getClientName()) || !PibicsOAuthUtil.hasValue(credentialRequest.getGrantType())  ) {
			throw new AuthorizationException(DefaultExceptionMessage.AUTHORIZATION);
		}
	}
	
	/**
	 * Validate Header Cancel Authorize Request
	 * @param conn
	 * @param authorizeKey
	 * @throws Exception
	 */
	protected void validateHeaderCancelAuthorizeRequest(CCTConnection conn, String authorizeKey) throws Exception {
		getLogger().info("");

		try {
			// ตรวจสอบว่ามี AUTHORIZE_KEY นี้ในฐานข้อมูล ?
			long count = dao.searchCountAuthorizeByAuthorizeKey(conn, authorizeKey);
			if(count == 0) {
				getLogger().error("Not found AUTHORIZE_KEY in database.");
				throw new AuthorizationException(DefaultExceptionMessage.AUTHORIZATION);
			}
			
		} catch (Exception e) {
			getLogger().error("");
			throw e;
		}
	}
	
	/**
	 * Create Credential
	 * @param conn
	 * @param credentialRequest
	 * @return
	 * @throws Exception
	 */
	protected Authorize manageCredential(CCTConnection conn, CredentialRequest credentialRequest) throws Exception {
		getLogger().info("");
		
		Authorize authorizeRes = new Authorize();
		
		try {
			// Generate Credential
			SecurityUtil secUtil = new SecurityUtil();
			Authorize authorize = new Authorize();
			authorize.setAuthorizeKey(secUtil.genSalt());
			authorize.setClientId(secUtil.genClientId());
			authorize.setSecret(secUtil.genSecret());
			authorize.setSalt(secUtil.genSalt());
			authorize.setEncData(secUtil.encryptId(authorize.getSalt(), authorize.getSecret(), credentialRequest.getId()));
			authorize.setGrantType(credentialRequest.getGrantType());
			
			dao.registerAuth(conn, authorize, credentialRequest);

			// For response
			authorizeRes.setClientId(authorize.getClientId());
			authorizeRes.setSecret(authorize.getSecret());
			authorizeRes.setAuthorizeKey(authorize.getAuthorizeKey());
		} catch (Exception e) {
			getLogger().error("");
			throw e;
		}
		return authorizeRes;
	}
	
	/**
	 * Update Cancel Authorize
	 * @param conn
	 * @param authorizeKey
	 * @throws Exception
	 */
	protected void updateCancelAuthorize(CCTConnection conn, String authorizeKey) throws Exception {
		dao.updateCancelAuthorize(conn, authorizeKey);
	}
	
	/**
	 * SQL : บันทึกค่า token _SQL
	 * @param conn
	 * @param token
	 * @param authorizeKey
	 * @throws Exception
	 */
	protected void updateToken(CCTConnection conn, String token, String authorizeKey) throws Exception {
		dao.updateToken(conn, token, authorizeKey);
	}
	
	/**
	 * Get Authorize Code Basic
	 * @param authorizationHeader
	 * @return
	 * @throws AuthorizationException 
	 * @throws Exception
	 */
	public Authorize getAuthorizeCodeBasic(String authorizationHeader) throws AuthorizationException {
		getLogger().info("");
		
		Authorize authorizeReq = null;
		
		try {
			if(authorizationHeader != null) {
				String authorizeCode = authorizationHeader.replace(Authorization.BASIC.getValue(), "").trim();
				getLogger().debug("after remove {} : {}", Authorization.BASIC.getValue(), authorizeCode);
				
				SecurityUtil secUtil = new SecurityUtil();
				
				//decode base64
				String dec = secUtil.decodeBase64(authorizeCode);
				getLogger().debug("after decodeBase64 : {}", dec);
				if(dec != null && !dec.isEmpty()) {
					String[] dataHeader = dec.split(":", -1);
					
					if(1 == dataHeader.length) {
						authorizeReq = new Authorize();
						authorizeReq.setClientId(dataHeader[0]);
					} else if(2 == dataHeader.length) {
						authorizeReq = new Authorize();
						authorizeReq.setClientId(dataHeader[0]);
						authorizeReq.setSecret(dataHeader[1]);
					} else if(3 == dataHeader.length) {
						authorizeReq = new Authorize();
						authorizeReq.setClientId(dataHeader[0]);
						authorizeReq.setSecret(dataHeader[1]);
						authorizeReq.setGrantType(dataHeader[2]);
					}
				}
			}
		} catch (Exception e) {
			getLogger().error("", e);
			throw new AuthorizationException(DefaultExceptionMessage.AUTHORIZATION);
		}
		return authorizeReq;
	}
}
