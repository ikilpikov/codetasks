package ru.sber.codetasks.dto.comment;

import java.sql.Timestamp;

public class GetCommentDto {
    private Long id;

    private String username;

    private String commentText;

    private int likes;

    private boolean isLiked;

    private Timestamp postDate;

    public GetCommentDto(Long id,
                         String username,
                         String commentText,
                         int likes,
                         boolean isLiked,
                         Timestamp postDate) {
        this.id = id;
        this.username = username;
        this.commentText = commentText;
        this.likes = likes;
        this.isLiked = isLiked;
        this.postDate = postDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public Timestamp getPostDate() {
        return postDate;
    }

    public void setPostDate(Timestamp postDate) {
        this.postDate = postDate;
    }

}
