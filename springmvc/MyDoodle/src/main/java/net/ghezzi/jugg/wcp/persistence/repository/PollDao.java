package net.ghezzi.jugg.wcp.persistence.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import net.ghezzi.jugg.wcp.persistence.entity.Poll;

@Repository
@Transactional(readOnly = true) 
public class PollDao {

	@PersistenceContext EntityManager em;

	/**
	 * Cerca il Poll di default che è stato "cablato"
	 */
	public Poll findDefault() {
		return em.createQuery("SELECT e FROM Poll e", Poll.class).getSingleResult();
	}

	/**
	 * Conta i poll nel database
	 * @return il numero di poll trovati
	 */
	public long count() {
		return em.createQuery("SELECT COUNT(e) FROM Poll e", Long.class).getSingleResult();
	}
	
	/**
	 * Cerca un Poll
	 * @param pollId id del poll
	 * @return il poll trovato, oppure null
	 */
	public Poll find(long pollId) {
		Poll poll = em.find(Poll.class, pollId);
		return poll;
	}

    /**
     * Memorizza un Poll nuovo oppure esistente
     */
	@Transactional(readOnly = false)
    public Poll save(Poll entity) {
    	if (entity.getId()==null) {
    		em.persist(entity);
    		return entity;
    	}
    	return em.merge(entity);
    }	
}
