package ru.sber.codetasks.domain;

import javax.persistence.*;

@Table(name = "comment_likes")
public class CommentLikes {
    @Id
    @ManyToOne
    @JoinColumn(name = "comment", nullable = false)
    private Comment comment;

    @Id
    @ManyToOne
    @JoinColumn(name = "\"user\"", nullable = false)
    private User user;

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}