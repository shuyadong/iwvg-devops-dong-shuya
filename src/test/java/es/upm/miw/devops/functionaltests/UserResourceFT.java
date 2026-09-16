package es.upm.miw.devops.functionaltests;

import es.upm.miw.devops.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    void testSearchUsersBillableTrue() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/user").queryParam("billable", true).build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class)
                .value(users -> assertThat(users).extracting(User::id).containsExactlyInAnyOrder("1", "2", "4"));
    }

    @Test
    void testSearchUsersBillableFalse() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/user").queryParam("billable", false).build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class)
                .value(users -> assertThat(users).extracting(User::id).containsExactlyInAnyOrder("3", "5"));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void testSetActiveFound() {
        webTestClient.put()
                .uri("/user/{id}/active", "4")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(true)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("4")
                .jsonPath("$.active").isEqualTo(true);
    }

    @Test
    void testSetActiveNotFound() {
        webTestClient.put()
                .uri("/user/{id}/active", "unknown")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(true)
                .exchange()
                .expectStatus().isNotFound();
    }
}
