package com.juggenova.doodleclone.scheduler;

import com.juggenova.doodleclone.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduler for poll-related tasks
 */
@Component
public class PollScheduler {

    private final PollService pollService;

    @Autowired
    public PollScheduler(PollService pollService) {
        this.pollService = pollService;
    }

    /**
     * Check for polls with expired deadlines every minute
     */
    @Scheduled(cron = "0 * * * * *") // Run every minute
    public void checkPollDeadlines() {
        pollService.processPollsWithExpiredDeadlines();
    }
}
