package ru.sber.codetasks.dto.solution;

import java.sql.Timestamp;

public class SolutionUserDto {
    private Long id;

    private String username;

    private Timestamp submissionDate;

    private int submissionTime;

    private String code;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Timestamp submissionDate) {
        this.submissionDate = submissionDate;
    }

    public int getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(int submissionTime) {
        this.submissionTime = submissionTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
