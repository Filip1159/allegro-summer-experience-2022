package com.example.githubexplorer.util.apiclient;

import com.example.githubexplorer.model.UserDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IGithubApiClient {
    Optional<UserDto> getUserByLogin(String login);
    Map<String, Integer> getLanguagesByRepo(String login, String repoName);
    Optional<List<String>> getReposByLogin(String login);
}
