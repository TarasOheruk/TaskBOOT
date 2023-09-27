package com.example.demo;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private List<User> userList; // Ваш список користувачів

    // Конструктор, що приймає список користувачів
    public UserService(List<User> userList) {
        this.userList = userList;
    }

    // Метод для отримання користувача за ідентифікатором
    public User getUserById(Long userId) {
        int index = userId.intValue(); // Перетворення Long в int (індекс списку)
        if (index >= 0 && index < userList.size()) {
            return userList.get(index);
        }
        return null; // Користувач не знайдений
    }
    public void deleteUser(Long index) {
        if (index >= 0 && index < userList.size()) {
            userList.remove(index);
        }
    }
    public List<User> getUsersByBirthDateRange(LocalDate fromDate, LocalDate toDate) {
        List<User> matchingUsers = new ArrayList<>();
        for (User user : userList) {
            LocalDate birthDate = user.getBirthDate();
            if (birthDate != null && !birthDate.isBefore(fromDate) && !birthDate.isAfter(toDate)) {
                matchingUsers.add(user);
            }
        }
        return matchingUsers;
    }
    public int calculateAge(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }

}