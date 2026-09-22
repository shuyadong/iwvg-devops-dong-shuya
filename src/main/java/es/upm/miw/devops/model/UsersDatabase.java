package es.upm.miw.devops.model;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UsersDatabase {

    private final UserJpaRepository userJpaRepository;

    public UsersDatabase(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    public Optional<User> findById(String id) {
        return this.userJpaRepository.findById(id).map(UserEntity::toUser);
    }

    public List<User> findByFilter(Boolean active, Role role, Boolean billable) {
        return this.userJpaRepository.findAll().stream()
                .map(UserEntity::toUser)
                .filter(user -> active == null || user.active() == active)
                .filter(user -> role == null || user.role() == role)
                .filter(user -> billable == null || user.billable() == billable)
                .toList();
    }

    public boolean deleteById(String id) {
        if (!this.userJpaRepository.existsById(id)) {
            return false;
        }
        this.userJpaRepository.deleteById(id);
        return true;
    }

    public Optional<User> updateActive(String id, boolean active) {
        return this.userJpaRepository.findById(id).map(entity -> {
            entity.setActive(active);
            return this.userJpaRepository.save(entity).toUser();
        });
    }

    public Optional<User> update(String id, User user) {
        return this.userJpaRepository.findById(id).map(entity -> {
            User updatedUser = new User(id, user.firstName(), user.familyName(), user.email(), user.identity(),
                    user.address(), user.city(), user.province(), user.postalCode(), user.active(), user.role());
            return this.userJpaRepository.save(UserEntity.from(updatedUser)).toUser();
        });
    }
}
