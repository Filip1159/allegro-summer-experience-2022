package com.example.githubexplorer.service;

import com.example.githubexplorer.model.Repo;
import com.example.githubexplorer.model.User;
import com.example.githubexplorer.model.UserDto;
import com.example.githubexplorer.util.apiclient.IGithubApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GithubService {
    private final IGithubApiClient githubApi;

    public void login(String token) {
        githubApi.login(token);
    }

    public User getUserByLogin(String login, int languagesPage, int languagesPerPage) { // page number starts from 1
        UserDto userDto = githubApi.getUserByLogin(login).orElseThrow();
        List<String> repoNames = githubApi.getReposByLogin(login).orElseThrow();
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

        languagesOverall = languagesOverall.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .skip((long) (languagesPage-1) * languagesPerPage)
                .limit(languagesPerPage)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        System.out.println(languagesPage);
        log.info(Integer.toString(languagesPerPage));
        log.info(languagesOverall.toString());

        return User.builder()
                .login(userDto.getLogin())
                .name(userDto.getName())
                .bio(userDto.getBio())
                .languages(languagesOverall)
                .build();
    }

    public List<Repo> getAllReposByLogin(String login, int page, int pageSize) {
        List<String> repoNames = githubApi.getReposByLogin(login).orElseThrow();
        repoNames = repoNames.stream()
                .skip((long) (page-1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
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
