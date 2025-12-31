package com.desafio2.demo2.repository;

import com.desafio2.demo2.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    void shouldSaveUser() {
        User user = new User();
        user.setName("Marcos");
        user.setEmail("marcos@email.com");

        User saved = repository.save(user);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Marcos");
    }
}
