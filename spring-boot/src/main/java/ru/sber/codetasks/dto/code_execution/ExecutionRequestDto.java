package ru.sber.codetasks.dto.code_execution;

import javax.validation.constraints.NotBlank;

public class ExecutionRequestDto {
    @NotBlank
    private String language;

    private String stdin;

    @NotBlank
    private String code;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getStdin() {
        return stdin;
    }

    public void setStdin(String stdin) {
        this.stdin = stdin;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
