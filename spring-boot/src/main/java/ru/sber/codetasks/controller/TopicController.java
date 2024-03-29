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

import static ru.sber.codetasks.controller.constants.Messages.*;

@RestController
@RequestMapping("/topic")
@CrossOrigin(origins = "*")
public class TopicController {
    private final TopicService topicService;

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
        return new ResponseEntity<>(TOPIC_RELATION_VIOLATION_MESSAGE,
                HttpStatus.BAD_REQUEST);
    }

}
