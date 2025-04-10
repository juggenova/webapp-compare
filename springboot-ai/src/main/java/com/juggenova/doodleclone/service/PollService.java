package com.juggenova.doodleclone.service;

import com.juggenova.doodleclone.model.ChoiceEnum;
import com.juggenova.doodleclone.model.Poll;
import com.juggenova.doodleclone.model.Vote;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Service
public class PollService {

    // Hardcoded poll for the first week of November 2024
    private final Poll defaultPoll;
    private final List<Vote> defaultVotes;
    private static final Long DEFAULT_POLL_ID = 1L;

    public PollService() {
        // Create the default poll
        defaultPoll = new Poll(
            DEFAULT_POLL_ID,
            "JUG Genova Meeting",
            "Choose the best day for our next meeting in November 2024",
            LocalDate.of(2024, Month.NOVEMBER, 1),
            LocalDate.of(2024, Month.NOVEMBER, 7),
            LocalDateTime.of(2024, Month.OCTOBER, 31, 23, 59)
        );

        // Create default votes for each day (all set to NO by default)
        defaultVotes = new ArrayList<>();
        long voteId = 1L;
        
        for (LocalDate day = defaultPoll.getStartDay(); 
             !day.isAfter(defaultPoll.getEndDay()); 
             day = day.plusDays(1)) {
            
            defaultVotes.add(new Vote(voteId++, DEFAULT_POLL_ID, day, ChoiceEnum.NO));
        }
    }

    /**
     * Get the default poll
     */
    public Poll getDefaultPoll() {
        return defaultPoll;
    }

    /**
     * Get all votes for the default poll
     */
    public List<Vote> getDefaultVotes() {
        return defaultVotes;
    }

    /**
     * Update a vote
     */
    public Vote updateVote(Long voteId, ChoiceEnum choice) {
        for (Vote vote : defaultVotes) {
            if (vote.getId().equals(voteId)) {
                vote.setChoice(choice);
                return vote;
            }
        }
        return null;
    }
}
