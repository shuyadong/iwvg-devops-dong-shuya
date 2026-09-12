package es.upm.miw.devops.functionaltests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class UserResourceFT {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testGetUserFound() {
        webTestClient.get()
                .uri("/user/{id}", "1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("1")
                .jsonPath("$.firstName").isEqualTo("John")
                .jsonPath("$.familyName").isEqualTo("Smith");
    }

    @Test
    void testGetUserNotFound() {
        webTestClient.get()
                .uri("/user/{id}", "unknown")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void testDeleteUserFound() {
        webTestClient.delete()
                .uri("/user/{id}", "3")
                .exchange()
                .expectStatus().isOk();

        webTestClient.get()
                .uri("/user/{id}", "3")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testDeleteUserNotFound() {
        webTestClient.delete()
                .uri("/user/{id}", "unknown")
                .exchange()
                .expectStatus().isNotFound();
    }
}
