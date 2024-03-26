package com.dblog.dblog.service;


import com.dblog.dblog.model.User;
import com.dblog.dblog.model.dtos.UserDto;
import com.dblog.dblog.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public User createUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public String uploadImage(MultipartFile file) throws Exception {
        try {
            String fileName = UUID.randomUUID().toString();
            byte[] bytes = file.getBytes();
            String fileOriginalName = file.getOriginalFilename();
            String fileExt = fileOriginalName.substring(fileOriginalName.lastIndexOf("."));
            String newFileName = fileName + fileExt;
            File folder = new File("/root/app/image/userimg/");
            if (!folder.exists()){
                folder.mkdir();
            }
            Path path = Paths.get("/root/app/image/userimg/" + newFileName);
            Files.write(path,bytes);
            return path.toString();
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    };

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepo.findAllUsers();
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setUser(user.getUser());
            userDtos.add(userDto);
        }
        return userDtos;
    }

    @Override
    public UserDto getUserById(Long userId) {
       User user = userRepo.getUserWithBlogs(userId);
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
    public UserDto getUserByIdPost(Long userId) {
        User user = userRepo.getUserWithBlogs(userId);
        if (user != null){
            UserDto userDTO = new UserDto();
            //userDTO.setImage(user.getImage());
            userDTO.setUser(user.getUser());
            return userDTO;
        }
        return null;
    }

    @Override
    public void deleteUser(Long userId) {
        userRepo.deleteById(userId);
    }
}
