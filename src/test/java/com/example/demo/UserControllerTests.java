package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc

class UserControllerTests {
    @MockBean
    UserService userService ;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testCreateUser() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"tar@gmail.com\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"birthDate\":\"1990-01-01\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void testUpdateUser() throws Exception {
        User existingUser = new User();
        existingUser.setEmail("oldEmail@gmail.com");
        existingUser.setFirstName("OldFirstName");
        existingUser.setLastName("OldLastName");
        existingUser.setBirthDate(LocalDate.of(1980, Month.JANUARY, 1));


        when(userService.getUserById(1)).thenReturn(existingUser);

        assertEquals("oldEmail@gmail.com", userService.getUserById(1).getEmail());
        assertEquals("OldLastName", userService.getUserById(1).getLastName());
        assertEquals("OldFirstName", userService.getUserById(1).getFirstName());
        assertEquals(LocalDate.of(1980, Month.JANUARY, 1), userService.getUserById(1).getBirthDate());

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"newEmail@gmail.com\",\"firstName\":\"NewFirstName\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("User updated successfully"));

        assertEquals("newEmail@gmail.com", userService.getUserById(1).getEmail());
        assertEquals("NewFirstName", userService.getUserById(1).getFirstName());

    }
    @Test
    public void testUpdateAllUserFields() throws Exception {
        User existingUser = new User();
        existingUser.setEmail("oldEmail@gmail.com");
        existingUser.setFirstName("OldFirstName");
        existingUser.setLastName("OldLastName");
        existingUser.setBirthDate(LocalDate.of(1980, Month.JANUARY, 1));

        User updatedUser = new User();
        updatedUser.setEmail("newEmail@gmail.com");
        updatedUser.setFirstName("NewFirstName");
        updatedUser.setLastName("NewLastName");
        updatedUser.setBirthDate(LocalDate.of(1990, Month.FEBRUARY, 2));
        updatedUser.setAddress("New Address");
        updatedUser.setPhoneNumber("1234567890");

        when(userService.getUserById(1)).thenReturn(existingUser);

        mockMvc.perform(MockMvcRequestBuilders.put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"newEmail@gmail.com\",\"firstName\":\"NewFirstName\"," +
                                "\"lastName\":\"NewLastName\",\"birthDate\":\"1990-02-02\"," +
                                "\"address\":\"New Address\",\"phoneNumber\":\"1234567890\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("All user fields updated successfully"));

        assertEquals("newEmail@gmail.com", existingUser.getEmail());
        assertEquals("NewFirstName", existingUser.getFirstName());
        assertEquals("NewLastName", existingUser.getLastName());
        assertEquals(LocalDate.of(1990, Month.FEBRUARY, 2), existingUser.getBirthDate());
        assertEquals("New Address", existingUser.getAddress());
        assertEquals("1234567890", existingUser.getPhoneNumber());
    }

    @Test
    public void testDeleteUser() throws Exception {
        User existingUser = new User();
        existingUser.setEmail("oldEmail@gmail.com");
        existingUser.setFirstName("OldFirstName");
        existingUser.setLastName("OldLastName");
        existingUser.setBirthDate(LocalDate.of(1980, Month.JANUARY, 1));

        when(userService.getUserById(1)).thenReturn(existingUser);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("User deleted successfully"));

        verify(userService, times(1)).deleteUser(1);
    }
    @Test
    public void testSearchUsersByBirthDateRange() throws Exception {
        LocalDate fromDate = LocalDate.of(1980, Month.JANUARY, 1);
        LocalDate toDate = LocalDate.of(1990, Month.JANUARY, 1);

        User user1 = new User();
        user1.setFirstName("User1");
        user1.setBirthDate(LocalDate.of(1985, Month.MARCH, 15));

        User user2 = new User();
        user2.setFirstName("User2");
        user2.setBirthDate(LocalDate.of(1988, Month.DECEMBER, 10));

        List<User> users = Arrays.asList(user1, user2);

        when(userService.getUsersByBirthDateRange(fromDate, toDate)).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/search")
                        .param("from", "1980-01-01")
                        .param("to", "1990-01-01"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("User1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].firstName").value("User2"));
    }

}
