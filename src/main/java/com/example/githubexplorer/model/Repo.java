package com.example.githubexplorer.model;

import lombok.*;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Repo {
    private String name;
    private Map<String, Integer> languages;
}
