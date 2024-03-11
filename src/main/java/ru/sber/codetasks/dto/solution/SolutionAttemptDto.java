package ru.sber.codetasks.dto.solution;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SolutionAttemptDto {
    @NotNull
    private Long taskId;

    @NotBlank
    private String language;

    @NotBlank
    private String code;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
