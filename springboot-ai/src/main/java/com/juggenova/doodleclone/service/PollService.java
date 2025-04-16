package com.juggenova.doodleclone.service;

import com.juggenova.doodleclone.dao.PollDao;
import com.juggenova.doodleclone.dao.VoteDao;
import com.juggenova.doodleclone.model.ChoiceEnum;
import com.juggenova.doodleclone.model.Poll;
import com.juggenova.doodleclone.model.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PollService {

    private final PollDao pollDao;
    private final VoteDao voteDao;

    @Autowired
    public PollService(PollDao pollDao, VoteDao voteDao) {
        this.pollDao = pollDao;
        this.voteDao = voteDao;
    }

    /**
     * Get the default poll (first poll in the database)
     */
    public Poll getDefaultPoll() {
        return pollDao.findAll().stream().findFirst().orElse(null);
    }

    /**
     * Get all votes for a session
     */
    @Transactional
    public List<Vote> getVotesForSession(String sessionId) {
        Poll poll = getDefaultPoll();
        if (poll == null) {
            return Collections.emptyList();
        }

        List<Vote> votes = voteDao.findByPollAndSessionId(poll, sessionId);

        // If no votes exist for this session, create default votes
        if (votes.isEmpty()) {
            votes = createDefaultVotes(poll, sessionId);
        }

        return votes;
    }

    /**
     * Create default votes for a session
     */
    private List<Vote> createDefaultVotes(Poll poll, String sessionId) {
        List<Vote> votes = new ArrayList<>();

        for (LocalDate day = poll.getStartDay();
             !day.isAfter(poll.getEndDay());
             day = day.plusDays(1)) {

            Vote vote = new Vote();
            vote.setPoll(poll);
            vote.setDay(day);
            vote.setChoice(ChoiceEnum.NO); // Default is NO
            vote.setSessionId(sessionId);

            voteDao.save(vote);
            votes.add(vote);
        }

        return votes;
    }

    /**
     * Update a vote
     */
    @Transactional
    public Vote updateVote(Long voteId, ChoiceEnum choice, String sessionId) {
        Optional<Vote> optionalVote = voteDao.findById(voteId);

        if (optionalVote.isPresent()) {
            Vote vote = optionalVote.get();

            // Only allow updating if the vote belongs to the session
            if (vote.getSessionId().equals(sessionId)) {
                vote.setChoice(choice);
                return voteDao.save(vote);
            }
        }

        return null;
    }

    /**
     * Get the most voted day for a poll
     */
    @Transactional(readOnly = true)
    public LocalDate getMostVotedDay(Poll poll) {
        return voteDao.findMostVotedDay(poll);
    }

    /**
     * Process polls with expired deadlines
     */
    @Transactional
    public void processPollsWithExpiredDeadlines() {
        List<Poll> expiredPolls = pollDao.findPollsWithExpiredDeadline();

        for (Poll poll : expiredPolls) {
            LocalDate mostVotedDay = getMostVotedDay(poll);
            if (mostVotedDay != null) {
                pollDao.updateChosenDay(poll.getId(), mostVotedDay);
            }
        }
    }
}
