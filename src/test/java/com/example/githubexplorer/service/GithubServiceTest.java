package com.example.githubexplorer.service;

import com.example.githubexplorer.MockitoFakeApi;
import com.example.githubexplorer.model.Repo;
import com.example.githubexplorer.model.User;
import com.example.githubexplorer.util.apiclient.IGithubApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.example.githubexplorer.MockitoFakeApi.johnDoeRepos;
import static com.example.githubexplorer.MockitoFakeApi.userLanguages;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class GithubServiceTest {
    @InjectMocks
    GithubService githubService;

    @Mock
    IGithubApiClient iGithubApiClient;

    @BeforeEach
    void setUp() {
        MockitoFakeApi.createMockApi(iGithubApiClient);
    }

    @Test
    void shouldReturnJohnDoeDetailsAndLanguages() {
        User user = githubService.getUser("john-doe", 1, 20);
        User expectedUser = User.builder()
                .login("john-doe")
                .name("John Doe")
                .bio("Fake user account created by mockito")
                .languages(userLanguages).build();
        Assertions.assertEquals(expectedUser, user);
    }

    @Test
    void shouldReturnJohnDoeDetailsAndLanguagesWithPaging() {
        User user = githubService.getUser("john-doe", 1, 5);
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

        user = githubService.getUser("john-doe", 2, 3);
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

        user = githubService.getUser("john-doe", 3, 4);
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
        Assertions.assertThrows(IllegalArgumentException.class, () -> githubService.getUser("john-doe", 0, 2));
    }

    @Test
    void shouldThrowWhenLoginDoesNotExist() {
        Assertions.assertThrows(NoSuchElementException.class, () -> githubService.getUser("abc", 1, 1));
        Assertions.assertThrows(NoSuchElementException.class, () -> githubService.getReposPage("xyz", 1, 1));
    }

    @Test
    void shouldFetchAllReposByLogin() {
        List<Repo> fetchedRepos = githubService.getReposPage("john-doe", 1, 10);
        Assertions.assertEquals(johnDoeRepos, fetchedRepos);
    }

}