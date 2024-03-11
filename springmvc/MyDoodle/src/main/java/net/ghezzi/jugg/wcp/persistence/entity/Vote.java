package net.ghezzi.jugg.wcp.persistence.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;

/**
 * Rappresenta il voto di un utente per un certo giorno di un dato poll.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"poll_id", "voter_id", "day"}))
public class Vote {
	
	@Version
	protected long version;

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	protected Long id;
	
	@ManyToOne(optional=false) // Decido di non mappare la inverse da Poll a Vote
	private Poll poll;
	
	@ManyToOne(optional=false) // Decido di non mappare la inverse da Poll a Vote
	private UserProfile voter;
	
	@Temporal(TemporalType.DATE)
	private Date day;
	
	private ChoiceEnum choice;

	public Poll getPoll() {
		return poll;
	}

	public void setPoll(Poll poll) {
		this.poll = poll;
	}

	public UserProfile getVoter() {
		return voter;
	}

	public void setVoter(UserProfile voter) {
		this.voter = voter;
	}

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public ChoiceEnum getChoice() {
		return choice;
	}

	public void setChoice(ChoiceEnum choice) {
		this.choice = choice;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
