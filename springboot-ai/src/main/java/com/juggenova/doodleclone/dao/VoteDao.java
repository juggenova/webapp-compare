package com.juggenova.doodleclone.dao;

import com.juggenova.doodleclone.model.Poll;
import com.juggenova.doodleclone.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Vote entities
 */
public interface VoteDao {
    
    /**
     * Find a vote by its ID
     */
    Optional<Vote> findById(Long id);
    
    /**
     * Find all votes for a poll and session
     */
    List<Vote> findByPollAndSessionId(Poll poll, String sessionId);
    
    /**
     * Find a specific vote by poll, session, and day
     */
    Optional<Vote> findByPollAndSessionIdAndDay(Poll poll, String sessionId, LocalDate day);
    
    /**
     * Save a vote
     */
    Vote save(Vote vote);
    
    /**
     * Find the most voted day for a poll
     */
    LocalDate findMostVotedDay(Poll poll);
}
