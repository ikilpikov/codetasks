package ru.sber.codetasks.service;

import ru.sber.codetasks.dto.topic.ReducedTopicDto;
import ru.sber.codetasks.dto.topic.TopicDto;

import java.util.List;

public interface TopicService {
    void createTopic(ReducedTopicDto topicDto);

    void deleteTopic(Long id);

    ReducedTopicDto getTopic(Long id);

    List<TopicDto> getTopics();

    void updateTopic(Long id, ReducedTopicDto taskDto);

}
