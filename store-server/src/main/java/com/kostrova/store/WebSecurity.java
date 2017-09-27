package com.kostrova.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class WebSecurity extends WebSecurityConfigurerAdapter {
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("manager").password("manager").roles("MANAGER");
		auth.inMemoryAuthentication().withUser("employee").password("employee").roles("EMPLOYEE");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/good/**").access("hasRole('ROLE_MANAGER')")
				.antMatchers("/goods/**").access("hasRole('ROLE_EMPLOYEE' OR hasRole('ROLE_MANAGER'))")
				.and().formLogin().defaultSuccessUrl("/", false);

	}
}
