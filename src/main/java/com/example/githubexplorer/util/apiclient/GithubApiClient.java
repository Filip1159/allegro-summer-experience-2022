package com.example.githubexplorer.util.apiclient;

import com.example.githubexplorer.model.RepoDto;
import com.example.githubexplorer.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.Optional;

@Component
@SessionScope
public class GithubApiClient implements IGithubApiClient {
    private final String baseUrl;
    private WebClient client;

    @Autowired
    public GithubApiClient(@Value("${api.baseUrl}") String baseUrl) {
        this.baseUrl = baseUrl;
        client = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Override
    public Optional<UserDto> getUser(String login) {
        UserDto fetchedUser = client.get()
                .uri(uriBuilder -> uriBuilder.path("users/{login}").build(login))
                .retrieve()
                .bodyToMono(UserDto.class).block();
        return Optional.ofNullable(fetchedUser);
    }

    @Override
    public boolean doesLoginExist(String login) {
        ResponseEntity<Void> responseForUser = client.get()
                .uri(uriBuilder -> uriBuilder.path("users/{login}").build(login))
                .retrieve()
                .toBodilessEntity().block();
        if (responseForUser == null)
            throw new NullPointerException(String.format("Request for user %s ended with null", login));
        return responseForUser.getStatusCode().equals(HttpStatus.NOT_FOUND);
    }

    @Override
    public boolean doesRepoExist(String login, String repoName) {
        ResponseEntity<Void> responseForRepo = client.get()
                .uri(uriBuilder -> uriBuilder.path("repos/{login}/{repoName}").build(login, repoName))
                .retrieve()
                .toBodilessEntity().block();
        if (responseForRepo == null)
            throw new NullPointerException(String.format("Request for repo %s/%s ended with null", login, repoName));
        return responseForRepo.getStatusCode().equals(HttpStatus.NOT_FOUND);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Integer> getLanguages(String login, String repoName) {
        return client.get()
                .uri(uriBuilder -> uriBuilder.path("repos/{login}/{repoName}/languages").build(login, repoName))
                .retrieve()
                .bodyToMono(Map.class).block();
    }

    @Override
    public Optional<RepoDto[]> getReposPage(String login, int page, int pageSize) {
        if (pageSize > 100)
            throw new IllegalArgumentException("Github api will not handle page size bigger than 100");
        RepoDto[] fetchedRepos = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("users/{login}/repos")
                        .queryParam("page", "{page}")
                        .queryParam("per_page", "{pageSize}")
                        .build(login, page, pageSize))
                .retrieve()
                .bodyToMono(RepoDto[].class).block();
        return Optional.ofNullable(fetchedRepos);
    }

    @Override
    public void login(String token) {
        client = WebClient.builder().defaultHeader(HttpHeaders.AUTHORIZATION, token).baseUrl(baseUrl).build();
    }

    @Override
    public void logout() {
        client = WebClient.builder().baseUrl(baseUrl).build();
    }
}
