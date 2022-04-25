package com.example.githubexplorer.controller;

import com.example.githubexplorer.model.Repo;
import com.example.githubexplorer.model.User;
import com.example.githubexplorer.service.GithubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GithubController {
    private final GithubService githubService;

    @PostMapping("/login")
    public void login(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        githubService.login(token);
    }

    @PostMapping("/logout")
    public void logout() {
        githubService.logout();
    }

    @GetMapping("/user/{login}")
    public User getUser(@PathVariable String login,
                        @RequestParam(required = false) Integer languagesPage,
                        @RequestParam(required = false) Integer languagesPerPage) {
        if (shouldUsePagination(languagesPage, languagesPerPage)) {
            if (languagesPerPage == null) languagesPerPage = 10;
            return githubService.getUser(login, languagesPage, languagesPerPage);
        } else return githubService.getUser(login);
    }

    @GetMapping("/repos/{login}")
    public List<Repo> getRepos(@PathVariable String login,
                                         @RequestParam(required = false) Integer page,
                                         @RequestParam(required = false) Integer pageSize) {
        if (page == null) page = 1;
        if (pageSize == null) pageSize = 10;
        return githubService.getReposPage(login, page, pageSize);
    }

    private boolean shouldUsePagination(@Nullable Integer page, @Nullable Integer pageSize) {
        return page != null && page > 1 && (pageSize == null || pageSize >= 1);
    }
}
