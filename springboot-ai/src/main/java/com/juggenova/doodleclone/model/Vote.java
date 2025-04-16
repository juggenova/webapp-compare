package com.juggenova.doodleclone.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "votes")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "poll_id", nullable = false)
    private Poll poll;

    @Column(nullable = false)
    private LocalDate day;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChoiceEnum choice;

    @Column(name = "session_id", nullable = false)
    private String sessionId;

    public Vote() {
    }

    public Vote(Long id, Poll poll, LocalDate day, ChoiceEnum choice, String sessionId) {
        this.id = id;
        this.poll = poll;
        this.day = day;
        this.choice = choice;
        this.sessionId = sessionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public ChoiceEnum getChoice() {
        return choice;
    }

    public void setChoice(ChoiceEnum choice) {
        this.choice = choice;
    }
}
