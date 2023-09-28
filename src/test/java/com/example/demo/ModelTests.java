package com.example.demo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import jakarta.validation.*;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

public class ModelTests {
    private User mockedPerson;

    @BeforeEach
    public void setUp(){
        mockedPerson = mock(User.class);
    }
    private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private Validator validator = factory.getValidator();

    @Test
    public void testValidEmail() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLastName("Jones");
        user.setFirstName("John");
        user.setBirthDate(LocalDate.of(1990, Month.JANUARY, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidEmail() {
        User user = new User();
        user.setEmail("invalid-email");
        user.setLastName("Jones");
        user.setFirstName("John");
        user.setBirthDate(LocalDate.of(1990, Month.JANUARY, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testValidPhoneNumber() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLastName("Jones");
        user.setFirstName("John");
        user.setBirthDate(LocalDate.of(1990, Month.JANUARY, 1));
        user.setPhoneNumber("1234567890");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testValidPhoneNumberWithDashes() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLastName("Jones");
        user.setFirstName("John");
        user.setBirthDate(LocalDate.of(1990, Month.JANUARY, 1));
        user.setPhoneNumber("(123) 456-7890");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidPhoneNumber() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLastName("Jones");
        user.setFirstName("John");
        user.setBirthDate(LocalDate.of(1990, Month.JANUARY, 1));
        user.setPhoneNumber("invalid-phone-number");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testValidBirthDate() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLastName("Jones");
        user.setFirstName("John");
        user.setBirthDate(LocalDate.of(1990, Month.JANUARY, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testFutureBirthDate() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLastName("Jones");
        user.setFirstName("John");
        user.setBirthDate(LocalDate.now().plusDays(1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }
}
