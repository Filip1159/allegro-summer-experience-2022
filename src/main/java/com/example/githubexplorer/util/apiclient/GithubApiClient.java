package com.example.githubexplorer.util.apiclient;

import com.example.githubexplorer.model.RepoDto;
import com.example.githubexplorer.model.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.stream.Collectors;

@Component
@SessionScope
public class GithubApiClient implements IGithubApiClient {
    @Value("${api.baseUrl}")
    private String baseUrl;
    WebClient client = WebClient.builder()
            .baseUrl(baseUrl)
            .build();

    @Override
    public void login(String token) {
        client = WebClient.builder()
                .defaultHeader(HttpHeaders.AUTHORIZATION, token)
                .baseUrl(baseUrl)
                .build();
    }

    @Override
    public void logout() {
        client = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Override
    public Optional<UserDto> getUserByLogin(String login) {
        UserDto fetchedUser = client.get()
                .uri("https://api.github.com/users/{login}", login)
                .retrieve()
                .bodyToMono(UserDto.class).block();
        return Optional.ofNullable(fetchedUser);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Integer> getLanguagesByRepo(String login, String repoName) {
        ResponseEntity<Void> responseForUser = client.get()
                .uri("https://api.github.com/users/{login}", login)
                .retrieve()
                .toBodilessEntity().block();
        if (responseForUser == null || responseForUser.getStatusCode().equals(HttpStatus.NOT_FOUND))
            throw new NoSuchElementException("Given login (" + login + ") not found");
        ResponseEntity<Void> responseForRepo = client.get()
                .uri("https://api.github.com/repos/{login}/{repoName}", login, repoName)
                .retrieve()
                .toBodilessEntity().block();
        if (responseForRepo == null || responseForRepo.getStatusCode().equals(HttpStatus.NOT_FOUND))
            throw new NoSuchElementException("Given repo (" + repoName + ") not found");
        return client.get()
                .uri("https://api.github.com/repos/{login}/{repoName}/languages", login, repoName)
                .retrieve()
                .bodyToMono(Map.class).block();
    }

    @Override
    public Optional<List<String>> getReposByLogin(String login) {
        RepoDto[] fetchedRepos = client.get()
                .uri("https://api.github.com/users/{login}/repos", login)
                .retrieve()
                .bodyToMono(RepoDto[].class).block();
        if (fetchedRepos == null) return Optional.empty();
        List<String> repoNames = Arrays.stream(fetchedRepos)
                .map(RepoDto::getName)
                .collect(Collectors.toList());
        return Optional.of(repoNames);
    }
}
