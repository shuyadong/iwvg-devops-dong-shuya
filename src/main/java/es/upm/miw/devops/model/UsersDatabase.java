package es.upm.miw.devops.model;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UsersDatabase {

    private final List<User> users = List.of(
            new User("1", "John", "Smith"),
            new User("2", "Mary", "Johnson"),
            new User("3", "Robert", "Williams")
    );

    public Optional<User> findById(String id) {
        return this.users.stream()
                .filter(user -> user.id().equals(id))
                .findFirst();
    }
}
