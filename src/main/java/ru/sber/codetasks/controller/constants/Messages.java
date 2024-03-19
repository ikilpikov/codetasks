package ru.sber.codetasks.controller.constants;

public final class Messages {
    private Messages() {
    }

    public static final String FIELDS_INVALID_MESSAGE = "Fields are invalid";
    public static final String ENUM_INVALID_MESSAGE = "Enum is invalid";

    public static final String CANNOT_AUTHENTICATE = "Cannot authenticate";

    public static final String LANGUAGE_ADDED_MESSAGE = "Language added successfully";
    public static final String LANGUAGE_DELETED_MESSAGE = "Language deleted successfully";
    public static final String LANGUAGE_UPDATED_MESSAGE = "Language updated successfully";
    public static final String LANGUAGE_RELATION_VIOLATION_MESSAGE =
            "Cannot delete cause this language is related to a task";

    public static final String TASK_ADDED_MESSAGE = "Task added successfully";
    public static final String TASK_UPDATED_MESSAGE = "Task updated successfully";
    public static final String TASK_DELETED_MESSAGE = "Task deleted successfully";

    public static final String COMMENT_ADDED_MESSAGE = "Comment added successfully";
    public static final String COMMENT_DELETED_MESSAGE = "Comment deleted successfully";
    public static final String COMMENT_LIKED_MESSAGE = "Comment liked successfully";
    public static final String COMMENT_UNLIKED_MESSAGE = "Comment unliked successfully";

    public static final String TOPIC_ADDED_MESSAGE = "Topic added successfully";
    public static final String TOPIC_DELETED_MESSAGE = "Topic deleted successfully";
    public static final String TOPIC_UPDATED_MESSAGE = "Topic updated successfully";
    public static final String TOPIC_RELATION_VIOLATION_MESSAGE =
            "Cannot delete cause this topic is related to a task";

}

