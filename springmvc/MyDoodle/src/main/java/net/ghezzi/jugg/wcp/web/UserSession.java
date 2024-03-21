package net.ghezzi.jugg.wcp.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import net.ghezzi.jugg.wcp.persistence.entity.UserProfile;

import net.yadaframework.security.web.YadaSession;

/**
 * User session
 */
@Component
@Scope(value="session", proxyMode=ScopedProxyMode.TARGET_CLASS) 
public class UserSession extends YadaSession<UserProfile> {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * Returns the current user name (the email address)
	 * @return
	 */
	public String getCurrentUsername() {
		try {
			return getCurrentUserProfile().getUserCredentials().getUsername();
		} catch (Exception e) {
			log.error("Can't retrieve current user name");
			return "unknown";
		}
	}
}
