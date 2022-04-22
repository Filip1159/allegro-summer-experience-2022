package com.example.githubexplorer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDto {
    private String login;
    private String name;
    private String bio;
}
