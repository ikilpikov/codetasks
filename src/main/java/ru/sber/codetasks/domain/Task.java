package ru.sber.codetasks.domain;

import ru.sber.codetasks.domain.enums.Difficulty;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 60)
    private String name;

    @Column(name = "condition", columnDefinition = "TEXT", nullable = false)
    private String condition;

    @ManyToOne
    @JoinColumn(name = "topic", nullable = false)
    private Topic topic;

    @Column(name = "difficulty", nullable = false)
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<TestCase> testCases;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @ManyToMany
    @JoinTable(
            name = "task_language_accessibility",
            joinColumns = @JoinColumn(name = "task"),
            inverseJoinColumns = @JoinColumn(name = "language")
    )
    private List<ProgrammingLanguage> languages;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public List<TestCase> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<TestCase> testCases) {
        this.testCases = testCases;
    }

    public List<ProgrammingLanguage> getLanguages() {
        return languages;
    }

    public void setLanguages(List<ProgrammingLanguage> languages) {
        this.languages = languages;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}