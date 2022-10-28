package com.sit.pibics.oauth.webservice.v2.auth;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sit.pibics.oauth.common.CommonWS;
import com.sit.pibics.oauth.core.auth.v1.domain.Token;
import com.sit.pibics.oauth.core.auth.v1.service.AuthManager;
import com.sit.pibics.oauth.core.config.parameter.domain.ParameterConfig;
import com.sit.pibics.oauth.enums.Authorization;
import com.sit.pibics.oauth.exception.ANSICode;
import com.sit.pibics.oauth.exception.AuthenticateException;
import com.sit.pibics.oauth.util.response.ResponseMessageUtil;

import util.string.StringUtil;

@Path("/v2/auth")
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

			if(StringUtil.stringToNull(jsonRequest) != null) {
				String[] bodyArr = jsonRequest.split("&", -1);
				String grantType = bodyArr[0];	//REQUIRED ref. https://www.rfc-editor.org/rfc/rfc6749#section-3.3
				if(StringUtil.stringToNull(grantType) == null || !grantType.equalsIgnoreCase("grant_type=client_credentials")) {
					throw new AuthenticateException(ANSICode.INVALID_GR.getValue());
				}
				
				//OPTIONAL >> scope
			} else {
				throw new AuthenticateException(ANSICode.INVALID_REQ.getValue());
			}
			
			Token token = new Token();
			try {
				AuthManager authManager = new AuthManager(getLogger());
				authManager.getAccessToken(token, authorizationHeader);
			} catch (Exception e) {
				throw new AuthenticateException(ANSICode.INVALID_CLI.getValue());
			}
			
			// manage result
			response = responseMessageUtil.manageSuccessResponseStd(token.getAccessToken(), Authorization.BEARER.getValue(), ParameterConfig.getApplication().getTokenExpireTime());
		} catch (Exception e) {
			getLogger().error("", e);
			response = responseMessageUtil.manageExceptionStd(e);
		}
		
		return response;
	}
	
}
