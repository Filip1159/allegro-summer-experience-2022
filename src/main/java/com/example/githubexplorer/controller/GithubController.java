package com.example.githubexplorer.controller;

import com.example.githubexplorer.model.Repo;
import com.example.githubexplorer.model.User;
import com.example.githubexplorer.service.GithubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GithubController {
    private final GithubService githubService;

    @GetMapping("/login")
    public void login(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        githubService.login(token);
    }

    @GetMapping("/user/{login}")
    public User getUser(@PathVariable String login,
                        @RequestParam(required = false) Integer languagesPage,
                        @RequestParam(required = false) Integer languagesPerPage) {
        if (languagesPage == null) languagesPage = 1;
        if (languagesPerPage == null) languagesPerPage = 10;
        return githubService.getUserByLogin(login, languagesPage, languagesPerPage);
    }

    @GetMapping("/repos/{login}")
    public List<Repo> getAllReposByLogin(@PathVariable String login,
                                         @RequestParam(required = false) Integer page,
                                         @RequestParam(required = false) Integer pageSize) {
        if (page == null) page = 1;
        if (pageSize == null) pageSize = 10;
        return githubService.getAllReposByLogin(login, page, pageSize);
    }
}
