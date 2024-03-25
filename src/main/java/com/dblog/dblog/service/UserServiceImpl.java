package com.dblog.dblog.service;


import com.dblog.dblog.model.User;
import com.dblog.dblog.model.dtos.UserDto;
import com.dblog.dblog.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public User createUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepo.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setUser(user.getUser());
            userDto.setCorreo(user.getCorreo());
            userDtos.add(userDto);
        }
        return userDtos;
    }

    @Override
    public UserDto getUserById(Long userId) {
       User user = userRepo.getReferenceById(userId);
        if (user != null){
            UserDto userDTO = new UserDto();
            userDTO.setId(user.getId());
            userDTO.setUser(user.getUser());
            userDTO.setBlogs(user.getBlogs());
            return userDTO;
        }
        return null;
    }

    @Override
    public void deleteUser(Long userId) {
        userRepo.deleteById(userId);
    }
}
