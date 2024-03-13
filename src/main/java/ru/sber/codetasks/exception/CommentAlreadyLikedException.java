package ru.sber.codetasks.exception;

public class CommentAlreadyLikedException extends RuntimeException {
    public CommentAlreadyLikedException(String message) {
        super(message);
    }

}
