package com.example.githubexplorer.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RepoDto {
    private String name;

    public RepoDto(String name) {
        this.name = name;
    }
}
