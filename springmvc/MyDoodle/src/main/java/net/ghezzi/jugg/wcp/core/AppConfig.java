package net.ghezzi.jugg.wcp.core;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import net.yadaframework.core.YadaAppConfig;
import net.yadaframework.core.YadaConfiguration;

//@Configuration not needed when using WebApplicationInitializer.java
// WARNING: do not add the "web" package here ("net.ghezzi.jugg.wcp.web"), or Spring will fail to find the YadaWebSecurity controllers for autowiring
//          Just set it in the WebConfig class, not here.
@ComponentScan(basePackages = { "net.ghezzi.jugg.wcp.components", "net.ghezzi.jugg.wcp.persistence.entity", "net.ghezzi.jugg.wcp.persistence.repository" })
public class AppConfig extends YadaAppConfig {
	
	/**
	 * Create a "config" bean
	 * @return
	 * @throws ConfigurationException
	 */
	@Bean
	public WcpConfiguration config() throws ConfigurationException {
		// Reuse the static configuration ensuring that it is loaded already
		YadaConfiguration staticConfig = getStaticConfig();
		WcpConfiguration config = new WcpConfiguration();
		staticConfig.copyTo(config);
		// Replace the static config so we have just one instance around
		CONFIG = config;
		return config;
	}

}
