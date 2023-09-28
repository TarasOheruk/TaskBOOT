package com.example.demo;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Value("${app.minimumAge}")
    private int minimumAge;

    private List<User> userList = new ArrayList<>();

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody @Valid User user) {
        int age = userService.calculateAge(user.getBirthDate());
        try {
            if (age >= minimumAge) {
                userList.add(user);
                return ResponseEntity.ok("User created successfully");
            } else {
                return ResponseEntity.badRequest().body("User must be at least " + minimumAge + " years old");
            }
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body("Invalid user data: " + e.getMessage());

        }

    }

    @PatchMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable int userId, @RequestBody User updatedUser) {
        try {
            User existingUser = userService.getUserById(userId);

            if (existingUser == null) {
                return ResponseEntity.notFound().build();
            }

            if (updatedUser.getEmail() != null) {
                existingUser.setEmail(updatedUser.getEmail());
            }

            if (updatedUser.getFirstName() != null) {
                existingUser.setFirstName(updatedUser.getFirstName());
            }

            if (updatedUser.getLastName() != null) {
                existingUser.setLastName(updatedUser.getLastName());
            }

            if (updatedUser.getBirthDate() != null) {
                existingUser.setBirthDate(updatedUser.getBirthDate());
            }

            if (updatedUser.getAddress() != null) {
                existingUser.setAddress(updatedUser.getAddress());
            }

            if (updatedUser.getPhoneNumber() != null) {
                existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
            }

            return ResponseEntity.ok("User updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid user data: " + e.getMessage());
        }

    }
    @PutMapping("/{userId}")
    public ResponseEntity<String> updateAllUserFields(@PathVariable int userId, @RequestBody User updatedUser) {
        try {
            User existingUser = userService.getUserById(userId);

            if (existingUser == null) {
                return ResponseEntity.notFound().build();
            }

            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setBirthDate(updatedUser.getBirthDate());
            existingUser.setAddress(updatedUser.getAddress());
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());

            return ResponseEntity.ok("All user fields updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid user data: " + e.getMessage());
        }

    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable int userId) {
        try {
            User userToDelete = userService.getUserById(userId);
            if (userToDelete == null) {
                return ResponseEntity.notFound().build();
            }
            userService.deleteUser(userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid user data: " + e.getMessage());
        }

    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsersByBirthDateRange(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        try {
            if (fromDate.isAfter(toDate)) {
                return ResponseEntity.badRequest().body(null);
            }

            List<User> users = userService.getUsersByBirthDateRange(fromDate, toDate);
            return ResponseEntity.ok(users);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}