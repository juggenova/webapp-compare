package net.ghezzi.jugg.wcp.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import net.yadaframework.security.YadaSecurityConfig;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Security configuration.
 * You only need to set the roles for the application paths.
 * Other configuration is set on YadaSecurityConfig
 * @see YadaSecurityConfig
 */
@Configuration
@EnableWebSecurity
//@DependsOn("webConfig") // Fix circular reference on 'mvcContentNegotiationManager' creation
public class SecurityConfig extends YadaSecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		//	The mapping matches URLs using the following rules:
		//		? matches one character
		//		* matches zero or more characters
		//		** matches zero or more 'directories' in a path
		//	Patterns which end with /** (and have no other wildcards) are optimized by using a substring match
        http.authorizeHttpRequests(authorize -> authorize
			.requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("USER")
			.requestMatchers(new AntPathRequestMatcher("/user/**")).hasRole("USER")
		);

        super.configure(http);
        super.successHandler.setDefaultTargetUrlNormalRequest("/user/poll");
        // anyRequest().permitAll() mapping must be the last one
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
        return http.build();
    }
}
