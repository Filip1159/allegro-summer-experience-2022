package com.example.githubexplorer.util.apiclient;

import com.example.githubexplorer.model.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GithubApiClient {
    @Value("${api.baseUrl}")
    private String baseUrl;
    private final RestTemplate template = new RestTemplate();

    public UserDto getUserByLogin(String login) {
        return template.getForObject(baseUrl + "users/{login}", UserDto.class, login);
    }
}
