package com.example.githubexplorer.controller;

import com.example.githubexplorer.model.Repo;
import com.example.githubexplorer.model.User;
import com.example.githubexplorer.service.GithubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GithubController {
    private final GithubService githubService;

    @GetMapping("/user/{login}")
    public User getUser(@PathVariable String login) {
        return githubService.getUserByLogin(login);
    }

    @GetMapping("/repos/{login}")
    public List<Repo> getAllReposByLogin(@PathVariable String login) {
        return githubService.getAllReposByLogin(login);
    }
}
