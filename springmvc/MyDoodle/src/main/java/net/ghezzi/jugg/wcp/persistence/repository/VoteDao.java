package net.ghezzi.jugg.wcp.persistence.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import net.ghezzi.jugg.wcp.persistence.entity.Poll;
import net.ghezzi.jugg.wcp.persistence.entity.UserProfile;
import net.ghezzi.jugg.wcp.persistence.entity.Vote;
import net.yadaframework.persistence.YadaSql;

@Repository
@Transactional(readOnly = true) 
public class VoteDao {

	@PersistenceContext EntityManager em;
	
	/**
	 * Ritorna il Vote avente l'id specificato ma solo se appartiene allo userProfile
	 * @param userProfile
	 * @param voteId
	 * @return Vote se trovato, oppure null se non trovato
	 */
	public Vote findForUser(UserProfile userProfile, Long voteId) {
		YadaSql yadaSql = YadaSql.instance().selectFrom("select v from Vote v")
				.join("join v.voter u")
				.where("where u=:userProfile").and()
				.where("where v.id=:voteId").and()
				.setParameter("userProfile", userProfile)
				.setParameter("voteId", voteId);
		List<Vote> votes = yadaSql.query(em, Vote.class).getResultList();
		if (votes.isEmpty()) {
			return null;
		}
		return votes.get(0);
	}

	/**
	 * Ritorna la lista di voti per un poll di un dato user.
	 * @param userProfile l'utente attuale
	 * @param poll il poll di cui ritornare la lista
	 * @return la lista di voti
	 */
	public List<Vote> findVotes(UserProfile userProfile, Poll poll) {
		YadaSql yadaSql = YadaSql.instance().selectFrom("select v from Vote v")
			.join(poll!=null, "join v.poll p")
			.join("join v.voter u")
			.where(poll!=null, "where p=:poll").and()
			.where("where u=:userProfile").and()
			.orderBy("v.day asc")
			.setParameter("poll", poll)
			.setParameter("userProfile", userProfile);
		return yadaSql.query(em, Vote.class).getResultList();
	}
	

    /**
     * Memorizza un Poll nuovo oppure esistente
     */
	@Transactional(readOnly = false)
    public Vote save(Vote entity) {
    	if (entity.getId()==null) {
    		em.persist(entity);
    		return entity;
    	}
    	return em.merge(entity);
    }	
	
}
