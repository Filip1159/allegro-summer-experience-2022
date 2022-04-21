package com.example.githubexplorer.service;

import com.example.githubexplorer.model.UserDto;
import com.example.githubexplorer.util.apiclient.GithubApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GithubService {
    private final GithubApiClient githubApi;
    public UserDto getUserByLogin(String login) {
        UserDto response = githubApi.getUserByLogin(login);
        log.info(response.toString());
        return response;
    }
}
