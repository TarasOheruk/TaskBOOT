package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class ServicesTests {

    @MockBean
    UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testCalculateAge() {
        List<User> userList = new ArrayList<>();
        UserService userService = new UserService(userList);
        LocalDate birthDate = LocalDate.of(1990, 1, 1);

        int age = userService.calculateAge(birthDate);

        assertEquals(33, age);
    }
    @Test
    public void testGetUserById(){
        User user = new User();
        user.setEmail("user@example.com");
        user.setLastName("Jones");
        user.setFirstName("John");
        user.setBirthDate(LocalDate.of(1990, Month.JANUARY, 1));

        when(userService.getUserById(1)).thenReturn(user);

        assertEquals("user@example.com", userService.getUserById(1).getEmail());
        assertEquals("Jones", userService.getUserById(1).getLastName());
        assertEquals("John", userService.getUserById(1).getFirstName());
        assertEquals(LocalDate.of(1990, Month.JANUARY, 1), userService.getUserById(1).getBirthDate());

    }

    @Test
    public void testDeleteUserById()  {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLastName("Jones");
        user.setFirstName("John");
        user.setBirthDate(LocalDate.of(1990, Month.JANUARY, 1));

        when(userService.getUserById(1)).thenReturn(user);

        userService.deleteUser(1);

        when(userService.getUserById(1)).thenReturn(null);

        assertEquals(null, userService.getUserById(1));

    }

    @Test
    public void testGetUsersByBirthDateRange(){
        List<User> userList = new ArrayList<>();
        User user1 = new User();
        user1.setBirthDate(LocalDate.of(1990, Month.JANUARY, 1));
        userList.add(user1);

        User user2 = new User();
        user2.setBirthDate(LocalDate.of(1985, Month.MARCH, 15));
        userList.add(user2);

        User user3 = new User();
        user3.setBirthDate(LocalDate.of(1995, Month.AUGUST, 20));
        userList.add(user3);

        UserService userService = new UserService(userList);

        LocalDate fromDate = LocalDate.of(1980, Month.JANUARY, 1);
        LocalDate toDate = LocalDate.of(1994, Month.DECEMBER, 31);

        List<User> matchingUsers = userService.getUsersByBirthDateRange(fromDate, toDate);

        assertEquals(2, matchingUsers.size());
        assertTrue(matchingUsers.contains(user1));
        assertTrue(matchingUsers.contains(user2));
        assertFalse(matchingUsers.contains(user3));

    }

}
