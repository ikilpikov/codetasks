package ru.sber.codetasks.service.implementation;

import org.springframework.stereotype.Service;
import ru.sber.codetasks.domain.Topic;
import ru.sber.codetasks.dto.topic.ReducedTopicDto;
import ru.sber.codetasks.dto.topic.TopicDto;
import ru.sber.codetasks.repository.TopicRepository;
import ru.sber.codetasks.service.TopicService;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopicServiceImpl implements TopicService {
    private TopicRepository topicRepository;

    public TopicServiceImpl(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @Override
    public void createTopic(ReducedTopicDto topicDto) {
        var topic = new Topic();
        topic.setName(topicDto.getName());
        topicRepository.save(topic);
    }

    @Override
    public void deleteTopic(Long id) {
        if (!topicRepository.existsById(id)) {
            throw new EntityNotFoundException("Topic not found: " + id);
        }
        topicRepository.deleteById(id);
    }

    @Override
    public ReducedTopicDto getTopic(Long id) {
        var topic = topicRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Topic not found: " + id));

        return new ReducedTopicDto(topic.getName());
    }

    @Override
    public List<TopicDto> getTopics() {
        return topicRepository.findAll()
                .stream()
                .map(topic -> new TopicDto(topic.getId(), topic.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public void updateTopic(Long id, ReducedTopicDto topicDto) {
        var topic = topicRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Topic not found: " + id));

        topic.setName(topicDto.getName());
        topicRepository.save(topic);
    }

}
