package ru.sber.codetasks.controller;

import org.springframework.expression.AccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.sber.codetasks.domain.enums.Difficulty;
import ru.sber.codetasks.dto.comment.CreateUpdateCommentDto;
import ru.sber.codetasks.dto.task.CreateUpdateTaskDto;
import ru.sber.codetasks.dto.task.ReducedTaskDto;
import ru.sber.codetasks.dto.task.TaskUserDto;
import ru.sber.codetasks.exception.CommentAlreadyLikedException;
import ru.sber.codetasks.service.TaskService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/task")
@CrossOrigin(origins = "*")
public class TaskController {
    private final TaskService taskService;

    public static final String TASK_ADDED_MESSAGE = "Task added successfully";

    public static final String TASK_UPDATED_MESSAGE = "Task updated successfully";

    public static final String TASK_DELETED_MESSAGE = "Task deleted successfully";

    public static final String FIELDS_INVALID_MESSAGE = "Fields are invalid";

    public static final String ENUM_INVALID_MESSAGE = "Enum is invalid";

    public static final String COMMENT_ADDED_MESSAGE = "Comment added successfully";

    public static final String COMMENT_DELETED_MESSAGE = "Comment deleted successfully";

    public static final String COMMENT_LIKED_MESSAGE = "Comment liked successfully";

    public static final String COMMENT_UNLIKED_MESSAGE = "Comment unliked successfully";

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addTask(@RequestBody @Valid CreateUpdateTaskDto taskDto) {
        taskService.createTask(taskDto);
        return new ResponseEntity<>(TASK_ADDED_MESSAGE, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> removeTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(TASK_DELETED_MESSAGE, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TaskUserDto> getTask(@PathVariable Long id, Authentication authentication) {
        return new ResponseEntity<>(taskService.getTask(id, authentication.getName()), HttpStatus.OK);
    }

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ReducedTaskDto>> getAllTasks(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                            @RequestParam(defaultValue = "") List<Difficulty> difficulties,
                                                            @RequestParam(defaultValue = "") List<String> topics,
                                                            @RequestParam(defaultValue = "") List<String> languages) {

        var tasks = taskService.getTasks(page, size, difficulties, topics, languages);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateTask(@PathVariable Long id,
                                             @RequestBody @Valid CreateUpdateTaskDto taskDto) {

        taskService.updateTask(id, taskDto);
        return new ResponseEntity<>(TASK_UPDATED_MESSAGE, HttpStatus.OK);
    }

    @GetMapping("/count")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> countTasks() {
        var count = taskService.countTasks();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @PostMapping("/{id}/comment/add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> addComment(@PathVariable Long id,
                                             @RequestBody CreateUpdateCommentDto commentDto,
                                             Authentication authentication) {

        taskService.addComment(id, commentDto, authentication.getName());
        return new ResponseEntity<>(COMMENT_ADDED_MESSAGE, HttpStatus.OK);
    }

    @PostMapping("/comment/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteComment(@PathVariable Long id,
                                                Authentication authentication) throws AccessException {

        taskService.deleteComment(id, authentication.getName());
        return new ResponseEntity<>(COMMENT_DELETED_MESSAGE, HttpStatus.OK);
    }

    @PostMapping("/comment/like/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> likeComment(@PathVariable Long id,
                                                Authentication authentication) throws CommentAlreadyLikedException {

        taskService.likeComment(id, authentication.getName());
        return new ResponseEntity<>(COMMENT_LIKED_MESSAGE, HttpStatus.OK);
    }

    @PostMapping("/comment/unlike/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> unlikeComment(@PathVariable Long id,
                                              Authentication authentication) throws CommentAlreadyLikedException {

        taskService.unlikeComment(id, authentication.getName());
        return new ResponseEntity<>(COMMENT_UNLIKED_MESSAGE, HttpStatus.OK);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> invalidDataExceptionHandler(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(FIELDS_INVALID_MESSAGE, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> entityNotFoundExceptionHandler(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<String> methodArgumentTypeMismatchExceptionHandler(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(ENUM_INVALID_MESSAGE, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessException.class)
    public ResponseEntity<String> accessExceptionHandler(AccessException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CommentAlreadyLikedException.class)
    public ResponseEntity<String> commentAlreadyLikedExceptionHandler(CommentAlreadyLikedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

}
