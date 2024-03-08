package net.ghezzi.jugg.wcp.persistence.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
	
	@Column(columnDefinition="TIMESTAMP NULL")
	@Temporal(TemporalType.TIMESTAMP)
	private Date deadline;
	
	@Temporal(TemporalType.DATE)
	private Date startDay;
	
	@Temporal(TemporalType.DATE)
	private Date endDay;

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
	
}
