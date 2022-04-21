package com.example.githubexplorer.controller;

import com.example.githubexplorer.model.UserDto;
import com.example.githubexplorer.service.GithubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GithubController {
    private final GithubService githubService;

    @GetMapping("/user/{login}")
    public UserDto getUser(@PathVariable String login) {
        return githubService.getUserByLogin(login);
    }
}
