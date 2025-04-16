package com.juggenova.doodleclone.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.juggenova.doodleclone.model.ChoiceEnum;
import com.juggenova.doodleclone.model.Poll;
import com.juggenova.doodleclone.model.Vote;
import com.juggenova.doodleclone.service.PollService;

@RestController
@RequestMapping("/api/polls")
@CrossOrigin(origins = "http://localhost:3000") // Allow requests from React dev server
public class PollController {

    private final PollService pollService;

    @Autowired
    public PollController(PollService pollService) {
        this.pollService = pollService;
    }

    /**
     * Get the default poll with its votes
     */
    @GetMapping("/default")
    public ResponseEntity<Map<String, Object>> getDefaultPoll(HttpServletRequest request) {
        // Get or create a session ID
        String sessionId = getOrCreateSessionId(request);

        Poll poll = pollService.getDefaultPoll();
        List<Vote> votes = pollService.getVotesForSession(sessionId);

        Map<String, Object> response = new HashMap<>();
        response.put("poll", poll);
        response.put("votes", votes);

        return ResponseEntity.ok(response);
    }

    /**
     * Update a vote
     */
    @PostMapping("/votes/{voteId}")
    public ResponseEntity<Vote> updateVote(
            @PathVariable Long voteId,
            @RequestParam ChoiceEnum choice,
            HttpServletRequest request) {

        String sessionId = getOrCreateSessionId(request);
        Vote updatedVote = pollService.updateVote(voteId, choice, sessionId);

        if (updatedVote != null) {
            return ResponseEntity.ok(updatedVote);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get or create a session ID for the current user
     */
    private String getOrCreateSessionId(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        String sessionId = (String) session.getAttribute("VOTE_SESSION_ID");

        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
            session.setAttribute("VOTE_SESSION_ID", sessionId);
        }

        return sessionId;
    }

    /**
     * Get all possible choice values
     */
    @GetMapping("/choices")
    public ResponseEntity<ChoiceEnum[]> getChoices() {
        return ResponseEntity.ok(ChoiceEnum.getSortedValues());
    }
}
