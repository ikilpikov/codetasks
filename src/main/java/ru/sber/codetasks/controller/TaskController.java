package ru.sber.codetasks.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.sber.codetasks.domain.enums.Difficulty;
import ru.sber.codetasks.dto.task.CreateUpdateTaskDto;
import ru.sber.codetasks.dto.task.ReducedTaskDto;
import ru.sber.codetasks.dto.task.TaskUserDto;
import ru.sber.codetasks.service.TaskService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addTask(@RequestBody @Valid CreateUpdateTaskDto taskDto) {
        taskService.createTask(taskDto);
        return new ResponseEntity<>("Task added successfully", HttpStatus.OK);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<String> removeTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>("Task deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskUserDto> getTask(@PathVariable Long id) {
        return new ResponseEntity<>(taskService.getTask(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ReducedTaskDto>> getAllTasks(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                            @RequestParam(defaultValue = "") Difficulty difficulty,
                                                            @RequestParam(defaultValue = "") String topic) {

        var tasks = taskService.getTasks(page, size, difficulty, topic);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateTask(@PathVariable Long id,
                                             @RequestBody @Valid CreateUpdateTaskDto taskDto) {

        taskService.updateTask(id, taskDto);
        return new ResponseEntity<>("Task updated successfully", HttpStatus.OK);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> invalidDataExceptionHandler(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>("Fields are invalid", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> entityNotFoundExceptionHandler(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException ex) {
        return new ResponseEntity<>("Enum value incorrect", HttpStatus.NOT_FOUND);
    }

}
