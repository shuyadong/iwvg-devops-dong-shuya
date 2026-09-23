package es.upm.miw.devops.functionaltests;

import es.upm.miw.devops.model.Role;
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

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void testUpdateUserFound() {
        User newData = new User("3", "Ana", "Garcia", "ana.garcia@example.com", "12345678A",
                "Calle Mayor 1", "Madrid", "Madrid", "28001", true, Role.USER);

        webTestClient.put()
                .uri("/user/{id}", "3")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newData)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("3")
                .jsonPath("$.firstName").isEqualTo("Ana")
                .jsonPath("$.familyName").isEqualTo("Garcia")
                .jsonPath("$.email").isEqualTo("ana.garcia@example.com");

        webTestClient.get()
                .uri("/user/{id}", "3")
                .exchange()
                .expectBody()
                .jsonPath("$.firstName").isEqualTo("Ana");
    }

    @Test
    void testUpdateUserNotFound() {
        User newData = new User("unknown", "Ana", "Garcia", "ana.garcia@example.com", "12345678A",
                "Calle Mayor 1", "Madrid", "Madrid", "28001", true, Role.USER);

        webTestClient.put()
                .uri("/user/{id}", "unknown")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newData)
                .exchange()
                .expectStatus().isNotFound();
    }
}
