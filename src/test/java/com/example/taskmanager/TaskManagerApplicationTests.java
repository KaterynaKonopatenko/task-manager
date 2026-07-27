package com.example.taskmanager;

import com.example.taskmanager.model.User;
import com.example.taskmanager.repo.UserRepository;
import com.example.taskmanager.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TaskManagerApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
        // test that a new user is saved to the database after registration
    void testRegisterSavesUser() {
        userService.register("testuser", "test@test.com", "password");
        assertTrue(userRepository.findByUsername("testuser").isPresent());
    }

    @Test
        // test that password is encrypted and not stored as plain text
    void testPasswordIsEncrypted() {
        userService.register("testuser2", "test2@test.com", "password");
        User user = userRepository.findByUsername("testuser2").orElseThrow();
        assertNotEquals("password", user.getPassword());
    }

    @Test
        // test that registering with duplicate username throws exception
    void testDuplicateUsernameThrowsException() {
        userService.register("sameuser", "first@test.com", "password");
        assertThrows(Exception.class, () ->
                userService.register("sameuser", "second@test.com", "password"));
    }
}
