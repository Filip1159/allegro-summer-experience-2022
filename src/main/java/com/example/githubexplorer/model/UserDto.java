package com.example.githubexplorer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String login;
    private String name;
    private String bio;
    private int publicRepos;
}
