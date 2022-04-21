package com.example.githubexplorer.util.apiclient;

import com.example.githubexplorer.model.RepoDto;
import com.example.githubexplorer.model.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GithubApiClient {
    @Value("${api.baseUrl}")
    private String baseUrl;
    private final RestTemplate template = new RestTemplate();

    public UserDto getUserByLogin(String login) {
        return template.getForObject(baseUrl + "users/{login}", UserDto.class, login);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Integer> getLanguagesByRepo(String login, String repoName) {
        return template.getForObject(baseUrl + "repos/{login}/{repoName}/languages", Map.class, login, repoName);
    }

    public List<String> getReposByLogin(String login) {
        RepoDto[] reposDto = template.getForObject(baseUrl + "users/{login}/repos", RepoDto[].class, login);
        if (reposDto == null) throw new NullPointerException("Request for user repos ended with null response");
        ArrayList<RepoDto> list = new ArrayList<>(Arrays.asList(reposDto));
        return list.stream()
                .map(RepoDto::getName)
                .collect(Collectors.toList());
    }
}
