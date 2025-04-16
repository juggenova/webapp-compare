package com.juggenova.doodleclone.dao;

import com.juggenova.doodleclone.model.Poll;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Poll entities
 */
public interface PollDao {
    
    /**
     * Find a poll by its ID
     */
    Optional<Poll> findById(Long id);
    
    /**
     * Find all polls
     */
    List<Poll> findAll();
    
    /**
     * Save a poll
     */
    Poll save(Poll poll);
    
    /**
     * Update the chosen day for a poll
     */
    void updateChosenDay(Long pollId, LocalDate chosenDay);
    
    /**
     * Find polls that have reached their deadline but don't have a chosen day yet
     */
    List<Poll> findPollsWithExpiredDeadline();
}
