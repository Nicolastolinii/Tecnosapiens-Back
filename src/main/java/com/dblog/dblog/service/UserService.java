package com.dblog.dblog.service;

import com.dblog.dblog.model.User;
import com.dblog.dblog.model.dtos.UserDto;

import java.util.List;

public interface UserService {
    User createUser(User user);

    List<UserDto> getAllUsers();

    UserDto getUserById(Long userId);


    void deleteUser(Long userId);
}
