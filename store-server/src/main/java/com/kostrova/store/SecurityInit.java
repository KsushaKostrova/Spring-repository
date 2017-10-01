package com.kostrova.store;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SecurityInit extends AbstractSecurityWebApplicationInitializer {
	
		public SecurityInit() {
			super(WebSecurity.class);
		}
	
}