package ru.sber.codetasks.dto.programming_language;

import javax.validation.constraints.NotBlank;

public class ReducedProgrammingLanguageDto {
    @NotBlank
    private String name;

    public ReducedProgrammingLanguageDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
