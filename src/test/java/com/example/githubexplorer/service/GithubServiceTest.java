package com.example.githubexplorer.service;

import com.example.githubexplorer.model.Repo;
import com.example.githubexplorer.model.User;
import com.example.githubexplorer.model.UserDto;
import com.example.githubexplorer.util.apiclient.IGithubApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class GithubServiceTest {
    @InjectMocks
    GithubService githubService;

    @Mock
    IGithubApiClient iGithubApiClient;

    final List<Repo> johnDoeRepos = List.of(
            new Repo("repo1", Map.of("Python", 100)),
            new Repo("repo2", Map.of("C", 150, "C++", 200)),
            new Repo("repo3", Map.of("JavaScript", 110, "TypeScript", 150)),
            new Repo("repo4", Map.of("Java", 300, "Groovy", 50, "Scala", 50)),
            new Repo("repo5", Map.of("Java", 160)),
            new Repo("repo6", Map.of("HTML", 100, "CSS", 130, "JavaScript", 380)),
            new Repo("repo7", Map.of("TypeScript", 155)),
            new Repo("repo8", Map.of("Scala", 120)));

    final List<String> repoNames = johnDoeRepos.stream()
            .map(Repo::getName)
            .collect(Collectors.toList());

    final Map<String, Integer> userLanguages = Map.of(
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

    final UserDto johnDoe = new UserDto("john-doe", "John Doe", "Fake user account created by mockito", 8);

    @BeforeEach
    void createMockApi() {
        Mockito.when(iGithubApiClient.getUserByLogin("john-doe")).thenReturn(
                Optional.of(johnDoe)
        );
        Mockito.when(iGithubApiClient.getReposByLogin(eq("john-doe"), anyInt(), anyInt())).thenAnswer(invocationOnMock -> {
                    Object[] args = invocationOnMock.getArguments();
                    int page = (int) args[1];
                    int pageSize = (int) args[2];
                    List<String> repoNamesPage = repoNames.stream()
                            .skip((long) (page - 1) * pageSize)
                            .limit(pageSize)
                            .collect(Collectors.toList());
                    return Optional.of(repoNamesPage);
                }
        );
        Mockito.when(iGithubApiClient.getLanguagesByRepo("john-doe", "repo1")).thenReturn(
                johnDoeRepos.get(0).getLanguages()
        );
        Mockito.when(iGithubApiClient.getLanguagesByRepo("john-doe", "repo2")).thenReturn(
                johnDoeRepos.get(1).getLanguages()
        );
        Mockito.when(iGithubApiClient.getLanguagesByRepo("john-doe", "repo3")).thenReturn(
                johnDoeRepos.get(2).getLanguages()
        );
        Mockito.when(iGithubApiClient.getLanguagesByRepo("john-doe", "repo4")).thenReturn(
                johnDoeRepos.get(3).getLanguages()
        );
        Mockito.when(iGithubApiClient.getLanguagesByRepo("john-doe", "repo5")).thenReturn(
                johnDoeRepos.get(4).getLanguages()
        );
        Mockito.when(iGithubApiClient.getLanguagesByRepo("john-doe", "repo6")).thenReturn(
                johnDoeRepos.get(5).getLanguages()
        );
        Mockito.when(iGithubApiClient.getLanguagesByRepo("john-doe", "repo7")).thenReturn(
                johnDoeRepos.get(6).getLanguages()
        );
        Mockito.when(iGithubApiClient.getLanguagesByRepo("john-doe", "repo8")).thenReturn(
                johnDoeRepos.get(7).getLanguages()
        );
    }

    @Test
    void shouldReturnJohnDoeDetailsAndLanguages() {
        User user = githubService.getUserByLogin("john-doe", 1, 20);
        User expectedUser = User.builder()
                .login("john-doe")
                .name("John Doe")
                .bio("Fake user account created by mockito")
                .languages(userLanguages).build();
        Assertions.assertEquals(expectedUser, user);
    }

    @Test
    void shouldReturnJohnDoeDetailsAndLanguagesWithPaging() {
        User user = githubService.getUserByLogin("john-doe", 1, 5);
        Map<String, Integer> expectedLanguages = Map.of(
                "JavaScript", 490,
                "Java", 460,
                "TypeScript", 305,
                "C++", 200,
                "Scala", 170);
        User expectedUser = User.builder()
                .login("john-doe")
                .name("John Doe")
                .bio("Fake user account created by mockito")
                .languages(expectedLanguages).build();
        Assertions.assertEquals(expectedUser, user);

        user = githubService.getUserByLogin("john-doe", 2, 3);
        expectedLanguages = Map.of(
                "C++", 200,
                "Scala", 170,
                "C", 150);
        expectedUser = User.builder()
                .login("john-doe")
                .name("John Doe")
                .bio("Fake user account created by mockito")
                .languages(expectedLanguages).build();
        Assertions.assertEquals(expectedUser, user);

        user = githubService.getUserByLogin("john-doe", 3, 4);
        expectedLanguages = Map.of(
                "Python", 100,
                "Groovy", 50);
        expectedUser = User.builder()
                .login("john-doe")
                .name("John Doe")
                .bio("Fake user account created by mockito")
                .languages(expectedLanguages).build();
        Assertions.assertEquals(expectedUser, user);
    }

    @Test
    void shouldThrowWhenIllegalPagingDetails() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> githubService.getUserByLogin("john-doe", 0, 2));
        Assertions.assertThrows(IllegalArgumentException.class, () -> githubService.getUserByLogin("john-doe", 3, -1));
    }

    @Test
    void shouldThrowWhenLoginDoesNotExist() {
        Assertions.assertThrows(NoSuchElementException.class, () -> githubService.getUserByLogin("abc", 1, 1));
        Assertions.assertThrows(NoSuchElementException.class, () -> githubService.getAllReposByLogin("xyz", 1, 1));
    }

    @Test
    void shouldFetchAllReposByLogin() {
        List<Repo> fetchedRepos = githubService.getAllReposByLogin("john-doe", 1, 10);
        Assertions.assertEquals(johnDoeRepos, fetchedRepos);
    }

}