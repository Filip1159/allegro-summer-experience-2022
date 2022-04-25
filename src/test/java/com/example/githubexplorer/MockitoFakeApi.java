package com.example.githubexplorer;

import com.example.githubexplorer.model.Repo;
import com.example.githubexplorer.model.RepoDto;
import com.example.githubexplorer.model.UserDto;
import com.example.githubexplorer.util.apiclient.IGithubApiClient;
import org.jetbrains.annotations.NotNull;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;

public class MockitoFakeApi {
    public static final List<Repo> johnDoeRepos = List.of(
            new Repo("repo1", Map.of("Python", 100)),
            new Repo("repo2", Map.of("C", 150, "C++", 200)),
            new Repo("repo3", Map.of("JavaScript", 110, "TypeScript", 150)),
            new Repo("repo4", Map.of("Java", 300, "Groovy", 50, "Scala", 50)),
            new Repo("repo5", Map.of("Java", 160)),
            new Repo("repo6", Map.of("HTML", 100, "CSS", 130, "JavaScript", 380)),
            new Repo("repo7", Map.of("TypeScript", 155)),
            new Repo("repo8", Map.of("Scala", 120)));

    public static final Map<String, Integer> userLanguages = Map.of(
            "JavaScript", 490,
            "Java", 460,
            "TypeScript", 305,
            "C++", 200,
            "Scala", 170,
            "C", 150,
            "CSS", 130,
            "HTML", 100,
            "Python", 100,
            "Groovy", 50);

    public static final UserDto johnDoe = new UserDto("john-doe", "John Doe", "Fake user account created by mockito", 8);

    public static final RepoDto[] repoDtos = johnDoeRepos.stream()
            .map(repo -> new RepoDto(repo.getName()))
            .toArray(RepoDto[]::new);

    public static void createMockApi(@NotNull IGithubApiClient iGithubApiClient) {
        Mockito.when(iGithubApiClient.getUser("john-doe")).thenReturn(
                Optional.of(johnDoe)
        );
        Mockito.when(iGithubApiClient.getReposPage(eq("john-doe"), anyInt(), anyInt())).thenAnswer(invocationOnMock -> {
                    Object[] args = invocationOnMock.getArguments();
                    int page = (int) args[1];
                    int pageSize = (int) args[2];
                    RepoDto[] repoNamesPage = Arrays.stream(repoDtos)
                            .skip((long) (page - 1) * pageSize)
                            .limit(pageSize)
                            .toArray(RepoDto[]::new);
                    return Optional.of(repoNamesPage);
                }
        );
        Mockito.when(iGithubApiClient.getLanguages("john-doe", "repo1")).thenReturn(
                johnDoeRepos.get(0).getLanguages()
        );
        Mockito.when(iGithubApiClient.getLanguages("john-doe", "repo2")).thenReturn(
                johnDoeRepos.get(1).getLanguages()
        );
        Mockito.when(iGithubApiClient.getLanguages("john-doe", "repo3")).thenReturn(
                johnDoeRepos.get(2).getLanguages()
        );
        Mockito.when(iGithubApiClient.getLanguages("john-doe", "repo4")).thenReturn(
                johnDoeRepos.get(3).getLanguages()
        );
        Mockito.when(iGithubApiClient.getLanguages("john-doe", "repo5")).thenReturn(
                johnDoeRepos.get(4).getLanguages()
        );
        Mockito.when(iGithubApiClient.getLanguages("john-doe", "repo6")).thenReturn(
                johnDoeRepos.get(5).getLanguages()
        );
        Mockito.when(iGithubApiClient.getLanguages("john-doe", "repo7")).thenReturn(
                johnDoeRepos.get(6).getLanguages()
        );
        Mockito.when(iGithubApiClient.getLanguages("john-doe", "repo8")).thenReturn(
                johnDoeRepos.get(7).getLanguages()
        );
    }

}
