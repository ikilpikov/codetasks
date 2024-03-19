package ru.sber.codetasks.dto.task;

import ru.sber.codetasks.domain.enums.Difficulty;
import ru.sber.codetasks.dto.comment.GetCommentDto;

import java.util.List;

public class TaskUserDto {
    private String name;

    private String condition;

    private String topic;

    private Difficulty difficulty;

    private List<String> languages;

    private List<GetCommentDto> comments;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public List<GetCommentDto> getComments() {
        return comments;
    }

    public void setComments(List<GetCommentDto> comments) {
        this.comments = comments;
    }

}
