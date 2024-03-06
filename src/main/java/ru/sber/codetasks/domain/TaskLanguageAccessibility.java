package ru.sber.codetasks.domain;

import javax.persistence.*;


@Table(name = "task_language_accessibility")
public class TaskLanguageAccessibility {
    @Id
    @ManyToOne
    @JoinColumn(name = "task", nullable = false)
    private Task task;

    @Id
    @ManyToOne
    @JoinColumn(name = "language", nullable = false)
    private ProgrammingLanguage language;

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public ProgrammingLanguage getLanguage() {
        return language;
    }

    public void setLanguage(ProgrammingLanguage language) {
        this.language = language;
    }

}
