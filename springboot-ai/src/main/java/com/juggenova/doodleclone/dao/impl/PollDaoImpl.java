package com.juggenova.doodleclone.dao.impl;

import com.juggenova.doodleclone.dao.PollDao;
import com.juggenova.doodleclone.model.Poll;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class PollDaoImpl implements PollDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Poll> findById(Long id) {
        Poll poll = entityManager.find(Poll.class, id);
        return Optional.ofNullable(poll);
    }

    @Override
    public List<Poll> findAll() {
        TypedQuery<Poll> query = entityManager.createQuery(
                "SELECT p FROM Poll p ORDER BY p.id", Poll.class);
        return query.getResultList();
    }

    @Override
    public Poll save(Poll poll) {
        if (poll.getId() == null) {
            entityManager.persist(poll);
            return poll;
        } else {
            return entityManager.merge(poll);
        }
    }

    @Override
    public void updateChosenDay(Long pollId, LocalDate chosenDay) {
        entityManager.createQuery(
                "UPDATE Poll p SET p.chosenDay = :chosenDay WHERE p.id = :pollId")
                .setParameter("chosenDay", chosenDay)
                .setParameter("pollId", pollId)
                .executeUpdate();
    }

    @Override
    public List<Poll> findPollsWithExpiredDeadline() {
        TypedQuery<Poll> query = entityManager.createQuery(
                "SELECT p FROM Poll p WHERE p.deadline < :now AND p.chosenDay IS NULL",
                Poll.class);
        query.setParameter("now", LocalDateTime.now());
        return query.getResultList();
    }
}
