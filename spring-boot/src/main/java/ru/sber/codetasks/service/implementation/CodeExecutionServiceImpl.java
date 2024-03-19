package ru.sber.codetasks.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.sber.codetasks.dto.code_execution.ExecutionRequestDto;
import ru.sber.codetasks.dto.code_execution.ExecutionResultDto;
import ru.sber.codetasks.mapper.CodeExecutionResultMapper;
import ru.sber.codetasks.service.CodeExecutionService;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Map;

@Service
public class CodeExecutionServiceImpl implements CodeExecutionService {
    private static final String HOST_HEADER_KEY ="X-RapidAPI-Host";

    private static final String HOST_APIKEY_KEY ="X-RapidAPI-Key";

    private final String apiUrl;

    private final String apiHost;

    private final String apiKey;

    private final CodeExecutionResultMapper codeExecutionResultMapper;

    private HttpHeaders headers;


    public CodeExecutionServiceImpl(@Value("${custom-properties.executor.url}") String apiUrl,
                                    @Value("${custom-properties.executor.host}") String apiHost,
                                    @Value("${custom-properties.executor.key}") String apiKey,
                                    CodeExecutionResultMapper codeExecutionResultMapper) {
        this.apiUrl = apiUrl;
        this.apiHost = apiHost;
        this.apiKey = apiKey;
        this.codeExecutionResultMapper = codeExecutionResultMapper;
    }

    @PostConstruct
    private void init() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HOST_HEADER_KEY, apiHost);
        httpHeaders.set(HOST_APIKEY_KEY, apiKey);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        this.headers = httpHeaders;
    }

    public ExecutionResultDto executeCode(ExecutionRequestDto executionRequestDto) throws JsonProcessingException {
        var requestBody = buildRequestBody(executionRequestDto);
        var requestEntity = new HttpEntity<>(requestBody, headers);

        var restTemplate = new RestTemplate();
        String response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        ).getBody();

        return codeExecutionResultMapper.map(response);
    }

    private Map<String, Object> buildRequestBody(ExecutionRequestDto executionRequestDto) {
       return Map.of(
                "language", executionRequestDto.getLanguage(),
                "stdin", executionRequestDto.getStdin(),
                "files", Collections.singletonList(
                        Map.of(
                                "name", "solution.txt",
                                "content", executionRequestDto.getCode()
                        )
                )
        );
    }

}
