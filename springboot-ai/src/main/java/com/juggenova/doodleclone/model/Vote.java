package com.juggenova.doodleclone.model;

import java.time.LocalDate;

public class Vote {
    private Long id;
    private Long pollId;
    private LocalDate day;
    private ChoiceEnum choice;

    public Vote() {
    }

    public Vote(Long id, Long pollId, LocalDate day, ChoiceEnum choice) {
        this.id = id;
        this.pollId = pollId;
        this.day = day;
        this.choice = choice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPollId() {
        return pollId;
    }

    public void setPollId(Long pollId) {
        this.pollId = pollId;
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
