package ru.sber.codetasks.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.sber.codetasks.dto.topic.ReducedTopicDto;
import ru.sber.codetasks.dto.topic.TopicDto;
import ru.sber.codetasks.service.TopicService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/topic")
public class TopicController {
    private final TopicService topicService;

    public static final String TOPIC_ADDED_MESSAGE = "Topic added successfully";

    public static final String TOPIC_DELETED_MESSAGE = "Topic deleted successfully";

    public static final String FIELDS_INVALID_MESSAGE = "Fields are invalid";

    public static final String TOPIC_UPDATED_MESSAGE = "Topic updated successfully";

    public static final String RELATION_VIOLATION_MESSAGE =
            "Cannot delete cause this topic is related to a task";

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addTopic(@RequestBody @Valid ReducedTopicDto topicDto) {
        topicService.createTopic(topicDto);
        return new ResponseEntity<>(TOPIC_ADDED_MESSAGE, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> removeTopic(@PathVariable Long id) {
        topicService.deleteTopic(id);
        return new ResponseEntity<>(TOPIC_DELETED_MESSAGE, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReducedTopicDto> getTopic(@PathVariable Long id) {
        return new ResponseEntity<>(topicService.getTopic(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<TopicDto>> getAllTopics() {
        var topics = topicService.getTopics();
        return new ResponseEntity<>(topics, HttpStatus.OK);
    }


    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateTopic(@PathVariable Long id,
                                              @RequestBody @Valid ReducedTopicDto topicDto) {

        topicService.updateTopic(id, topicDto);
        return new ResponseEntity<>(TOPIC_UPDATED_MESSAGE, HttpStatus.OK);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> invalidDataExceptionHandler() {
        return new ResponseEntity<>(FIELDS_INVALID_MESSAGE, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> entityNotFoundExceptionHandler(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> dataIntegrityViolationException() {
        return new ResponseEntity<>(RELATION_VIOLATION_MESSAGE,
                HttpStatus.BAD_REQUEST);
    }

}
