package es.upm.miw.devops.rest;

import es.upm.miw.devops.model.User;
import es.upm.miw.devops.model.UsersDatabase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/user")
public class UserResource {

    private final UsersDatabase usersDatabase;

    public UserResource(UsersDatabase usersDatabase) {
        this.usersDatabase = usersDatabase;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable String id) {
        return this.usersDatabase.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + id));
    }
}
