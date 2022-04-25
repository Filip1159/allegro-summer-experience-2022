package com.example.githubexplorer.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDto {
    private String login;
    private String name;
    private String bio;
    @JsonAlias("public_repos")
    private int publicRepos;
}
