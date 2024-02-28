package net.ghezzi.jugg.wcp.persistence.entity;

import jakarta.persistence.Entity;

import net.yadaframework.security.persistence.entity.YadaRegistrationRequest;

@Entity
public class WcpRegistrationRequest extends YadaRegistrationRequest {
	private static final long serialVersionUID = 1L;

	private String name;
	private String surname;
	private String username; // Used for antispam - the real username is the email field
	
	/**
	 * Newsletter flag: null = "unknown" because we didn't ask the user to choose
	 */
	private Boolean newsletterFlag = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Boolean getNewsletterFlag() {
		return newsletterFlag;
	}

	public void setNewsletterFlag(Boolean newsletterFlag) {
		this.newsletterFlag = newsletterFlag;
	}

}
