package ru.sber.codetasks.dto.task;

import ru.sber.codetasks.domain.enums.Difficulty;
import ru.sber.codetasks.dto.testcase.NewTestCaseDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class CreateUpdateTaskDto {
    @NotBlank
    private String name;

    @NotBlank
    private String condition;

    @NotBlank
    private String topic;

    @NotNull
    private Difficulty difficulty;

    @NotNull
    private List<String> languages;

    @NotNull
    private List<NewTestCaseDto> testcases;

    public CreateUpdateTaskDto(String name,
                               String condition,
                               String topic,
                               Difficulty difficulty,
                               List<String> languages,
                               List<NewTestCaseDto> testcases) {

        this.name = name;
        this.condition = condition;
        this.topic = topic;
        this.difficulty = difficulty;
        this.languages = languages;
        this.testcases = testcases;
    }

    public CreateUpdateTaskDto() {
    }

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

    public List<NewTestCaseDto> getTestcases() {
        return testcases;
    }

    public void setTestcases(List<NewTestCaseDto> testcases) {
        this.testcases = testcases;
    }

}
