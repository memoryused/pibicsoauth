package com.sit.pibics.oauth.webservice.v1.auth;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sit.pibics.oauth.common.CommonWS;
import com.sit.pibics.oauth.core.auth.v1.domain.Authorize;
import com.sit.pibics.oauth.core.auth.v1.domain.CredentialRequest;
import com.sit.pibics.oauth.core.auth.v1.domain.Token;
import com.sit.pibics.oauth.core.auth.v1.service.AuthManager;
import com.sit.pibics.oauth.util.JsonUtil;
import com.sit.pibics.oauth.util.response.ResponseMessageUtil;

@Path("/v1/auth")
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED })
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED })
public class CredentialsWS extends CommonWS {

	private static final String HEADER_KEY = "Authorization"; 
	private static final String HEADER_LOG_FORMAT = "{} : {}"; 
	
	@POST
	@Path("/token")
	public Response getAccessToken(@Context HttpServletRequest request, String jsonRequest) {
		getLogger().info("/token");
		
		Response response = null;
		ResponseMessageUtil responseMessageUtil = new ResponseMessageUtil(getLogger());
		
		try {						
			String authorizationHeader = request.getHeader(HEADER_KEY);
			getLogger().debug(HEADER_LOG_FORMAT, HEADER_KEY, authorizationHeader);

			Token token = new Token();
			AuthManager authManager = new AuthManager(getLogger());
			authManager.getAccessToken(token, authorizationHeader);
			
			// manage result
			response = responseMessageUtil.manageSuccessResponse(token.getAccessToken());
		} catch (Exception e) {
			getLogger().error("", e);
			response = responseMessageUtil.manageException(e);
		}
		
		return response;
	}
	
	@POST
	@Path("/checktoken")
	public Response checkAccessToken(@Context HttpServletRequest request, String jsonRequest) {
		getLogger().info("/checktoken");
		
		Response response = null;
		ResponseMessageUtil responseMessageUtil = new ResponseMessageUtil(getLogger());
		
		try {
			String authorizationHeader = request.getHeader(HEADER_KEY);
			getLogger().debug(HEADER_LOG_FORMAT, HEADER_KEY, authorizationHeader);
			
			// call manage business manager
			AuthManager authManager = new AuthManager(getLogger());
			Authorize authRes = authManager.checkAccessToken(authorizationHeader);
			
			response = responseMessageUtil.manageSuccessResponse(authRes.getClientId());
		} catch (Exception e) {
			getLogger().error("", e);
			response = responseMessageUtil.manageException(e);
		}
		return response;
	}
	
	@POST
	@Path("/createcredential")
	public Response createCredential(@Context HttpServletRequest request, String jsonRequest) {
		getLogger().info("/createcredential");
		
		Response response = null;
		ResponseMessageUtil responseMessageUtil = new ResponseMessageUtil(getLogger());
		
		try {						
			String authorizationHeader = request.getHeader(HEADER_KEY);
			getLogger().debug(HEADER_LOG_FORMAT, HEADER_KEY, authorizationHeader);
			
			CredentialRequest credentialRequest = (CredentialRequest) JsonUtil.convertJson2Object(jsonRequest, CredentialRequest.class);
			if(credentialRequest != null) {
				getLogger().debug("id : {}", credentialRequest.getId());
				getLogger().debug("systemName : {}", credentialRequest.getSystemName());
				getLogger().debug("clientName : {}", credentialRequest.getClientName());
				getLogger().debug("grantType : {}", credentialRequest.getGrantType());
			}
			
			// Create Credential
			AuthManager authManager = new AuthManager(getLogger());
			Authorize authorizeRes = authManager.createCredential(authorizationHeader, credentialRequest);
			
			// manage result
			response = responseMessageUtil.manageSuccessResponse(authorizeRes);
		} catch (Exception e) {
			getLogger().error("", e);
			response = responseMessageUtil.manageException(e);
		}
		
		return response;
	}
	
	@POST
	@Path("/cancelauthorize")
	public Response cancelAuthorize(@Context HttpServletRequest request, String jsonRequest) {
		getLogger().info("/cancelAuthorize");
		
		Response response = null;
		ResponseMessageUtil responseMessageUtil = new ResponseMessageUtil(getLogger());
		
		try {						
			String authorizationHeader = request.getHeader(HEADER_KEY);
			getLogger().debug(HEADER_LOG_FORMAT, HEADER_KEY, authorizationHeader);
	
			// Cancel Authorize
			AuthManager authManager = new AuthManager(getLogger());
			authManager.cancelAuthorize(authorizationHeader);
			
			// manage result
			response = responseMessageUtil.manageSuccessResponse(null);
		} catch (Exception e) {
			getLogger().error("", e);
			response = responseMessageUtil.manageException(e);
		}
		
		return response;
	}
}
