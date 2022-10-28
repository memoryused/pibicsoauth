package com.sit.pibics.oauth.webservice.filter;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;

import com.sit.pibics.oauth.core.config.parameter.domain.ParameterConfig;

import util.log4j2.DefaultLogUtil;

/**
 * Servlet Filter implementation class CORSFilter
 */
public class CorsFilter implements Filter {

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpRequest = ((HttpServletRequest) request);
		HttpServletResponse httpResponse = ((HttpServletResponse) response);

		List<String> headers = null;
		String origin = null;
		String host = null;
		boolean isValid = false;

		if (httpRequest.getRequestURI().equalsIgnoreCase(httpRequest.getContextPath() + "/rest/v1/auth/token")
				|| httpRequest.getRequestURI().equalsIgnoreCase(httpRequest.getContextPath() + "/rest/v2/auth/token")
				|| httpRequest.getRequestURI().equalsIgnoreCase(httpRequest.getContextPath() + "/index.html")
				) {
			httpResponse.addHeader("Access-Control-Allow-Origin", ParameterConfig.getSecure().getOrigin());
			httpResponse.addHeader("Access-Control-Allow-Headers", ParameterConfig.getSecure().getHeader());
			httpResponse.addHeader("Access-Control-Allow-Methods", ParameterConfig.getSecure().getMethod());
			httpResponse.addHeader("Access-Control-Max-Age", ParameterConfig.getSecure().getMaxage());
			httpResponse.addHeader("Access-Control-Allow-Credentials", "true");
			chain.doFilter(request, response);
			return;
		}
		
		/*
		 * Step 1 : Check that we have only one and non empty instance of the "Origin" header
		 */
		headers = CorsFilter.enumAsList(httpRequest.getHeaders("Origin"));
		if ((headers == null) || (headers.size() != 1)) {
			httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		origin = headers.get(0);
		
		/*
		 * Step 2 : Check that we have only one and non empty instance of the "Host" header
		 */
		headers = CorsFilter.enumAsList(httpRequest.getHeaders("Host"));
		if ((headers == null) || (headers.size() != 1)) {
			httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		host = headers.get(0);
			
		/* Step 3 : Perform analysis - Origin header is required */
		if ((origin != null) && !"".equals(origin.trim()) && (host != null) && !"".equals(host.trim())) {
			if (ParameterConfig.getSecure().getAllowedDomains().contains(origin) 
					&& ParameterConfig.getSecure().getHost().contains(host)) {
				// Check if origin is in allowed domain
				isValid = true;
				
			} else {
				isValid = false;
				// Add trace here
				// ....
			}
		}

		/* Step 4 : Finalize request next step */
		if (isValid) {
			// Authorize (allow) all domains to consume the content
			httpResponse.addHeader("Access-Control-Allow-Origin", origin);
			httpResponse.addHeader("Access-Control-Allow-Headers", ParameterConfig.getSecure().getHeader());
			httpResponse.addHeader("Access-Control-Allow-Methods", ParameterConfig.getSecure().getMethod());
			httpResponse.addHeader("Access-Control-Max-Age", ParameterConfig.getSecure().getMaxage());
			httpResponse.addHeader("Access-Control-Expose-Headers", "content-Disposition");
			httpResponse.addHeader("Access-Control-Allow-Credentials", "true");
			
			// Analysis OK then pass the request along the filter chain
			chain.doFilter(request, response);
			
		} else {
			// Return HTTP Error without any information about cause of the request reject !
			httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
		}
	}

	/**
	 * Convert a enumeration to a list
	 * 
	 * @param tmpEnum
	 *            Enumeration to convert
	 * @return list of string or null is input enumeration is null
	 */
	public static List<String> enumAsList(Enumeration<String> tmpEnum) {
		List<String> lst = null;
		
		if (tmpEnum != null) {
			lst = Collections.list(tmpEnum);
		}
		
		return lst;
	}

	@Override
	public void destroy() {
		getLogger().info("");
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		getLogger().info("");
	}
	
	public Logger getLogger() {
		return DefaultLogUtil.FILTER;
	}
}