package ru.sber.codetasks.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import ru.sber.codetasks.dto.code_execution.ExecutionResultDto;

@Component
public class CodeExecutionResultMapper {
    public ExecutionResultDto map(String executionResultJson) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(executionResultJson);

        ExecutionResultDto executionResultDto = new ExecutionResultDto();
        executionResultDto.setStatus(jsonNode.get("status").asText());
        executionResultDto.setStdout(jsonNode.get("stdout").asText());
        executionResultDto.setStderr(jsonNode.get("stderr") == null ? null : jsonNode.get("stderr").asText());
        executionResultDto.setExecutionTime(jsonNode.get("executionTime").asInt());
        executionResultDto.setStdin(jsonNode.get("stdin").asText());

        return executionResultDto;
    }
}
