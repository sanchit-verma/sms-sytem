package com.meesho.smssystem.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meesho.smssystem.constants.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.TimeZone;

@Configuration
public class AppConfig
{
    @Value("${sms.gateway.timeout}")
    private int SMSGatewayTimeout; // default terms

    @Value(Constants.datePattern)
    private String datePattern;


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder)
    {
        return restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(SMSGatewayTimeout))
           .setReadTimeout(Duration.ofSeconds(SMSGatewayTimeout))
           .build();
    }

    @Bean(name = "pojoMapper")
    public ObjectMapper pojoMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
        DateFormat dateFormat = new SimpleDateFormat(datePattern);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        mapper.setDateFormat(dateFormat);
        return mapper;
    }

}