package ru.sber.codetasks.dto.comment;

import javax.validation.constraints.NotBlank;

public class CreateCommentDto {
    @NotBlank
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
