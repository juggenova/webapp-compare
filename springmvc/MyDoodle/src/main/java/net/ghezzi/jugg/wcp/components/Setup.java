package net.ghezzi.jugg.wcp.components;

import org.springframework.stereotype.Component;

import net.ghezzi.jugg.wcp.persistence.entity.UserProfile;

import net.yadaframework.security.components.YadaUserSetup;

@Component
public class Setup extends YadaUserSetup<UserProfile> {
	// By default this class is only here to call the superclass methods.
	// You can customize it with any action required at startup
	// by implementing the setupAppliction() method:
	
	// @Override
	// protected void setupApplication() {
	// }

}

