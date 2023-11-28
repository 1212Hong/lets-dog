// package com.ssafy.dog.common.config;
//
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
//
// import com.fasterxml.jackson.databind.DeserializationFeature;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.databind.PropertyNamingStrategies;
// import com.fasterxml.jackson.databind.SerializationFeature;
// import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//
// @Configuration
// public class ObjectMapperConfig {
//
// 	@Bean
// 	public ObjectMapper objectMapper() {
// 		ObjectMapper objectMapper = new ObjectMapper();
// 		objectMapper.registerModule(new JavaTimeModule());
//
// 		objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
// 		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//
// 		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//
// 		objectMapper.setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());
//
// 		return objectMapper;
// 	}
// }