package com.bddinaction.chapter2.web.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;

import java.util.List;

@Configuration
public class WebMVCConfiguration extends DelegatingWebMvcConfiguration {

    @Bean
    MappingJackson2HttpMessageConverter jackson2HttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter(objectMapper());
    }

    @Bean
    Jackson2ObjectMapperFactoryBean jackson2ObjectMapperFactoryBean() {
        Jackson2ObjectMapperFactoryBean factoryBean = new Jackson2ObjectMapperFactoryBean();
        factoryBean.setIndentOutput(true);
        return factoryBean;
    }

    @Bean
    ObjectMapper objectMapper(){
        ObjectMapper objectMapper = jackson2ObjectMapperFactoryBean().getObject();
        objectMapper.registerModule(new JodaModule());
        return objectMapper;
    }

    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(jackson2HttpMessageConverter());
        super.addDefaultHttpMessageConverters(converters);
    }
}
