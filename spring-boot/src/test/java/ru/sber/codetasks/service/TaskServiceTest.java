package ru.sber.codetasks.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.expression.AccessException;
import ru.sber.codetasks.domain.Task;
import ru.sber.codetasks.domain.User;
import ru.sber.codetasks.domain.enums.Difficulty;
import ru.sber.codetasks.dto.comment.CreateCommentDto;
import ru.sber.codetasks.dto.comment.LikeUnlikeCommentDto;
import ru.sber.codetasks.dto.task.CreateUpdateTaskDto;
import ru.sber.codetasks.dto.testcase.NewTestCaseDto;
import ru.sber.codetasks.exception.CommentAlreadyLikedException;
import ru.sber.codetasks.mapper.TaskMapper;
import ru.sber.codetasks.repository.*;
import ru.sber.codetasks.service.implementation.TaskServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TaskServiceTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TestCaseRepository testCaseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProgrammingLanguageRepository programmingLanguageRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private EntityManager entityManager;

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        TaskMapper taskMapper = new TaskMapper(topicRepository, programmingLanguageRepository);
        taskService = new TaskServiceImpl(taskRepository,
                testCaseRepository,
                userRepository,
                taskMapper,
                commentRepository);

    }

    @AfterEach
    void tearDown() {
        entityManager.clear();
    }

    @Test
    void update_existing_task() {
        Long taskId = 1L;
        CreateUpdateTaskDto updateTaskDto = new CreateUpdateTaskDto();
        updateTaskDto.setName("New Task Name");
        updateTaskDto.setTopic("arrays");
        updateTaskDto.setCondition("cond");
        updateTaskDto.setDifficulty(Difficulty.EASY);
        updateTaskDto.setTestcases(List.of(new NewTestCaseDto("1", "2")));
        updateTaskDto.setLanguages(List.of("java"));

        taskService.updateTask(taskId, updateTaskDto);

        Task updatedTask = taskRepository.findById(taskId).get();
        assertEquals("New Task Name", updatedTask.getName());
    }

    @Test
    void update_not_existing_task() {
        Long nonExistingTaskId = 999L;
        CreateUpdateTaskDto updateTaskDto = new CreateUpdateTaskDto();
        updateTaskDto.setName("New Task Name");

        assertThrows(EntityNotFoundException.class, () -> taskService.updateTask(nonExistingTaskId, updateTaskDto));
    }

    @Test
    void add_comment_test() {
        CreateCommentDto commentDto = new CreateCommentDto();
        String text = "txt";
        commentDto.setText(text);

        taskService.addComment(1L, commentDto, "user");

        var comment = commentRepository.findById(3L).orElse(null);
        assertNotNull(comment);
        assertEquals(comment.getCommentText(), text);
    }

    @Test
    void delete_my_comment_by_user() throws AccessException {
        taskService.deleteComment(1L, "user");

        assertFalse(commentRepository.existsById(1L));
    }

    @Test
    void delete_others_comment_by_user() {
        assertThrows(AccessException.class, () -> taskService.deleteComment(2L, "user"));
    }

    @Test
    void delete_others_comment_by_admin() throws AccessException {
        taskService.deleteComment(1L, "user");

        assertFalse(commentRepository.existsById(1L));
    }

    @Test
    void like_comment_for_first_time() {
        LikeUnlikeCommentDto likeUnlikeCommentDto = new LikeUnlikeCommentDto();
        likeUnlikeCommentDto.setId(1L);
        taskService.likeComment(likeUnlikeCommentDto, "user");

        User user = userRepository.findByUsername("user").get();
        assertTrue(commentRepository.findById(1L).get().getUsersLiked().contains(user));
    }

    @Test
    void like_comment_for_second_time() {
        LikeUnlikeCommentDto likeUnlikeCommentDto = new LikeUnlikeCommentDto();
        likeUnlikeCommentDto.setId(1L);
        taskService.likeComment(likeUnlikeCommentDto, "user");

        assertThrows(CommentAlreadyLikedException.class, () -> taskService.likeComment(likeUnlikeCommentDto, "user"));
    }

    @Test
    void unlike_liked_comment() {
        LikeUnlikeCommentDto likeUnlikeCommentDto = new LikeUnlikeCommentDto();
        likeUnlikeCommentDto.setId(1L);
        taskService.likeComment(likeUnlikeCommentDto, "user");
        taskService.unlikeComment(likeUnlikeCommentDto, "user");

        User user = userRepository.findByUsername("user").get();
        assertFalse(commentRepository.findById(1L).get().getUsersLiked().contains(user));
    }

    @Test
    void unlike_not_liked_comment() {
        LikeUnlikeCommentDto likeUnlikeCommentDto = new LikeUnlikeCommentDto();
        likeUnlikeCommentDto.setId(1L);

        assertThrows(EntityNotFoundException.class, () -> taskService.unlikeComment(likeUnlikeCommentDto, "user"));
    }

}