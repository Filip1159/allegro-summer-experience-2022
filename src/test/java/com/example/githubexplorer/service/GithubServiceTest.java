package com.example.githubexplorer.service;

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

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class GithubServiceTest {
    @InjectMocks
    GithubService githubService;

    @Mock
    IGithubApiClient iGithubApiClient;

    @BeforeEach
    void createMockApi() {
        Mockito.when(iGithubApiClient.getUserByLogin("john-doe")).thenReturn(
                Optional.of(new UserDto("john-doe", "John Doe", "Fake user account created by mockito"))
        );
        Mockito.when(iGithubApiClient.getReposByLogin("john-doe")).thenReturn(
                Optional.of(List.of("repo1", "repo2", "repo3", "repo4", "repo5", "repo6", "repo7", "repo8"))
        );
        Mockito.when(iGithubApiClient.getLanguagesByRepo("john-doe", "repo1")).thenReturn(
                Map.of("Python", 100)
        );
        Mockito.when(iGithubApiClient.getLanguagesByRepo("john-doe", "repo2")).thenReturn(
                Map.of("C", 150, "C++", 200)
        );
        Mockito.when(iGithubApiClient.getLanguagesByRepo("john-doe", "repo3")).thenReturn(
                Map.of("JavaScript", 110, "TypeScript", 150)
        );
        Mockito.when(iGithubApiClient.getLanguagesByRepo("john-doe", "repo4")).thenReturn(
                Map.of("Java", 300, "Groovy", 50, "Scala", 50)
        );
        Mockito.when(iGithubApiClient.getLanguagesByRepo("john-doe", "repo5")).thenReturn(
                Map.of("Java", 160)
        );
        Mockito.when(iGithubApiClient.getLanguagesByRepo("john-doe", "repo6")).thenReturn(
                Map.of("HTML", 100, "CSS", 130, "JavaScript", 380)
        );
        Mockito.when(iGithubApiClient.getLanguagesByRepo("john-doe", "repo7")).thenReturn(
                Map.of("TypeScript", 155)
        );
        Mockito.when(iGithubApiClient.getLanguagesByRepo("john-doe", "repo8")).thenReturn(
                Map.of("Scala", 120)
        );
    }

    @Test
    void shouldReturnJohnDoeDetailsAndLanguages() {
        User user = githubService.getUserByLogin("john-doe", 1, 20);
        Map<String, Integer> expectedLanguages = Map.of(
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
        User expectedUser = User.builder()
                .login("john-doe")
                .name("John Doe")
                .bio("Fake user account created by mockito")
                .languages(expectedLanguages).build();
        Assertions.assertEquals(expectedUser, user);
    }

    @Test
    void shouldReturnJohnDoeDetailsAndLanguagesWithPaging() {
        User user = githubService.getUserByLogin("john-doe", 1, 5);
        Map<String, Integer> expectedLanguages = Map.of("JavaScript", 490, "Java", 460, "TypeScript", 305, "C++", 200, "Scala", 170);
        User expectedUser = User.builder()
                .login("john-doe")
                .name("John Doe")
                .bio("Fake user account created by mockito")
                .languages(expectedLanguages).build();
        Assertions.assertEquals(expectedUser, user);

        user = githubService.getUserByLogin("john-doe", 2, 3);
        expectedLanguages = Map.of("C++", 200, "Scala", 170, "C", 150);
        expectedUser = User.builder()
                .login("john-doe")
                .name("John Doe")
                .bio("Fake user account created by mockito")
                .languages(expectedLanguages).build();
        Assertions.assertEquals(expectedUser, user);

        user = githubService.getUserByLogin("john-doe", 3, 4);
        expectedLanguages = Map.of("Python", 100, "Groovy", 50);
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
        Assertions.assertThrows(NoSuchElementException.class, () -> githubService.getAllReposByLogin("xyz"));
    }

}