package ru.sber.codetasks.dto.task;

import ru.sber.codetasks.domain.enums.Difficulty;

import java.util.List;

public class ReducedTaskDto {
    private String name;

    private String topic;

    private Difficulty difficulty;

    private List<String> languages;

    public ReducedTaskDto(String name, String topic, Difficulty difficulty, List<String> languages) {
        this.name = name;
        this.topic = topic;
        this.difficulty = difficulty;
        this.languages = languages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
