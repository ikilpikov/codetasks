package ru.sber.codetasks.dto.solution;

import ru.sber.codetasks.dto.enums.SolutionResult;

public class SolutionResponseDto {
    private SolutionResult status;

    private int time;

    public SolutionResult getStatus() {
        return status;
    }

    public void setStatus(SolutionResult status) {
        this.status = status;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

}
