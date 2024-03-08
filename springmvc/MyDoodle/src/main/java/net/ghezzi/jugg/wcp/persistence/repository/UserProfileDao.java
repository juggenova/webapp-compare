package net.ghezzi.jugg.wcp.persistence.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import net.ghezzi.jugg.wcp.persistence.entity.UserProfile;
import net.yadaframework.security.persistence.repository.YadaUserProfileDao;

@Repository
@Transactional(readOnly = true)
public class UserProfileDao extends YadaUserProfileDao<UserProfile> {
	private final transient Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext private EntityManager em;

    private UserProfile system = null; // Cache for the System account

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
