package net.ghezzi.jugg.wcp.persistence.repository;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import net.ghezzi.jugg.wcp.persistence.entity.Poll;
import net.ghezzi.jugg.wcp.persistence.entity.UserProfile;

@Repository
@Transactional(readOnly = true) 
public class PollDao {

	@PersistenceContext EntityManager em;

	/**
	 * Ritorna tutti i votanti di un dato poll
	 * @param poll
	 */
	public List<UserProfile> findVoters(Poll poll) {
		String sql = "select distinct up from Vote v join v.voter up join v.poll p where p.id=:pid";
		return em.createQuery(sql, UserProfile.class)
			.setParameter("pid", poll.getId())
			.getResultList();
	}
	
	public Date getPollResult(Poll poll) {
		String sql = "SELECT day, " +
		"SUM(CASE choice " +
		"	WHEN 0 THEN 0 " +
		"	WHEN 2 THEN 1 " +
		"	WHEN 1 THEN 2 " +
		"	ELSE 0 END) AS score " +
		"FROM Vote " +
		"where poll_id = :pollId " +
		"GROUP BY day " +
		"order by score desc, day";
		Object result = em.createNativeQuery(sql)
			.setMaxResults(1)
			.setParameter("pollId", poll.getId())
			.getSingleResult();
		return (Date) ((Object[]) result)[0];
	}
	
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
