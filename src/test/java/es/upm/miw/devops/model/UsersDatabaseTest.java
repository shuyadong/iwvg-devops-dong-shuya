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
}
