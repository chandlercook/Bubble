package com.ckay.bubble;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // load application-test.properties instead of application.properties in main
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc; // MockMvc Pretends to call our API

    @Test
    void shouldRegisterUser() throws Exception {

        String json = """
                    {
                        "username":"test",
                        "password":"password123"
                    }
                """;

        mockMvc.perform(post("/api/auth/register") // call register endpoint
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                        .andExpect(status().isCreated()); // verifies creation
    }
}
