package com.kostrova.store;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@ComponentScan("com.kostrova.store")
public class WebAppConfig extends WebMvcConfigurerAdapter {
	
    @Bean
	public GoodService goodService() {
		return new GoodServiceImpl();		
	}
	
	@Bean
	public IGoodDao goodRepository() {
		return new GoodDaoImpl();
	}
}