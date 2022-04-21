package com.example.githubexplorer.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class Repo {
    private String name;
    private Map<String, Integer> languages;
}
