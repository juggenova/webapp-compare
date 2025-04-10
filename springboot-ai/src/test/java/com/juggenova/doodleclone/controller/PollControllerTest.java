package com.juggenova.doodleclone.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import com.juggenova.doodleclone.model.Poll;
import com.juggenova.doodleclone.model.Vote;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PollControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @SuppressWarnings("unchecked")
    @Test
    public void testGetDefaultPoll() {
        ResponseEntity<Map> response = restTemplate.getForEntity("/api/polls/default", Map.class);
        
        assertEquals(200, response.getStatusCodeValue());
        
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        
        Map<String, Object> poll = (Map<String, Object>) body.get("poll");
        List<Map<String, Object>> votes = (List<Map<String, Object>>) body.get("votes");
        
        assertNotNull(poll);
        assertNotNull(votes);
        
        assertEquals("JUG Genova Meeting", poll.get("title"));
        assertEquals(7, votes.size()); // One week = 7 days
    }
}
