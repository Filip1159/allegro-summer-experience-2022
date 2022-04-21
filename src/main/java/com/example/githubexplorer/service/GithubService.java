package com.example.githubexplorer.service;

import com.example.githubexplorer.model.Repo;
import com.example.githubexplorer.model.User;
import com.example.githubexplorer.model.UserDto;
import com.example.githubexplorer.util.apiclient.GithubApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map.Entry;

@Service
@Slf4j
@RequiredArgsConstructor
public class GithubService {
    private final GithubApiClient githubApi;
    public User getUserByLogin(String login) {
        UserDto response = githubApi.getUserByLogin(login);
        List<String> repoNames = githubApi.getReposByLogin(login);
        Map<String, Integer> languagesOverall = new HashMap<>();
        for (String repo : repoNames) {
            Map<String, Integer> languages = githubApi.getLanguagesByRepo(login, repo);
            Set<Entry<String, Integer>> languagesEntrySet = languages.entrySet();
            for (Entry<String, Integer> entry : languagesEntrySet) {
                if (languagesOverall.containsKey(entry.getKey())) {
                    int currentValue = languagesOverall.get(entry.getKey());
                    languagesOverall.replace(entry.getKey(), currentValue + entry.getValue());
                } else {
                    languagesOverall.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return User.builder()
                .login(response.getLogin())
                .name(response.getName())
                .bio(response.getBio())
                .languages(languagesOverall)
                .build();
    }

    public List<Repo> getAllReposByLogin(String login) {
        List<String> repoNames = githubApi.getReposByLogin(login);
        List<Repo> result = new ArrayList<>();
        for (String repoName : repoNames) {
            Map<String, Integer> repoLanguages = githubApi.getLanguagesByRepo(login, repoName);
            Repo repo = Repo.builder()
                    .name(repoName)
                    .languages(repoLanguages)
                    .build();
            result.add(repo);
        }
        return result;
    }
}
