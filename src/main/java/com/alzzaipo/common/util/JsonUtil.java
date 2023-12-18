package com.alzzaipo.common.util;

import com.alzzaipo.common.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JsonUtil {

	private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

	public static String toJson(Object obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			log.error("Json Serialization Error : {}", e.getMessage());
			throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "");
		}
	}

	public static <T> T fromJson(String json, Class<T> clazz) {
		try {
			return objectMapper.readValue(json, clazz);
		} catch (Exception e) {
			log.error("Json Parsing Error : {}", e.getMessage());
			throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "");
		}
	}

}
