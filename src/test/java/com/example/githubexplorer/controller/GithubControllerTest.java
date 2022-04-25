package com.example.githubexplorer.controller;

import com.example.githubexplorer.model.Repo;
import com.example.githubexplorer.model.User;
import com.example.githubexplorer.model.UserDto;
import com.example.githubexplorer.util.apiclient.IGithubApiClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
class GithubControllerTest {
    @MockBean
    IGithubApiClient iGithubApiClient;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    final List<Repo> johnDoeRepos = List.of(
            new Repo("repo1", Map.of("Python", 100)),
            new Repo("repo2", Map.of("C", 150, "C++", 200)),
            new Repo("repo3", Map.of("JavaScript", 110, "TypeScript", 150)),
            new Repo("repo4", Map.of("Java", 300, "Groovy", 50, "Scala", 50)),
            new Repo("repo5", Map.of("Java", 160)),
            new Repo("repo6", Map.of("HTML", 100, "CSS", 130, "JavaScript", 380)),
            new Repo("repo7", Map.of("TypeScript", 155)),
            new Repo("repo8", Map.of("Scala", 120))
    );

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
        Mockito.when(iGithubApiClient.getReposByLogin("john-doe", 1, 8)).thenReturn(
                Optional.of(repoNames)
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
    void shouldGetJohnDoeWithAllLanguages() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/user/john-doe"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
        User user = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), User.class);
        Assertions.assertEquals("john-doe", user.getLogin());
        Assertions.assertEquals("John Doe", user.getName());
        Assertions.assertEquals(johnDoe.getBio(), user.getBio());
    }
}