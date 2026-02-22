package com.ckay.bubble;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // rebuild DB each test run
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
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                        .andExpect(status().isCreated()); // verifies creation
    }

    @Test
    void shouldLogin() throws Exception {

        // 1: Create account first (in memory)
        String userJson = """
                    {
                        "username":"test",
                        "password":"password123"
                    }
                """;

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated());

        // 2: Attempt login on temporary account
        mockMvc.perform(post("/api/auth/login")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(userJson))
                .andExpect(status().isOk());
    }
}
