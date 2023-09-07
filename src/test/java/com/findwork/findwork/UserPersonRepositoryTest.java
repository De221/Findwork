package com.findwork.findwork;

import com.findwork.findwork.Entities.Users.UserPerson;
import com.findwork.findwork.Repositories.UserPersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // uses the embedded database for testing from the com.h2database dependency
class UserPersonRepositoryTest {
    @Autowired
    private UserPersonRepository repo;

    // Create a sample UserPerson entity
    UserPerson user1 = new UserPerson("user1@abv.bg", "password", "firstName", "lastName", LocalDate.of(1999, 1, 1));
    @Test
    public void testFindUserPersonByUsername()
    {
        // Create a sample UserPerson entity
        UserPerson user1 = new UserPerson("user1@abv.bg", "password", "firstName", "lastName", LocalDate.of(1999, 1, 1));


        // Save the entity to the database
        repo.save(user1);

        // Use the repository method to retrieve the entity
        UserPerson foundUser = repo.findUserPersonByUsername("user1@abv.bg");

        // Assert that the retrieved entity matches the saved entity
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("user1@abv.bg");
        assertThat(foundUser.getId()).isEqualTo(user1.getId());
    }
    @Test
    public void testFindUserPersonById() {
        // Create a sample UserPerson entity
        UserPerson user1 = new UserPerson("user1@abv.bg", "password", "firstName", "lastName", LocalDate.of(1999, 1, 1));

        // Save the entity to the database
        repo.save(user1);
        UUID personId = user1.getId();
        // Use the repository method to retrieve the entity
        UserPerson foundUser = repo.findUserPersonById(personId);
        // Assert that the retrieved entity matches the saved entity
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("user1@abv.bg");
        assertThat(foundUser.getId()).isEqualTo(user1.getId());
    }

    @Test
    public void testFindUserPeopleByUsernameStartsWith() {
        UserPerson user1 = new UserPerson("user1@abv.bg", "password", "firstName", "lastName", LocalDate.of(1999, 1, 1));
        UserPerson user2 = new UserPerson("user2@abv.bg", "password", "firstName", "lastName", LocalDate.of(1999, 1, 1));
        UserPerson user3 = new UserPerson("user3@abv.bg", "password", "firstName", "lastName", LocalDate.of(1999, 1, 1));

        List<UserPerson> savedUsers = new ArrayList<>();
        savedUsers.add(user1);
        savedUsers.add(user2);
        savedUsers.add(user3);

        repo.save(user1);
        repo.save(user2);
        repo.save(user3);

        // Use the repository method to retrieve the entity
        List<UserPerson> foundUsers = repo.findUserPeopleByUsernameStartsWith("user");
        // Assert that the retrieved entity matches the saved entity
        assertThat(foundUsers).isNotNull();
        assertThat(foundUsers.equals(savedUsers));
    }
}