package com.example.githubexplorer.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class User {
    private String login;
    private String name;
    private String bio;
    private Map<String, Integer> languages;
}
