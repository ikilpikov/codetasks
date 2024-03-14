package ru.sber.codetasks.dto.comment;

import javax.validation.constraints.NotNull;

public class LikeUnlikeCommentDto {
    @NotNull
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
