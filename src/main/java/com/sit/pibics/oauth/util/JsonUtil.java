package com.sit.pibics.oauth.util;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
	
	public static String convertObject2Json(Object obj) throws JsonProcessingException {
		String jsonString = "";
		
		jsonString = new ObjectMapper()
				.setSerializationInclusion(Include.NON_NULL)
				.writeValueAsString(obj);
		
		return jsonString;
	}
	
	public static Object convertJson2Object(String jsonString, Class<?> clazz) throws JsonProcessingException, IOException {
		Object obj = null;
		
		obj = new ObjectMapper()
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.readValue(jsonString, clazz);
					
		return obj;
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
