package ru.sber.codetasks.service;

import org.springframework.expression.AccessException;
import ru.sber.codetasks.domain.enums.Difficulty;
import ru.sber.codetasks.dto.comment.CreateUpdateCommentDto;
import ru.sber.codetasks.dto.task.CreateUpdateTaskDto;
import ru.sber.codetasks.dto.task.ReducedTaskDto;
import ru.sber.codetasks.dto.task.TaskUserDto;
import ru.sber.codetasks.exception.CommentAlreadyLikedException;

import java.util.List;

public interface TaskService {
    void createTask(CreateUpdateTaskDto taskDto);

    void deleteTask(Long id);

    TaskUserDto getTask(Long id);

    List<ReducedTaskDto> getTasks(int page, int size, List<Difficulty> difficulties,
                                  List<String> topics, List<String> languages);

    void updateTask(Long id, CreateUpdateTaskDto taskDto);

    Long countTasks();

    void addComment(Long taskId,
                    CreateUpdateCommentDto createUpdateCommentDto,
                    String username);

    void deleteComment(Long id, String username) throws AccessException;

    void likeComment(Long id, String username) throws CommentAlreadyLikedException;
}
