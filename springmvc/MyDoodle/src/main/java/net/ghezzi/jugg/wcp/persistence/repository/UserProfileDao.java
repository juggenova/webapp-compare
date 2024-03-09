package net.ghezzi.jugg.wcp.persistence.repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import net.ghezzi.jugg.wcp.persistence.entity.UserProfile;
import net.yadaframework.security.persistence.entity.YadaUserCredentials;
import net.yadaframework.security.persistence.repository.YadaUserProfileDao;

@Repository
@Transactional(readOnly = true)
public class UserProfileDao extends YadaUserProfileDao<UserProfile> {
	private final transient Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext private EntityManager em;

    private UserProfile system = null; // Cache for the System account
    
    /**
     * Per fase 4 ritorno un utente con un dato uuid creandolo se non esiste
     * @param uuid
     * @return
     */
    @Transactional(readOnly = false)
    public UserProfile ensureUser(String uuid) {
		UserProfile result; 
		List<UserProfile> found = em.createQuery("SELECT e FROM UserProfile e where e.uuid = :uuid", UserProfile.class)
			.setParameter("uuid", uuid)
			.getResultList();
		if (found.isEmpty()) {
			result = new UserProfile();
			result.setUuid(uuid);
			YadaUserCredentials yadaUserCredentials = new YadaUserCredentials();
			yadaUserCredentials.setUsername(uuid);
			yadaUserCredentials.setPassword("dummy");
			result.setUserCredentials(yadaUserCredentials);
			em.persist(result);
		} else {
			result = found.get(0);
		}
		return result;
	}

    /**
     *
     * @param userProfileId
     * @return UserProfile instance or null
     */
    public UserProfile find(long userProfileId) {
    	UserProfile userProfile = em.find(UserProfile.class, userProfileId);
    	return userProfile;
    }


}
