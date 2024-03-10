package ru.sber.codetasks.service;

import ru.sber.codetasks.domain.enums.Difficulty;
import ru.sber.codetasks.dto.task.CreateUpdateTaskDto;
import ru.sber.codetasks.dto.task.ReducedTaskDto;
import ru.sber.codetasks.dto.task.TaskUserDto;

import java.util.List;

public interface TaskService {
    void createTask(CreateUpdateTaskDto taskDto);

    void deleteTask(Long id);

    TaskUserDto getTask(Long id);

    List<ReducedTaskDto> getTasks(int page, int size, List<Difficulty> difficulties,
                                         List<String> topics, List<String> languages);

    void updateTask(Long id, CreateUpdateTaskDto taskDto);

}
