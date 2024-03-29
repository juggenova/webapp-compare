package net.ghezzi.jugg.wcp.persistence.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import net.yadaframework.security.persistence.entity.YadaUserProfile;
import net.yadaframework.web.YadaJsonDateTimeShortSerializer;

@Entity
public class UserProfile extends YadaUserProfile {
	private static final long serialVersionUID = 1L;

	@Column(length = 32)
	String uuid; // browser-generated id

	/**
	 * Used by DataTables
	 */
	@JsonProperty("DT_RowId")
	public String getDT_RowId() {
		return this.getClass().getSimpleName() + "#" + this.id; // UserProfile#142
	}

	@Transient
	public String getUserId() {
		return String.format("U%04d", id);
	}

	public Long getId() {
		return super.getId();
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(uuid).append("(");
		result.append(id).append(")");
		return result.toString();
	}

	@JsonProperty("email")
	@Transient
	public String getEmail() {
		return userCredentials.getUsername();
	}

	@JsonProperty("enabled")
	@Transient
	public Boolean getEnabled() {
		return userCredentials.isEnabled();
	}

	@JsonProperty("registration")
	@JsonSerialize(using = YadaJsonDateTimeShortSerializer.class)
	@Transient
	public Date getRegistrationDate() {
		return userCredentials.getCreationDate();
	}

	@JsonProperty("loginDate")
	@JsonSerialize(using = YadaJsonDateTimeShortSerializer.class)
	@Transient
	public Date getLoginDate() {
		return userCredentials.getLastSuccessfulLogin();
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
}
