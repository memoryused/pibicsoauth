package com.sit.pibics.oauth.util.response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.ws.rs.core.Response;

import org.apache.logging.log4j.Logger;

import com.sit.pibics.oauth.common.CommonError;
import com.sit.pibics.oauth.common.CommonResponse;
import com.sit.pibics.oauth.common.RFC6749StandardError;
import com.sit.pibics.oauth.common.RFC6749StandardResponse;
import com.sit.pibics.oauth.core.auth.v1.domain.Token;
import com.sit.pibics.oauth.exception.CommonException;
import com.sit.pibics.oauth.exception.ErrorCode;
import com.sit.pibics.oauth.util.JsonUtil;

public class ResponseMessageUtil {
	private Logger logger;
	
	private Logger getLogger() {
		return logger;
	}
	
	public ResponseMessageUtil(Logger logger) {
		this.logger = logger;
	}
	
	private Response manageTokenSuccessResponse(Token token) throws Exception {
		getLogger().debug("");
		
		CommonResponse commonResponse = new CommonResponse();
		commonResponse.setMessageCode("00000");
		commonResponse.setMessageDesc("Success");
		commonResponse.setData(token.getAccessToken());

		String message = JsonUtil.convertObject2Json(commonResponse);
		return Response.status(Response.Status.OK)
				.header("access-control-expose-headers", "Authorization")
				//.header("Authorization", Hex.toHexString(EncryptionUtil.encryptSHA512(token.getEncrypt())))
				.entity(message)
				.build();
	}
	
	public Response manageSuccessResponse(Object data) throws Exception {
		getLogger().debug("");
		
		CommonResponse commonResponse = new CommonResponse();
		commonResponse.setMessageCode("00000");
		commonResponse.setMessageDesc("Success");
		commonResponse.setData(data);

		String message = JsonUtil.convertObject2Json(commonResponse);
		return Response.status(Response.Status.OK)
				.header("access-control-expose-headers", "Authorization")
				.entity(message)
				.build();
	}
	
	public Response manageSuccessResponseStd(Object data, String tokenType, long expire) throws Exception {
		getLogger().debug("");
		
		RFC6749StandardResponse resp = new RFC6749StandardResponse();
		resp.setAccessToken(data.toString());
		resp.setTokenType(tokenType);
		resp.setExpiresIn(expire*60);	//Change minute to second
		
		String message = JsonUtil.convertObject2Json(resp);
		return Response.status(Response.Status.OK)
				.header("access-control-expose-headers", "Authorization")
				.entity(message)
				.build();
	}
	
	public Response manageExceptionStd(Exception exception) {
		Response response = null;
		
		try {
			RFC6749StandardError errResp = new RFC6749StandardError();
			errResp.setError(exception.getMessage());
			String message = JsonUtil.convertObject2Json(errResp);
			response = Response.status(Response.Status.BAD_REQUEST).entity(message).build();
			
		} catch (Exception e) {
			getLogger().catching(e);
		}
		return response;
	}
	
	public Response manageException(Exception exception) {
		Response response = null;
		try {
			if (exception != null) {
				response = manageOtherException(exception);
			}
		} catch (Exception e) {
			getLogger().catching(e);
		}
		return response;
	}
	
	private Response manageAuthenticateException(Exception exception) {
		Response response = null;
		try {
			CommonException ex = (CommonException) exception;
			logging(ex);	// Create Logging Code and Logging Exception
			
			// Set Common Exception
			String loggingCode = ex.getErrorCode();
			if (ex.getLoggingCode() != null && !ex.getLoggingCode().isEmpty()) {
				loggingCode = ex.getLoggingCode();
			}
			
			CommonResponse commonResponse = new CommonResponse();
			commonResponse.setMessageCode(ex.getErrorCode());
			commonResponse.setMessageDesc(ex.getErrorDesc());
			
			CommonError commonError = new CommonError();
			commonError.setErrorCode(loggingCode);
			commonError.setErrorDesc(ex.getMessage());
			
			commonResponse.setError(commonError);
			
			String message = JsonUtil.convertObject2Json(commonResponse);
			response = Response.status(Response.Status.FORBIDDEN).entity(message).build();
			
		} catch (Exception e) {
			getLogger().catching(e);
		}
		return response;
	}
	
	private Response manageAuthorizationException(Exception exception) {
		Response response = null;
		try {
			CommonException ex = (CommonException) exception;
			logging(ex);	// Create Logging Code and Logging Exception
			
			// Set Common Exception
			String loggingCode = ex.getErrorCode();
			if (ex.getLoggingCode() != null && !ex.getLoggingCode().isEmpty()) {
				loggingCode = ex.getLoggingCode();
			}
			
			CommonResponse commonResponse = new CommonResponse();
			commonResponse.setMessageCode(ex.getErrorCode());
			commonResponse.setMessageDesc(ex.getErrorDesc());
			
			CommonError commonError = new CommonError();
			commonError.setErrorCode(loggingCode);
			commonError.setErrorDesc(ex.getMessage());
			
			commonResponse.setError(commonError);
			
			String message = JsonUtil.convertObject2Json(commonResponse);
			response = Response.status(Response.Status.UNAUTHORIZED).entity(message).build();
			
		} catch (Exception e) {
			getLogger().catching(e);
		}
		return response;
	}
	
	private Response manageCustomException(Exception exception) {
		Response response = null;
		CommonException ex = null;
		try {
			ex = (CommonException) exception;
		} catch (Exception e) {
			ex = new CommonException(exception);
		}
		
		try {
			logging(ex);	// Create Logging Code and Logging Exception
			
			CommonResponse commonResponse = new CommonResponse();
			commonResponse.setMessageCode(ex.getErrorCode());
			commonResponse.setMessageDesc(ex.getMessage());
			
			CommonError commonError = new CommonError();
			commonError.setErrorCode(ex.getErrorCode());
			commonError.setErrorDesc(ex.getMessage());
			
			commonResponse.setError(commonError);
			
			String message = JsonUtil.convertObject2Json(commonResponse);
			response = Response.status(Response.Status.OK).entity(message).build();
			
		} catch (Exception e) {
			getLogger().catching(e);
		}
		return response;
	}
	
	private Response manageOtherException(Exception exception) {
		Response response = null;
		CommonException ex = null;
		try {
			ex = (CommonException) exception;
		} catch (Exception e) {
			ex = new CommonException(exception);
		}
		
		try {
			logging(ex);	// Create Logging Code and Logging Exception
			
			CommonResponse commonResponse = new CommonResponse();
			commonResponse.setMessageCode(ErrorCode.DEFAULT.getErrorCode());
			commonResponse.setMessageDesc(ErrorCode.DEFAULT.getErrorDesc());
			
			CommonError commonError = new CommonError();
			commonError.setErrorCode(ex.getErrorCode());
			commonError.setErrorDesc(ex.getMessage());
			
			commonResponse.setError(commonError);
			
			String message = JsonUtil.convertObject2Json(commonResponse);
			response = Response.status(Response.Status.OK).entity(message).build();
			
		} catch (Exception e) {
			getLogger().catching(e);
		}
		return response;
	}
	
	private void logging(CommonException exception) {
		try {
			CommonException ex = null;
			try {
				ex = (CommonException) exception;
			} catch (Exception e) {
				ex = new CommonException(e);
			}
			
			String loggingCode = ex.getErrorCode() + "-" + getLoggingTime();
			exception.setLoggingCode(loggingCode);
			
			getLogger().error(loggingCode);
			getLogger().catching(exception);
			
		} catch (Exception e) {
			getLogger().catching(e);
		}
	}
	
	private String getLoggingTime() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyMMddHHmmssSSS");
		return LocalDateTime.now().format(format);
	}
	
}
