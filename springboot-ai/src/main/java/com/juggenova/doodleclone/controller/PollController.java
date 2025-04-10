package com.juggenova.doodleclone.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Map<String, Object>> getDefaultPoll() {
        Poll poll = pollService.getDefaultPoll();
        List<Vote> votes = pollService.getDefaultVotes();

        Map<String, Object> response = new HashMap<>();
        response.put("poll", poll);
        response.put("votes", votes);

        return ResponseEntity.ok(response);
    }

    /**
     * Update a vote
     */
    @PostMapping("/votes/{voteId}")
    public ResponseEntity<Vote> updateVote(@PathVariable Long voteId, @RequestParam ChoiceEnum choice) {
        Vote updatedVote = pollService.updateVote(voteId, choice);
        if (updatedVote != null) {
            return ResponseEntity.ok(updatedVote);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get all possible choice values
     */
    @GetMapping("/choices")
    public ResponseEntity<ChoiceEnum[]> getChoices() {
        return ResponseEntity.ok(ChoiceEnum.getSortedValues());
    }
}
