package com.juggenova.doodleclone.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "polls")
public class Poll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(name = "start_day", nullable = false)
    private LocalDate startDay;

    @Column(name = "end_day", nullable = false)
    private LocalDate endDay;

    @Column(nullable = false)
    private LocalDateTime deadline;

    @Column(name = "chosen_day")
    private LocalDate chosenDay;

    public Poll() {
    }

    public Poll(Long id, String title, String description, LocalDate startDay, LocalDate endDay, LocalDateTime deadline) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDay = startDay;
        this.endDay = endDay;
        this.deadline = deadline;
    }

    public boolean isClosed() {
        return chosenDay != null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDay() {
        return startDay;
    }

    public void setStartDay(LocalDate startDay) {
        this.startDay = startDay;
    }

    public LocalDate getEndDay() {
        return endDay;
    }

    public void setEndDay(LocalDate endDay) {
        this.endDay = endDay;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public LocalDate getChosenDay() {
        return chosenDay;
    }

    public void setChosenDay(LocalDate chosenDay) {
        this.chosenDay = chosenDay;
    }
}
