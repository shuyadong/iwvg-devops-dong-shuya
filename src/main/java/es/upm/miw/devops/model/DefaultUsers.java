package es.upm.miw.devops.model;

import java.util.List;

final class DefaultUsers {

    private DefaultUsers() {
    }

    static List<User> seed() {
        return List.of(
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
        );
    }
}
