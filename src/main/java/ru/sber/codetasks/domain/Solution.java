package ru.sber.codetasks.domain;

import javax.persistence.*;

import java.security.Timestamp;
import java.time.Duration;

@Entity
@Table(name = "solution")
public class Solution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task", nullable = false)
    private Task task;

    @ManyToOne
    @JoinColumn(name = "\"user\"", nullable = false)
    private User user;

    @Column(name = "submission_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp submissionDate;

    @Column(name = "execution_time")
    private Duration executionTime;

    @Column(name = "is_accepted")
    private Boolean isAccepted;

    @Column(name = "likes", columnDefinition = "INT DEFAULT 0")
    private int likes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Timestamp submissionDate) {
        this.submissionDate = submissionDate;
    }

    public Duration getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Duration executionTime) {
        this.executionTime = executionTime;
    }

    public Boolean getAccepted() {
        return isAccepted;
    }

    public void setAccepted(Boolean accepted) {
        isAccepted = accepted;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

}