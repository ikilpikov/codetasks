package ru.sber.codetasks.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.sber.codetasks.domain.Task;
import ru.sber.codetasks.domain.enums.Difficulty;
import ru.sber.codetasks.dto.task.CreateUpdateTaskDto;
import ru.sber.codetasks.dto.task.ReducedTaskDto;
import ru.sber.codetasks.dto.task.TaskUserDto;
import ru.sber.codetasks.mapper.TaskMapper;
import ru.sber.codetasks.repository.TaskRepository;
import ru.sber.codetasks.repository.TestCaseRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    private final TestCaseRepository testCaseRepository;

    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository,
                       TestCaseRepository testCaseRepository,
                       TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.testCaseRepository = testCaseRepository;
    }

    public void createTask(CreateUpdateTaskDto taskDto) {
        Task task = taskMapper.mapCreateUpdateTaskDtoToTask(taskDto);
        taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public TaskUserDto getTask(Long id) {
        var task = taskRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));

        return taskMapper.mapTaskToTaskUserDto(task);
    }

    public List<ReducedTaskDto> getTasks(int page, int size, List<Difficulty> difficulties,
                                         List<String> topics,  List<String> languages) {
        Pageable pageable = PageRequest.of(page, size);

        return taskRepository
                .findByCriteria(difficulties, topics, languages, pageable)
                .stream()
                .map(taskMapper::mapTaskToReducedTaskDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateTask(Long id, CreateUpdateTaskDto taskDto) {
        if (!taskRepository.existsById(id)) {
            throw new EntityNotFoundException("Task not found with id: " + id);
        }

        testCaseRepository.deleteAllByTask_Id(id);
        var task = taskMapper.mapCreateUpdateTaskDtoToTask(taskDto);
        task.setId(id);
        taskRepository.save(task);
    }

}
