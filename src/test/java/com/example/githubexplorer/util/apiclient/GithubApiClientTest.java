package com.example.githubexplorer.util.apiclient;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class GithubApiClientTest {
    private MockWebServer server;
    private GithubApiClient githubApi;

    @BeforeEach
    void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
        String mockUrl = server.url("/").toString();
        githubApi = new GithubApiClient(mockUrl);
    }

    @Test
    void shouldRequestForJohnDoeRepos() throws InterruptedException {
        MockResponse response = new MockResponse();
        server.enqueue(response);
        githubApi.getLanguages("john-doe", "repo1");
        RecordedRequest request = server.takeRequest();
        assertEquals("GET", request.getMethod());
        assertEquals("/repos/john-doe/repo1/languages", request.getPath());
    }

    @AfterEach
    void tearDown() throws IOException {
        server.shutdown();
    }

}