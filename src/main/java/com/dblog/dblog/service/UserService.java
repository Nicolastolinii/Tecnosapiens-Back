package com.dblog.dblog.service;

import com.dblog.dblog.model.User;
import com.dblog.dblog.model.dtos.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    User createUser(User user);

    List<UserDto> getAllUsers();

    String uploadImage(MultipartFile file) throws Exception;


    UserDto getUserById(Long userId);

    UserDto getUserByIdPost(Long userId);


    void deleteUser(Long userId);
}
