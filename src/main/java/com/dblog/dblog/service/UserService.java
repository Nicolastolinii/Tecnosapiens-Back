package com.dblog.dblog.service;

import com.dblog.dblog.model.User;
import com.dblog.dblog.model.dtos.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    User createUser(User user);

    void validateUser(Long userId)throws Exception;

    String verifyAccount(String email, String otp);
    String regenerateOtp(String email);
    User updateUser(User user);

    List<String> findAllEmails();
    List<String> findAllUserName();

    List<UserDto> getAllUsers();

    String uploadImage(MultipartFile file, Long userId) throws Exception;


    UserDto getUserById(Long userId);

    User userById ( Long userId);
    User userByUser ( String user);

    UserDto getUserByIdPost(Long userId);


    void deleteUser(Long userId);
}
