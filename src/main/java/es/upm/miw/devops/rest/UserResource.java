package es.upm.miw.devops.rest;

import es.upm.miw.devops.model.Role;
import es.upm.miw.devops.model.User;
import es.upm.miw.devops.model.UsersDatabase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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

    @GetMapping
    public List<User> searchUsers(@RequestParam(required = false) Boolean active,
                                   @RequestParam(required = false) Role role,
                                   @RequestParam(required = false) Boolean billable) {
        return this.usersDatabase.findByFilter(active, role, billable);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        if (!this.usersDatabase.deleteById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + id);
        }
    }

    @PutMapping("/{id}/active")
    public User setActive(@PathVariable String id, @RequestBody boolean active) {
        return this.usersDatabase.updateActive(id, active)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + id));
    }
}
