package ru.sber.codetasks.dto.comment;

public class GetCommentDto {
    private Long id;

    private String username;

    private String commentText;

    private int likes;

    public GetCommentDto(Long id, String username, String commentText, int likes) {
        this.id = id;
        this.username = username;
        this.commentText = commentText;
        this.likes = likes;
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

}
