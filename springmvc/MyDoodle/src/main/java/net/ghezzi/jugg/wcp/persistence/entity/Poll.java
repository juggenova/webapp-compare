package net.ghezzi.jugg.wcp.persistence.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;

/**
 * Classe che rappresenta un "doodle".
 */
@Entity
public class Poll {
	
	@Version
	protected long version;

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	protected Long id;
	
	@Column(length = 32)
	private String title;

	@Column(length = 256)
	private String description;
	
	/**
	 * Giorno entro il quale gli utenti devono esprimere una preferenza
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm")
	@Column(columnDefinition="TIMESTAMP NULL")
	@Temporal(TemporalType.TIMESTAMP)
	private Date deadline;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date startDay;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date endDay;

	/**
	 * Giorno scelto dall'algoritmo in base alle preferenze espresse dagli utenti
	 */
	@Temporal(TemporalType.DATE)
	private Date chosenDay;
	
	@ManyToOne
	private UserProfile owner;
	
	@Transient
	public boolean isClosed() {
		return chosenDay!=null;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public Date getStartDay() {
		return startDay;
	}

	public void setStartDay(Date startDay) {
		this.startDay = startDay;
	}

	public Date getEndDay() {
		return endDay;
	}

	public void setEndDay(Date endDay) {
		this.endDay = endDay;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getChosenDay() {
		return chosenDay;
	}

	public void setChosenDay(Date chosenDay) {
		this.chosenDay = chosenDay;
	}
	
	public String toString() {
		return title + "(" + id + ")";
	}

	public UserProfile getOwner() {
		return owner;
	}

	public void setOwner(UserProfile owner) {
		this.owner = owner;
	}
	
}
