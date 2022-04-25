package com.example.githubexplorer.util.apiclient;

import com.example.githubexplorer.model.RepoDto;
import com.example.githubexplorer.model.UserDto;

import java.util.Map;
import java.util.Optional;

public interface IGithubApiClient {
    void login(String token);
    void logout();
    Optional<UserDto> getUser(String login);
    boolean doesLoginExist(String login);
    boolean doesRepoExist(String login, String repoName);
    Map<String, Integer> getLanguages(String login, String repoName);
    Optional<RepoDto[]> getReposPage(String login, int page, int pageSize);
}
