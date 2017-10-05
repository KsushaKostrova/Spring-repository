package com.kostrova.store;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity(debug = true)
@Configuration
public class WebSecurity extends WebSecurityConfigurerAdapter {

	@Bean
	public UserDetailsService userDetailsService() {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(User.withUsername("manager").password("manager").roles("MANAGER").build());
		manager.createUser(User.withUsername("employee").password("employee").roles("EMPLOYEE").build());
		return manager;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable().authorizeRequests().antMatchers(HttpMethod.DELETE, "/good/**").hasRole("MANAGER")
				.antMatchers(HttpMethod.POST, "/good/**").hasRole("MANAGER").antMatchers(HttpMethod.GET, "/good/**")
				.access("hasRole('MANAGER') or hasRole('EMPLOYEE')").antMatchers(HttpMethod.PUT, "/good/**")
				.hasRole("MANAGER").antMatchers("/goods/").access("hasRole('MANAGER') or hasRole('EMPLOYEE')")
				.anyRequest().authenticated().and().httpBasic();
	}
}
