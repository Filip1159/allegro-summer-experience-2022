package com.example.githubexplorer.model;

import lombok.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String login;
    private String name;
    private String bio;
    private Map<String, Integer> languages;

    public User(String login, String name, String bio) {
        this.login = login;
        this.name = name;
        this.bio = bio;
        this.languages = new HashMap<>();
    }

    public void addNewLanguageBytes(String language, int bytes) {
        if (languages.containsKey(language)) {
            int currentBytes = languages.get(language);
            languages.replace(language, currentBytes + bytes);
        } else {
            languages.put(language, bytes);
        }
    }

    public void spliceLanguagesPage(int page, int pageSize) {
        languages = languages.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .skip((long) (page-1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @NonNull
    public static User of(@NonNull UserDto userDto) {
        return new User(userDto.getLogin(), userDto.getName(), userDto.getBio());
    }
}
