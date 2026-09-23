package es.upm.miw.devops.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(UsersDatabase.class)
class UsersDatabaseTest {

    @Autowired
    private UsersDatabase usersDatabase;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @BeforeEach
    void setUp() {
        this.userJpaRepository.deleteAll();
        DefaultUsers.seed().stream().map(UserEntity::from).forEach(this.userJpaRepository::save);
    }

    @Test
    void testFindByIdFound() {
        var user = this.usersDatabase.findById("1");

        assertThat(user).isPresent();
        assertThat(user.get().firstName()).isEqualTo("John");
        assertThat(user.get().familyName()).isEqualTo("Smith");
    }

    @Test
    void testFindByIdNotFound() {
        assertThat(this.usersDatabase.findById("unknown")).isEmpty();
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

    @Test
    void testFindByFilterBillableTrue() {
        List<User> result = this.usersDatabase.findByFilter(null, null, true);

        assertThat(result).extracting(User::id).containsExactlyInAnyOrder("1", "2", "4");
    }

    @Test
    void testFindByFilterBillableFalse() {
        List<User> result = this.usersDatabase.findByFilter(null, null, false);

        assertThat(result).extracting(User::id).containsExactlyInAnyOrder("3", "5");
    }

    @Test
    void testUpdateActiveFound() {
        Optional<User> updated = this.usersDatabase.updateActive("4", true);

        assertThat(updated).isPresent();
        assertThat(updated.get().active()).isTrue();
        assertThat(this.usersDatabase.findById("4")).isPresent();
        assertThat(this.usersDatabase.findById("4").get().active()).isTrue();
    }

    @Test
    void testUpdateActiveNotFound() {
        Optional<User> updated = this.usersDatabase.updateActive("unknown", true);

        assertThat(updated).isEmpty();
    }

    @Test
    void testUpdateFound() {
        User newData = new User("3", "Ana", "Garcia", "ana.garcia@example.com", "12345678A",
                "Calle Mayor 1", "Madrid", "Madrid", "28001", true, Role.USER);

        Optional<User> updated = this.usersDatabase.update("3", newData);

        assertThat(updated).isPresent();
        assertThat(updated.get().firstName()).isEqualTo("Ana");
        assertThat(updated.get().familyName()).isEqualTo("Garcia");
        assertThat(updated.get().email()).isEqualTo("ana.garcia@example.com");
        assertThat(this.usersDatabase.findById("3")).isPresent();
        assertThat(this.usersDatabase.findById("3").get().firstName()).isEqualTo("Ana");
    }

    @Test
    void testUpdateNotFound() {
        User newData = new User("unknown", "Ana", "Garcia", "ana.garcia@example.com", "12345678A",
                "Calle Mayor 1", "Madrid", "Madrid", "28001", true, Role.USER);

        Optional<User> updated = this.usersDatabase.update("unknown", newData);

        assertThat(updated).isEmpty();
    }

    @Test
    void testUpdateActiveListFound() {
        List<User> updated = this.usersDatabase.updateActive(List.of(
                new UserActiveUpdate("3", false),
                new UserActiveUpdate("4", true)));

        assertThat(updated).extracting(User::id).containsExactlyInAnyOrder("3", "4");
        assertThat(this.usersDatabase.findById("3")).isPresent();
        assertThat(this.usersDatabase.findById("3").get().active()).isFalse();
        assertThat(this.usersDatabase.findById("4")).isPresent();
        assertThat(this.usersDatabase.findById("4").get().active()).isTrue();
    }

    @Test
    void testUpdateActiveListSkipsNotFound() {
        List<User> updated = this.usersDatabase.updateActive(List.of(
                new UserActiveUpdate("4", true),
                new UserActiveUpdate("unknown", true)));

        assertThat(updated).extracting(User::id).containsExactly("4");
    }

    @Test
    void testUpdateActiveListSkipsAdminDeactivation() {
        List<User> updated = this.usersDatabase.updateActive(List.of(
                new UserActiveUpdate("1", false),
                new UserActiveUpdate("4", false)));

        assertThat(updated).extracting(User::id).containsExactly("4");
        assertThat(this.usersDatabase.findById("1")).isPresent();
        assertThat(this.usersDatabase.findById("1").get().active()).isTrue();
    }

    @Test
    void testUpdateActiveListAllowsAdminActivation() {
        List<User> updated = this.usersDatabase.updateActive(List.of(
                new UserActiveUpdate("5", true)));

        assertThat(updated).extracting(User::id).containsExactly("5");
        assertThat(this.usersDatabase.findById("5")).isPresent();
        assertThat(this.usersDatabase.findById("5").get().active()).isTrue();
    }
}
