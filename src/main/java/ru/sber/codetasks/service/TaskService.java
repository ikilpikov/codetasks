package ru.sber.codetasks.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.sber.codetasks.domain.ProgrammingLanguage;
import ru.sber.codetasks.domain.Task;
import ru.sber.codetasks.domain.TestCase;
import ru.sber.codetasks.dto.comment.CommentUserDto;
import ru.sber.codetasks.dto.task.CreateUpdateTaskDto;
import ru.sber.codetasks.dto.task.ReducedTaskDto;
import ru.sber.codetasks.dto.task.TaskUserDto;
import ru.sber.codetasks.repository.*;

import javax.persistence.EntityNotFoundException;
import javax.persistence.Transient;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private TaskRepository taskRepository;

    private TopicRepository topicRepository;

    private ProgrammingLanguageRepository programmingLanguageRepository;

    private TestCaseRepository testCaseRepository;

    public TaskService(TaskRepository taskRepository,
                       TopicRepository topicRepository,
                       ProgrammingLanguageRepository programmingLanguageRepository,
                       TestCaseRepository testCaseRepository) {
        this.taskRepository = taskRepository;
        this.topicRepository = topicRepository;
        this.programmingLanguageRepository = programmingLanguageRepository;
        this.testCaseRepository = testCaseRepository;
    }

    public void createTask(CreateUpdateTaskDto taskDto) {
        Task task = new Task();
        task.setName(taskDto.getName());
        task.setCondition(taskDto.getCondition());
        task.setTopic(topicRepository
                .findByName(taskDto.getTopic())
                .orElseThrow(() -> new EntityNotFoundException("Topic not found with name: " + taskDto.getTopic())));
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
                        .orElseThrow(() -> new EntityNotFoundException("Language not found with name: " +
                                x)))
                .collect(Collectors.toList());
        task.setLanguages(languages);

        taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public TaskUserDto getTask(Long id) {
        var taskEntity = taskRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
        var task = new TaskUserDto();
        task.setName(taskEntity.getName());
        task.setCondition(taskEntity.getCondition());
        task.setDifficulty(taskEntity.getDifficulty());
        task.setTopic(taskEntity.getTopic().getName());

        var languages = taskEntity.getLanguages().stream().map(ProgrammingLanguage::getName).collect(Collectors.toList());
        task.setLanguages(languages);

        var comments = taskEntity
                .getComments()
                .stream()
                .map(x -> new CommentUserDto(x.getUser().getUsername(), x.getCommentText(), x.getLikes()))
                .collect(Collectors.toList());
        task.setComments(comments);

        return task;
    }

    public List<ReducedTaskDto> getTasks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        var tasks = taskRepository
                .findAll(pageable)
                .stream()
                .map(x -> new ReducedTaskDto(x.getName(),
                        x.getTopic().getName(),
                        x.getDifficulty(),
                        x.getLanguages().stream().map(y -> y.getName()).collect(Collectors.toList())))
                .collect(Collectors.toList());

        return tasks;
    }

    @Transactional
    public void updateTask(Long id, CreateUpdateTaskDto taskDto) {
        testCaseRepository.deleteAllByTask_Id(id);

        var task = taskRepository.findById(id).get();
        task.setName(taskDto.getName());
        task.setCondition(taskDto.getCondition());
        task.setTopic(topicRepository.findByName(taskDto.getTopic()).get());
        task.setDifficulty(taskDto.getDifficulty());

        var testcases = taskDto.getTestcases()
                .stream()
                .map(x -> new TestCase(task, x.getInputData(), x.getOutputData()))
                .collect(Collectors.toList());
        task.setTestCases(testcases);

        var languages = taskDto.getLanguages()
                .stream()
                .map(x -> programmingLanguageRepository.findByName(x).get())
                .collect(Collectors.toList());
        task.setLanguages(languages);

        taskRepository.save(task);
    }

}
