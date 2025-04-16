package com.juggenova.doodleclone.dao.impl;

import com.juggenova.doodleclone.dao.VoteDao;
import com.juggenova.doodleclone.model.Poll;
import com.juggenova.doodleclone.model.Vote;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class VoteDaoImpl implements VoteDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Vote> findById(Long id) {
        Vote vote = entityManager.find(Vote.class, id);
        return Optional.ofNullable(vote);
    }

    @Override
    public List<Vote> findByPollAndSessionId(Poll poll, String sessionId) {
        TypedQuery<Vote> query = entityManager.createQuery(
                "SELECT v FROM Vote v WHERE v.poll = :poll AND v.sessionId = :sessionId ORDER BY v.day",
                Vote.class);
        query.setParameter("poll", poll);
        query.setParameter("sessionId", sessionId);
        return query.getResultList();
    }

    @Override
    public Optional<Vote> findByPollAndSessionIdAndDay(Poll poll, String sessionId, LocalDate day) {
        TypedQuery<Vote> query = entityManager.createQuery(
                "SELECT v FROM Vote v WHERE v.poll = :poll AND v.sessionId = :sessionId AND v.day = :day",
                Vote.class);
        query.setParameter("poll", poll);
        query.setParameter("sessionId", sessionId);
        query.setParameter("day", day);
        
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Vote save(Vote vote) {
        if (vote.getId() == null) {
            entityManager.persist(vote);
            return vote;
        } else {
            return entityManager.merge(vote);
        }
    }

    @Override
    public LocalDate findMostVotedDay(Poll poll) {
        // Using native query for more complex SQL
        String sql = "SELECT day, " +
                "SUM(CASE choice " +
                "   WHEN 'NO' THEN 0 " +
                "   WHEN 'MAYBE' THEN 1 " +
                "   WHEN 'YES' THEN 2 " +
                "   ELSE 0 END) AS score " +
                "FROM votes " +
                "WHERE poll_id = :pollId " +
                "GROUP BY day " +
                "ORDER BY score DESC, day ASC " +
                "LIMIT 1";
        
        try {
            Object[] result = (Object[]) entityManager.createNativeQuery(sql)
                    .setParameter("pollId", poll.getId())
                    .getSingleResult();
            
            return ((java.sql.Date) result[0]).toLocalDate();
        } catch (NoResultException e) {
            return null;
        }
    }
}
