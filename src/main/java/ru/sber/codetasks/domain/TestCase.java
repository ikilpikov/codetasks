package ru.sber.codetasks.domain;

import javax.persistence.*;

@Entity
@Table(name = "test_case")
public class TestCase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task", nullable = false)
    private Task task;

    @Column(name = "input_data", nullable = false, length = 100)
    private String inputData;

    @Column(name = "output_data", nullable = false, length = 100)
    private String outputData;

    public TestCase(Task task, String inputData, String outputData) {
        this.task = task;
        this.inputData = inputData;
        this.outputData = outputData;
    }

    public TestCase() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getInputData() {
        return inputData;
    }

    public void setInputData(String inputData) {
        this.inputData = inputData;
    }

    public String getOutputData() {
        return outputData;
    }

    public void setOutputData(String outputData) {
        this.outputData = outputData;
    }

}