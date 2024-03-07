package ru.sber.codetasks.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addTask(@RequestBody @Valid ReducedTopicDto topicDto) {
        topicService.createTopic(topicDto);
        return new ResponseEntity<>("Topic added successfully", HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> removeTask(@PathVariable Long id) {
        topicService.deleteTopic(id);
        return new ResponseEntity<>("Topic deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReducedTopicDto> getTask(@PathVariable Long id) {
        return new ResponseEntity<>(topicService.getTopic(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TopicDto>> getAllTopics() {
        var topics = topicService.getTopics();
        return new ResponseEntity<>(topics, HttpStatus.OK);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateTask(@PathVariable Long id,
                                             @RequestBody @Valid ReducedTopicDto topicDto) {

        topicService.updateTopic(id, topicDto);
        return new ResponseEntity<>("Topic updated successfully", HttpStatus.OK);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> invalidDataExceptionHandler() {
        return new ResponseEntity<>("Fields are invalid", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> entityNotFoundExceptionHandler(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public  ResponseEntity<String> dataIntegrityViolationException() {
        return new ResponseEntity<>("Cannot delete cause this topic is related to a task",
                HttpStatus.BAD_REQUEST);
    }

}
