package net.ghezzi.jugg.wcp.core;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import net.yadaframework.core.YadaAppConfig;

//@Configuration not needed when using WebApplicationInitializer.java
// WARNING: do not add the "web" package here ("net.ghezzi.jugg.wcp.web"), or Spring will fail to find the YadaWebSecurity controllers for autowiring
//          Just set it in the WebConfig class, not here.
@ComponentScan(basePackages = { "net.ghezzi.jugg.wcp.components", "net.ghezzi.jugg.wcp.persistence.entity", "net.ghezzi.jugg.wcp.persistence.repository" })
public class AppConfig extends YadaAppConfig {
	
	/**
	 * Creo il bean "config" in modo da poterlo usare in pagina con @config.xy invece di @wcpConfiguration.xy
	 */
	@Bean
	public WcpConfiguration config() throws ConfigurationException {
		WcpConfiguration config = new WcpConfiguration();
		super.makeCombinedConfiguration(config);
		return config;
	}

}
