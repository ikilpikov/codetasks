package ru.sber.codetasks.mapper;

import org.springframework.stereotype.Component;
import ru.sber.codetasks.domain.ProgrammingLanguage;
import ru.sber.codetasks.domain.Task;
import ru.sber.codetasks.domain.TestCase;
import ru.sber.codetasks.domain.User;
import ru.sber.codetasks.dto.comment.GetCommentDto;
import ru.sber.codetasks.dto.task.CreateUpdateTaskDto;
import ru.sber.codetasks.dto.task.ReducedTaskDto;
import ru.sber.codetasks.dto.task.TaskUserDto;
import ru.sber.codetasks.repository.ProgrammingLanguageRepository;
import ru.sber.codetasks.repository.TopicRepository;

import javax.persistence.EntityNotFoundException;
import java.util.stream.Collectors;

@Component
public class TaskMapper {
    private final TopicRepository topicRepository;

    private final ProgrammingLanguageRepository programmingLanguageRepository;

    public TaskMapper(TopicRepository topicRepository,
                      ProgrammingLanguageRepository programmingLanguageRepository) {
        this.topicRepository = topicRepository;
        this.programmingLanguageRepository = programmingLanguageRepository;
    }

    public Task mapCreateUpdateTaskDtoToTask(CreateUpdateTaskDto taskDto) {
        Task task = new Task();
        task.setName(taskDto.getName());
        task.setCondition(taskDto.getCondition());

        task.setTopic(topicRepository
                .findByName(taskDto.getTopic())
                .orElseThrow(() -> new EntityNotFoundException("Topic not found with name: " +
                        taskDto.getTopic())));
        task.setDifficulty(taskDto.getDifficulty());

        var testcases = taskDto.getTestcases()
                .stream()
                .map(x -> new TestCase(task, x.getInputData(), x.getOutputData()))
                .collect(Collectors.toList());
        task.setTestCases(testcases);

        var languages = taskDto.getLanguages()
                .stream()
                .map(x -> programmingLanguageRepository
                        .findByName(x)
                        .orElseThrow(() -> new EntityNotFoundException("Language not found: " + x)))
                .collect(Collectors.toList());
        task.setLanguages(languages);

        return task;
    }

    public ReducedTaskDto mapTaskToReducedTaskDto(Task task) {
        return new ReducedTaskDto(task.getId(),
                task.getName(),
                task.getTopic().getName(),
                task.getDifficulty(),
                task.getLanguages()
                        .stream()
                        .map(ProgrammingLanguage::getName)
                        .collect(Collectors.toList()));
    }

    public TaskUserDto mapTaskToTaskUserDto(Task task, User user) {
        var taskUserDto = new TaskUserDto();
        taskUserDto.setName(task.getName());
        taskUserDto.setCondition(task.getCondition());
        taskUserDto.setDifficulty(task.getDifficulty());
        taskUserDto.setTopic(task.getTopic().getName());

        var languages = task.getLanguages().stream().map(ProgrammingLanguage::getName).collect(Collectors.toList());
        taskUserDto.setLanguages(languages);

        var comments = task
                .getComments()
                .stream()
                .map(x -> new GetCommentDto(x.getId(),
                        x.getUser().getUsername(),
                        x.getCommentText(),
                        x.getUsersLiked().size(),
                        x.getUsersLiked().contains(user)))
                .collect(Collectors.toList());
        taskUserDto.setComments(comments);

        return taskUserDto;
    }

}
