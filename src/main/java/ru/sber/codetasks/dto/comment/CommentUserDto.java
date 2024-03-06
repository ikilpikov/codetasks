package ru.sber.codetasks.dto.comment;

public class CommentUserDto {
    private String username;

    private String commentText;

    private int likes;

    public CommentUserDto(String username, String commentText, int likes) {
        this.username = username;
        this.commentText = commentText;
        this.likes = likes;
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
