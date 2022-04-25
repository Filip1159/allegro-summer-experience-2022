package com.example.githubexplorer.service;

import com.example.githubexplorer.model.Repo;
import com.example.githubexplorer.model.RepoDto;
import com.example.githubexplorer.model.User;
import com.example.githubexplorer.model.UserDto;
import com.example.githubexplorer.util.apiclient.IGithubApiClient;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Service
@RequiredArgsConstructor
public class GithubService {
    private final IGithubApiClient githubApi;

    public User getUser(String login, int languagesPage, int languagesPerPage) {
        if (languagesPage < 1)
            throw new IllegalArgumentException("Number of languages per page positive, provided: " + languagesPage);
        UserDto userDto = githubApi.getUser(login).orElseThrow();
        List<RepoDto> repoDtos = getAllRepoDtos(userDto);
        User user = addRepos(userDto, repoDtos);
        if (languagesPerPage >= 0)
            user.spliceLanguagesPage(languagesPage, languagesPerPage);
        return user;
    }

    public List<Repo> getReposPage(String login, int page, int pageSize) {
        RepoDto[] repoDtos = githubApi.getReposPage(login, page, pageSize).orElseThrow();
        List<Repo> result = new ArrayList<>();
        for (RepoDto repoDto : repoDtos) {
            Map<String, Integer> repoLanguages = githubApi.getLanguages(login, repoDto.getName());
            result.add(new Repo(repoDto.getName(), repoLanguages));
        }
        return result;
    }

    public User getUser(String login) {
        return getUser(login, 1, -1);
    }

    public void login(String token) {
        githubApi.login(token);
    }

    public void logout() {
        githubApi.logout();
    }

    @NonNull
    private List<RepoDto> getAllRepoDtos(@NonNull UserDto userDto) {
        List<RepoDto> repoNames = new ArrayList<>();
        for (int i=0; i<userDto.getPublicRepos(); i+=100) {
            RepoDto[] fetchedRepos = githubApi.getReposPage(userDto.getLogin(), i/100+1, 100).orElseThrow();
            repoNames.addAll(List.of(fetchedRepos));
        }
        return repoNames;
    }

    @NonNull
    private User addRepos(@NonNull UserDto userDto, @NonNull List<RepoDto> repoDtos) {
        User user = User.of(userDto);
        for (RepoDto repoDto : repoDtos) {
            Map<String, Integer> languages = githubApi.getLanguages(user.getLogin(), repoDto.getName());
            for (Entry<String, Integer> entry : languages.entrySet())
                user.addNewLanguageBytes(entry.getKey(), entry.getValue());
        }
        return user;
    }
}
