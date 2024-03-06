package ru.sber.codetasks.dto.testcase;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class NewTestCaseDto {
    @NotBlank
    private String inputData;

    @NotBlank
    private String outputData;

    public NewTestCaseDto() {
    }

    public NewTestCaseDto(String inputData, String outputData) {
        this.inputData = inputData;
        this.outputData = outputData;
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
