package es.upm.miw.devops.model;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class UserSeeder implements ApplicationRunner {

    private final UserJpaRepository userJpaRepository;

    public UserSeeder(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (this.userJpaRepository.count() == 0) {
            DefaultUsers.seed().stream().map(UserEntity::from).forEach(this.userJpaRepository::save);
        }
    }
}
