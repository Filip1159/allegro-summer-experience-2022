package com.example.githubexplorer.util.apiclient;

import com.example.githubexplorer.model.RepoDto;
import com.example.githubexplorer.model.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class GithubApiClient implements IGithubApiClient {
    @Value("${api.baseUrl}")
    private String baseUrl;
    private final RestTemplate template = new RestTemplate();

    @Override
    public Optional<UserDto> getUserByLogin(String login) {
        UserDto fetchedUser = template.getForObject(baseUrl + "users/{login}", UserDto.class, login);
        return Optional.ofNullable(fetchedUser);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Integer> getLanguagesByRepo(String login, String repoName) {
        ResponseEntity<Object> responseForUser = template.exchange(baseUrl + "users/" + login, HttpMethod.GET, null, Object.class);
        if (responseForUser.getStatusCode().equals(HttpStatus.NOT_FOUND))
            throw new NoSuchElementException("Given login (" + login + ") not found");
        ResponseEntity<Object> responseForRepo = template.exchange(baseUrl + "users/" + login + "/" + repoName, HttpMethod.GET, null, Object.class);
        if (responseForRepo.getStatusCode().equals(HttpStatus.NOT_FOUND))
            throw new NoSuchElementException("Given repo (" + repoName + ") not found");
        return template.getForObject(baseUrl + "repos/{login}/{repoName}/languages", Map.class, login, repoName);
    }

    @Override
    public Optional<List<String>> getReposByLogin(String login) {
        RepoDto[] fetchedRepos = template.getForObject(baseUrl + "users/{login}/repos", RepoDto[].class, login);
        if (fetchedRepos == null) return Optional.empty();
        ArrayList<RepoDto> fetchedReposAsList = new ArrayList<>(Arrays.asList(fetchedRepos));
        List<String> repoNames = fetchedReposAsList.stream()
                .map(RepoDto::getName)
                .collect(Collectors.toList());
        return Optional.of(repoNames);
    }
}
