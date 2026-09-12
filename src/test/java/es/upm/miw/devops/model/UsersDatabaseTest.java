package es.upm.miw.devops.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UsersDatabaseTest {

    private UsersDatabase usersDatabase;

    @BeforeEach
    void setUp() {
        this.usersDatabase = new UsersDatabase();
    }

    @Test
    void testDeleteByIdFound() {
        boolean deleted = this.usersDatabase.deleteById("1");

        assertThat(deleted).isTrue();
        assertThat(this.usersDatabase.findById("1")).isEmpty();
    }

    @Test
    void testDeleteByIdNotFound() {
        boolean deleted = this.usersDatabase.deleteById("unknown");

        assertThat(deleted).isFalse();
        assertThat(this.usersDatabase.findByFilter(null, null, null)).hasSize(5);
    }
}
