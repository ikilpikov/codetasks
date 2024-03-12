package ru.sber.codetasks.service.implementation;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.expression.AccessException;
import org.springframework.stereotype.Service;
import ru.sber.codetasks.domain.Comment;
import ru.sber.codetasks.domain.Task;
import ru.sber.codetasks.domain.User;
import ru.sber.codetasks.domain.enums.Difficulty;
import ru.sber.codetasks.domain.enums.Role;
import ru.sber.codetasks.dto.comment.CreateUpdateCommentDto;
import ru.sber.codetasks.dto.task.CreateUpdateTaskDto;
import ru.sber.codetasks.dto.task.ReducedTaskDto;
import ru.sber.codetasks.dto.task.TaskUserDto;
import ru.sber.codetasks.mapper.TaskMapper;
import ru.sber.codetasks.repository.CommentRepository;
import ru.sber.codetasks.repository.TaskRepository;
import ru.sber.codetasks.repository.TestCaseRepository;
import ru.sber.codetasks.repository.UserRepository;
import ru.sber.codetasks.service.TaskService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final TestCaseRepository testCaseRepository;

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    private final TaskMapper taskMapper;

    public static final String TASK_NOT_FOUND_MESSAGE = "Task not found: ";

    public static final String COMMENT_NOT_FOUND_MESSAGE = "Comment not found ";

    public TaskServiceImpl(TaskRepository taskRepository,
                           TestCaseRepository testCaseRepository,
                           UserRepository userRepository,
                           TaskMapper taskMapper,
                           CommentRepository commentRepository) {
        this.taskRepository = taskRepository;
        this.testCaseRepository = testCaseRepository;
        this.userRepository = userRepository;
        this.taskMapper = taskMapper;
        this.commentRepository = commentRepository;
    }

    public void createTask(CreateUpdateTaskDto taskDto) {
        Task task = taskMapper.mapCreateUpdateTaskDtoToTask(taskDto);
        taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new EntityNotFoundException(TASK_NOT_FOUND_MESSAGE + id);
        }
        taskRepository.deleteById(id);
    }

    public TaskUserDto getTask(Long id) {
        var task = taskRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(TASK_NOT_FOUND_MESSAGE + id));

        return taskMapper.mapTaskToTaskUserDto(task);
    }

    public List<ReducedTaskDto> getTasks(int page, int size, List<Difficulty> difficulties,
                                         List<String> topics, List<String> languages) {
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
            throw new EntityNotFoundException(TASK_NOT_FOUND_MESSAGE + id);
        }

        testCaseRepository.deleteAllByTask_Id(id);
        var task = taskMapper.mapCreateUpdateTaskDtoToTask(taskDto);
        task.setId(id);
        taskRepository.save(task);
    }

    public Long countTasks() {
        return taskRepository.count();
    }

    public void addComment(Long taskId,
                           CreateUpdateCommentDto createUpdateCommentDto,
                           String username) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found " + taskId));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found " + username));

        Comment comment = new Comment();
        comment.setCommentText(createUpdateCommentDto.getText());
        comment.setTask(task);
        comment.setUser(user);

        List<Comment> comments = task.getComments();
        comments.add(comment);
        task.setComments(comments);

        taskRepository.save(task);
    }


    public void deleteComment(Long id, String username) throws AccessException {
        var comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(COMMENT_NOT_FOUND_MESSAGE + id));

        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found " + username));

        if (canCommentBeDeleted(user, comment)) {
            commentRepository.deleteById(id);
        } else {
            throw new AccessException("No rights to delete");
        }
    }

    private boolean canCommentBeDeleted(User user, Comment comment) {
        if (comment.getUser().getId().equals(user.getId())) {
            return true;
        }

        if (user.getRole() == Role.ROLE_MODERATOR || user.getRole() == Role.ROLE_ADMIN) {
            return true;
        }

        return false;
    }

}
