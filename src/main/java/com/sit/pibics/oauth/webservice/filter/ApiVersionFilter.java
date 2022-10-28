package com.sit.pibics.oauth.webservice.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;

import org.apache.logging.log4j.Logger;

import util.log4j2.DefaultLogUtil;

public class ApiVersionFilter implements Filter {

	private Logger logger = DefaultLogUtil.INITIAL;
	
	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		getLogger().debug("ApiVersionFilter");

		HttpServletRequest httpRequest = ((HttpServletRequest) request);
		HttpServletResponse httpResponse = ((HttpServletResponse) response);

		getLogger().debug("METHOD : {}", httpRequest.getMethod());
		getLogger().debug("URI : {}", httpRequest.getRequestURI());

		if (httpRequest.getMethod().equalsIgnoreCase(HttpMethod.OPTIONS)) {
			getLogger().debug("SKIP: OPTION : {}", httpRequest.getRequestURI());
			return;
		}

		if (!checkPatternUri(httpRequest, httpResponse)) {
			chain.doFilter(request, response);
		} else {
			// 2019-04-26 Control API provide version
			if (!checkVersionProvide(httpRequest, httpResponse)) {
				return;
			} else {
				chain.doFilter(request, response);
			}
		}
	}

	private boolean checkPatternUri(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
		boolean isPattern = false;

		try {
			getLogger().debug("URI : {}", httpRequest.getRequestURI());

			String[] versionProvide = httpRequest.getRequestURI().split("/");
			if (versionProvide[3].indexOf("v") == 0) {
				isPattern = true;
			}
		} catch (Exception e) {
			getLogger().error("", e);
		}
		return isPattern;
	}

	private boolean checkVersionProvide(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
		boolean isProvide = false;

		try {
			getLogger().debug("URI : {}", httpRequest.getRequestURI());

			String[] versionProvide = httpRequest.getRequestURI().split("/");
			//isProvide = Arrays.stream(ParameterConfig.getParameter().getApplication().getVersionProvide()).anyMatch(versionProvide[3]::equals);
			isProvide = true;
			if (!isProvide) {
				// Abort
				deprecatedVersionResponse(httpRequest, httpResponse);
			}
		} catch (Exception e) {
			getLogger().error("", e);
		}
		return isProvide;
	}

	private void deprecatedVersionResponse(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		try {
			/*
			String msgCode = GlobalVariable.STATUS_ERROR;
			String msgDesc = ErrorCode.DEPRECATED_VERSION.getErrorDesc();

			CommonError commonError = new CommonError();
			commonError.setErrorCode("500");
			commonError.setErrorDesc(msgDesc);

			CommonResponse commonResponse = new CommonResponse();
			commonResponse.setData(null);
			commonResponse.setMessageCode(msgCode);
			commonResponse.setMessageDesc(msgDesc);
			commonResponse.setError(commonError);

			AppUtil util = new AppUtil(getLogger());
			String json = util.convertObjAsJsonString(commonResponse);

			httpResponse.setContentType("application/json");
			httpResponse.setStatus(HttpServletResponse.SC_OK);
			httpResponse.setCharacterEncoding("UTF-8");

			PrintWriter out = httpResponse.getWriter();
			out.print(json);
			out.flush();
			 */
		} catch (Exception e) {
			getLogger().error("", e);
		}
	}

	@Override
	public void destroy() {
		
	}

	public Logger getLogger() {
		return logger;
	}
}
