package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc

class DemoApplicationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    UserService userService;

    @Test
    public void testCalculateAge() {
        // Arrange
        LocalDate birthDate = LocalDate.of(1990, Month.JANUARY, 1);

        // Act
        int age = userService.calculateAge(birthDate);

        // Assert
        assertEquals(33, age); // Припустимо, що користувачу 32 роки
    }

    @Test
    public void testCreateUser() throws Exception {
        User user = new User();
        user.setEmail("tar@gmail.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        LocalDate birthDate = LocalDate.of(1990, Month.JANUARY, 1);

        user.setBirthDate(birthDate);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testUpdateUser() throws Exception {
        User user = new User();
        user.setEmail("tar@gmail.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBirthDate(LocalDate.of(1990, 1, 1));

        User updatedUser = new User();
        updatedUser.setFirstName("UpdatedJohn");

        mockMvc.perform(MockMvcRequestBuilders.post("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }


}
