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
import ru.sber.codetasks.dto.comment.CreateCommentDto;
import ru.sber.codetasks.dto.comment.LikeUnlikeCommentDto;
import ru.sber.codetasks.dto.task.CreateUpdateTaskDto;
import ru.sber.codetasks.dto.task.ReducedTaskDto;
import ru.sber.codetasks.dto.task.TaskUserDto;
import ru.sber.codetasks.exception.CommentAlreadyLikedException;
import ru.sber.codetasks.mapper.TaskMapper;
import ru.sber.codetasks.repository.CommentRepository;
import ru.sber.codetasks.repository.TaskRepository;
import ru.sber.codetasks.repository.TestCaseRepository;
import ru.sber.codetasks.repository.UserRepository;
import ru.sber.codetasks.service.TaskService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import static ru.sber.codetasks.service.implementation.constatns.Messages.*;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final TestCaseRepository testCaseRepository;

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    private final TaskMapper taskMapper;

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

    @Override
    public void createTask(CreateUpdateTaskDto taskDto) {
        Task task = taskMapper.mapCreateUpdateTaskDtoToTask(taskDto);
        taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new EntityNotFoundException(TASK_NOT_FOUND_MESSAGE + id);
        }
        taskRepository.deleteById(id);
    }

    @Override
    public TaskUserDto getTask(Long id, String username) {
        var task = taskRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(TASK_NOT_FOUND_MESSAGE + id));

        var user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE + username));

        return taskMapper.mapTaskToTaskUserDto(task, user);
    }

    @Override
    public CreateUpdateTaskDto getTaskForUpdating(Long id) {
        var task = taskRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(TASK_NOT_FOUND_MESSAGE + id));

        return taskMapper.mapTaskToCreateUpdateTaskDto(task);
    }

    @Override
    public List<ReducedTaskDto> getTasks(int page, int size, List<Difficulty> difficulties,
                                         List<String> topics, List<String> languages) {
        Pageable pageable = PageRequest.of(page, size);

        return taskRepository
                .findByCriteria(difficulties, topics, languages, pageable)
                .stream()
                .map(taskMapper::mapTaskToReducedTaskDto)
                .collect(Collectors.toList());
    }

    @Override
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

    @Override
    public Long countTasks() {
        return taskRepository.count();
    }

    @Override
    public void addComment(Long taskId,
                           CreateCommentDto createCommentDto,
                           String username) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException(TASK_NOT_FOUND_MESSAGE + taskId));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE + username));

        Comment comment = new Comment();
        comment.setCommentText(createCommentDto.getText());
        comment.setTask(task);
        comment.setUser(user);
        comment.setPostDate(new Timestamp(System.currentTimeMillis()));

        List<Comment> comments = task.getComments();
        comments.add(comment);
        task.setComments(comments);

        taskRepository.save(task);
    }


    @Override
    public void deleteComment(Long id, String username) throws AccessException {
        var comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(COMMENT_NOT_FOUND_MESSAGE + id));

        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE + username));

        if (canCommentBeDeleted(user, comment)) {
            commentRepository.deleteById(id);
        } else {
            throw new AccessException(NO_RIGHTS_MESSAGE);
        }
    }

    @Override
    public void likeComment(LikeUnlikeCommentDto likeUnlikeCommentDto, String username) {
        var commentId = likeUnlikeCommentDto.getId();
        var comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException(COMMENT_NOT_FOUND_MESSAGE + commentId));

        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE + username));

        var likedUsers = comment.getUsersLiked();
        if (likedUsers.contains(user)) {
            throw new CommentAlreadyLikedException(ALREADY_LIKED_MESSAGE);
        }

        likedUsers.add(user);
        comment.setUsersLiked(likedUsers);
        commentRepository.save(comment);
    }

    @Override
    public void unlikeComment(LikeUnlikeCommentDto likeUnlikeCommentDto, String username) {
        var commentId = likeUnlikeCommentDto.getId();
        var comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException(COMMENT_NOT_FOUND_MESSAGE + commentId));

        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE + username));

        var likedUsers = comment.getUsersLiked();
        if (!likedUsers.contains(user)) {
            throw new EntityNotFoundException(LIKE_NOT_FOUND_MESSAGE);
        }

        likedUsers.remove(user);
        comment.setUsersLiked(likedUsers);
        commentRepository.save(comment);
    }

    private boolean canCommentBeDeleted(User user, Comment comment) {
        if (comment.getUser().getId().equals(user.getId())) {
            return true;
        }

        if (user.getRole() == Role.ROLE_ADMIN) {
            return true;
        }

        return false;
    }

}
