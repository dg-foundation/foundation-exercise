package com.foundation.mbta.data.reader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foundation.mbta.data.MbtaDataApplication;
import com.foundation.mbta.data.model.Route;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes={MbtaDataApplication.class, MbtaAPIReader.class})
public class MbtaAPIReaderTest {

    @Autowired
    private WebTestClient webTestClient;

    private static  MockWebServer mockBackEnd;

    MbtaAPIReader reader;
    ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s",
                mockBackEnd.getPort());
        reader = new MbtaAPIReader();
        reader.initialize(baseUrl);
    }

    @Test
    public void invokeRouteApi_thenGetRoute() throws JsonProcessingException {
        Route r1 = new Route("id1", "B-Line", "Blue Line");
        String json = mapper.writeValueAsString(r1);
        MockResponse mockResponse = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(json)
                .throttleBody(16, 5, TimeUnit.SECONDS);

        mockBackEnd.enqueue(mockResponse);

        WebClient.ResponseSpec responseSpec = reader.getRoutesData();
        String response = responseSpec.bodyToMono(String.class).block();

        Route r2 = mapper.readValue(response, Route.class);
        assertEquals(r1.getId(), r2.getId());
        assertEquals(r1.getLong_name(), r2.getLong_name());
    }

}