package ru.sber.codetasks.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

    public static final String TASK_ADDED_MESSAGE = "Task added successfully";

    public static final String TASK_DELETED_MESSAGE = "Task deleted successfully";

    public static final String FIELDS_INVALID_MESSAGE = "Fields are invalid";

    public static final String ENUM_INVALID_MESSAGE = "Enum is invalid";

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addTask(@RequestBody @Valid CreateUpdateTaskDto taskDto) {
        taskService.createTask(taskDto);
        return new ResponseEntity<>(TASK_ADDED_MESSAGE, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> removeTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(TASK_DELETED_MESSAGE, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskUserDto> getTask(@PathVariable Long id) {
        return new ResponseEntity<>(taskService.getTask(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ReducedTaskDto>> getAllTasks(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                            @RequestParam(defaultValue = "") List<Difficulty> difficulties,
                                                            @RequestParam(defaultValue = "") List<String> topics,
                                                            @RequestParam(defaultValue = "") List<String> languages) {

        var tasks = taskService.getTasks(page, size, difficulties, topics, languages);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateTask(@PathVariable Long id,
                                             @RequestBody @Valid CreateUpdateTaskDto taskDto) {

        taskService.updateTask(id, taskDto);
        return new ResponseEntity<>("Task updated successfully", HttpStatus.OK);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> invalidDataExceptionHandler() {
        return new ResponseEntity<>(FIELDS_INVALID_MESSAGE, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> entityNotFoundExceptionHandler(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<String> methodArgumentTypeMismatchExceptionHandler() {
        return new ResponseEntity<>(ENUM_INVALID_MESSAGE, HttpStatus.NOT_FOUND);
    }

}
