package ru.sber.codetasks.dto.topic;

import javax.validation.constraints.NotBlank;

public class ReducedTopicDto {
    @NotBlank
    private String name;

    public ReducedTopicDto(String name) {
        this.name = name;
    }

    public ReducedTopicDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
