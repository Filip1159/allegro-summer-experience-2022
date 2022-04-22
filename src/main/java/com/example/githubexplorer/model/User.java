package com.example.githubexplorer.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class User {
    private String login;
    private String name;
    private String bio;
    private Map<String, Integer> languages;
}
