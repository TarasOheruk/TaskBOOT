package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private List<User> userList;

    @Autowired
    public UserService(List<User> userList) {
        this.userList = userList;
    }
    public void saveUser(User user) {
        userList.add(user);
    }
    public User getUserById(int userId) {

        if (userId >= 0 && userId < userList.size()) {
            return userList.get(userId);
        }
        return null;
    }

    public void deleteUser(int userId) {
        if (userId >= 0 && userId < userList.size()) {
            userList.remove(userId);
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

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }


}