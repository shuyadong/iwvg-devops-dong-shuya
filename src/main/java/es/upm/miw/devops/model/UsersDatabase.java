package es.upm.miw.devops.model;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UsersDatabase {

    private final List<User> users = new ArrayList<>(List.of(
            new User("1", "John", "Smith", "john.smith@example.com", "12345678A",
                    "Calle Mayor 1", "Madrid", "Madrid", "28001", true, Role.ADMIN),
            new User("2", "Mary", "Johnson", "mary.johnson@example.com", "87654321B",
                    "Avenida Libertad 22", "Barcelona", "Barcelona", "08001", true, Role.USER),
            new User("3", "Robert", "Williams", null, null,
                    null, null, null, null, true, Role.USER),
            new User("4", "Patricia", "Brown", "patricia.brown@example.com", "11223344C",
                    "Plaza Espana 5", "Valencia", "Valencia", "46001", false, Role.USER),
            new User("5", "Michael", "Davis", null, "55667788D",
                    "Calle Sol 9", null, "Sevilla", "41001", false, Role.ADMIN)
    ));

    public Optional<User> findById(String id) {
        return this.users.stream()
                .filter(user -> user.id().equals(id))
                .findFirst();
    }

    public List<User> findByFilter(Boolean active, Role role, Boolean billable) {
        return this.users.stream()
                .filter(user -> active == null || user.active() == active)
                .filter(user -> role == null || user.role() == role)
                .filter(user -> billable == null || user.billable() == billable)
                .toList();
    }

    public boolean deleteById(String id) {
        return this.users.removeIf(user -> user.id().equals(id));
    }

    public Optional<User> updateActive(String id, boolean active) {
        return findById(id).map(user -> {
            User updated = new User(user.id(), user.firstName(), user.familyName(), user.email(), user.identity(),
                    user.address(), user.city(), user.province(), user.postalCode(), active, user.role());
            this.users.set(this.users.indexOf(user), updated);
            return updated;
        });
    }
}
