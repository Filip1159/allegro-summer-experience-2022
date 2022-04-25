package com.example.githubexplorer.controller;

import com.example.githubexplorer.MockitoFakeApi;
import com.example.githubexplorer.model.User;
import com.example.githubexplorer.util.apiclient.IGithubApiClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.example.githubexplorer.MockitoFakeApi.johnDoe;

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

    @BeforeEach
    void setUp() {
        MockitoFakeApi.createMockApi(iGithubApiClient);
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