package com.sit.pibics.oauth.core.auth.v1.service;

import java.io.File;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;

import org.apache.logging.log4j.Logger;

import com.sit.core.common.service.CommonManager;
import com.sit.pibics.oauth.core.auth.v1.domain.Authorize;
import com.sit.pibics.oauth.core.auth.v1.domain.CredentialRequest;
import com.sit.pibics.oauth.core.auth.v1.domain.Token;
import com.sit.pibics.oauth.core.config.parameter.domain.DBLookup;
import com.sit.pibics.oauth.core.config.parameter.domain.ParameterConfig;
import com.sit.pibics.oauth.enums.Authorization;
import com.sit.pibics.oauth.exception.AuthenticateException;
import com.sit.pibics.oauth.exception.AuthorizationException;
import com.sit.pibics.oauth.exception.DefaultExceptionMessage;
import com.sit.pibics.oauth.exception.TokenExpireException;
import com.sit.pibics.oauth.exception.TokenValidateException;
import com.sit.pibics.oauth.util.PibicsOAuthUtil;
import com.sit.pibics.oauth.util.auth.Payload;
import com.sit.pibics.oauth.util.auth.SecurityUtil;
import com.sit.pibics.oauth.util.database.CCTConnectionProvider;

import util.cryptography.PKIUtil;
import util.database.connection.CCTConnection;
import util.database.connection.CCTConnectionUtil;

public class AuthManager extends CommonManager {
	private AuthService service = null;
	
	public AuthManager(Logger logger) {
		super(logger);
		this.service = new AuthService(logger);
	}

	/**
	 * Get Access Token
	 * @param token
	 * @param authorizationHeader
	 * @throws Exception
	 */
	public void getAccessToken(Token token, String authorizationHeader) throws Exception {
		getLogger().info("");
		
		CCTConnection conn = null;
		
		try {
			//client_id:secret_key:grant_type
			Authorize authorizeReq = service.getAuthorizeCodeBasic(authorizationHeader);
			
			if( authorizeReq != null) {
				getLogger().debug("clientId : {}", authorizeReq.getClientId());
				getLogger().debug("secret : {}", authorizeReq.getSecret());
				getLogger().debug("grantType : {}", authorizeReq.getGrantType());
			}
			
			if( authorizeReq == null || 
					!PibicsOAuthUtil.hasValue(authorizeReq.getClientId()) || 
					!PibicsOAuthUtil.hasValue(authorizeReq.getSecret()) ) {
				throw new AuthenticateException(DefaultExceptionMessage.AUTHENTICATION);
			}
			
			conn = new CCTConnectionProvider().getConnection(conn, DBLookup.pibicsoauth.getLookup());
			
			// Check validate
			Authorize authorize = service.validateGetAccessTokenRequest(conn, authorizeReq);

			// จัดการส่วน Token
			managerAccessToken(token, authorize.getSalt(), authorize.getSecret(), authorize.getEncData());
			
			// บันทึกค่า token _SQL 
			service.updateToken(conn, token.getAccessToken(), authorize.getAuthorizeKey());
		} catch (Exception e) {
			getLogger().error("");
			throw e;
		} finally {
			CCTConnectionUtil.close(conn);
		}
	}
	
	/**
	 * Check Access Token
	 * @param accessToken
	 * @param token
	 * @throws Exception
	 */
	public Authorize checkAccessToken(String authorizationHeader) throws Exception {
		getLogger().info("");
		
		CCTConnection conn = null;
		Authorize auth = null;
		
		try {
			// Check validate
			if(authorizationHeader == null || authorizationHeader.isBlank()) {
				throw new AuthenticateException(DefaultExceptionMessage.AUTHENTICATION);
			}
			
			String accessToken = authorizationHeader.replace(Authorization.BEARER.getValue(), "").trim();
			getLogger().debug("access token after remove {} : {}", Authorization.BEARER.getValue(), accessToken);
			
			// Validate token
			PublicKey publicKey = PKIUtil.getPublicKey(new File(ParameterConfig.getCertification().getPublicKeyFile()));
			
			SecurityUtil secUtil = new SecurityUtil();
			String validFlag = secUtil.validateAccessToken((RSAPublicKey) publicKey, accessToken);
			
			getLogger().debug("Token Validate: {}", validFlag);
			if (validFlag.equals("V")) {
				throw new TokenValidateException(DefaultExceptionMessage.TOKEN_VALIDATE);
			} else if (validFlag.equals("E")) {
				throw new TokenExpireException(DefaultExceptionMessage.TOKEN_EXPIRE);
			}
			
			// Get payload from token (JWT) 
			Payload payload = secUtil.getPayloadFromToken(accessToken);
			getLogger().debug("ID: {}", payload.getId());
			getLogger().debug("Exp: {}", payload.getExp());
			
			//search by id 
			conn = new CCTConnectionProvider().getConnection(conn, DBLookup.pibicsoauth.getLookup());
			auth = service.searchAuthorizeByEncryptData(conn, payload.getId());
			if (auth == null) {
				throw new AuthorizationException(DefaultExceptionMessage.AUTHORIZATION);
			}

		} catch (Exception e) {
			getLogger().error("");
			throw e;
		} finally {
			CCTConnectionUtil.close(conn);
		}
		return auth;
	}
	
	/**
	 * Create Credential
	 * @param authorizationHeader
	 * @return
	 * @throws Exception
	 */
	public Authorize createCredential(String authorizationHeader, CredentialRequest credentialRequest) throws Exception {
		getLogger().info("");
		
		CCTConnection conn = null;
		Authorize authorizeRes = null;
		
		try {
			//client_id:id
			Authorize authorizeReq = service.getAuthorizeCodeBasic(authorizationHeader);
			
			if(authorizeReq == null || !PibicsOAuthUtil.hasValue(authorizeReq.getClientId())) {
				throw new AuthenticateException(DefaultExceptionMessage.AUTHENTICATION);
			}
			
			conn = new CCTConnectionProvider().getConnection(conn, DBLookup.pibicsoauth.getLookup());
			
			// Validate header
			service.validateHeaderCreateCredentialRequest(conn, authorizeReq.getClientId());
				
			// Validate body
			service.validateDataCreateCredentialRequest(credentialRequest);
			
			// Manage Credential
			authorizeRes = service.manageCredential(conn, credentialRequest);
		} catch (Exception e) {
			getLogger().error("");
			throw e;
		} finally {
			CCTConnectionUtil.close(conn);
		}
		return authorizeRes;
	}

	/**
	 * Cancel Authorize
	 * @param authorizationHeader
	 * @return
	 * @throws Exception
	 */
	public String cancelAuthorize(String authorizationHeader) throws Exception {
		getLogger().info("");
		
		CCTConnection conn = null;
		
		try {
			//get authorizeKey
			Authorize authorizeReq = service.getAuthorizeCodeBasic(authorizationHeader);
			
			if(authorizeReq == null) {
				throw new AuthenticateException(DefaultExceptionMessage.AUTHENTICATION);
			}
			
			// Validate header
			conn = new CCTConnectionProvider().getConnection(conn, DBLookup.pibicsoauth.getLookup());
			service.validateHeaderCancelAuthorizeRequest(conn, authorizeReq.getClientId());
			
			// บันทึกยกเลิกใช้งาน_SQL
			service.updateCancelAuthorize(conn, authorizeReq.getClientId());
			
		} catch (Exception e) {
			getLogger().error("");
			throw e;
		} finally {
			CCTConnectionUtil.close(conn);
		}
		return null;
	}
	
	
	//*************************************************************************************************
	
	/**
	 * Generate AccessToken
	 * @param token
	 * @param salt
	 * @param secret
	 * @param empId
	 * @throws Exception
	 */
	private void managerAccessToken(Token token, String salt, String secret, String empId) throws Exception {
		getLogger().info("");
		
		try {
			Payload payload = new Payload();
			payload.setId(empId);
			
			token.setEncrypt(empId);
			token.setSecret(secret);
			token.setSalt(salt);
			
			SecurityUtil secUtil = new SecurityUtil();
			long tokenExpire = secUtil.getAccessTokenExpireMillis(ParameterConfig.getApplication().getTokenExpireTime());
			token.setAccessToken(secUtil.genAccessToken(ParameterConfig.getCertification(), payload, tokenExpire));

		} catch (Exception e) {
			getLogger().error("");
			throw e;
		}
	} 
}
