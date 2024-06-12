package com.dblog.dblog.service;


import com.dblog.dblog.model.User;
import com.dblog.dblog.model.dtos.UserDto;
import com.dblog.dblog.repo.UserRepo;
import com.dblog.dblog.utils.EmailUtil;
import com.dblog.dblog.utils.GenerateOtp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private GenerateOtp generateOtp;
    @Override
    public User createUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public void validateUser(Long userId) throws Exception {
        User user = userRepo.findUserById(userId);
        if (user != null) {
            user.setValidated(true);
            userRepo.save(user);
        } else {
            throw new ChangeSetPersister.NotFoundException();
        }
    }

    @Override
    public String verifyAccount(String email, String otp){
        User user = userRepo.findByCorreo(email).orElseThrow(()-> new RuntimeException("No hay usuario con este Email!"));
        Integer currentCount = user.getOtpRequestCount();
        user.setOtpRequestCount(currentCount + 1);
        if (currentCount < 3){
            if (user.getOtp().equals(otp) && Duration.between(user.getOtpGeneratedTime(), LocalDateTime.now()).getSeconds() < (60)){
                user.setEmailValidated(true);
                userRepo.save(user);
                return "OTP verificado con exito!";
            }
        }
        return null;
    }
    @Override
    public  String regenerateOtp(String email){
        User user = userRepo.findByCorreo(email).orElseThrow(()-> new RuntimeException("No hay usuario con este Email!"));
        Integer currentCount = user.getOtpRequestCount();
        if (currentCount > 3){
            return null;
        }
        String otp = generateOtp.generate();
        try {
            emailUtil.sendOtpEmail(user.getCorreo(), otp);
        }catch (Exception e){
            throw new RuntimeException("No se pudo enviar el OTP, intentelo de nuevo.", e);
        }
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());
        userRepo.save(user);
        return "Email enviado...";
    }
    @Override
    public List<String> findAllEmails() {
        return userRepo.findAllEmails();
    }
    @Override
    public List<String> findAllUserName() {
        return userRepo.findAllUserName();
    }
    @Override
    public String uploadImage(MultipartFile file, Long userId) throws Exception {
        try {
            User user = userRepo.findUserById(userId);
            if (user != null) {
                String currentImageName = user.getImage();
                if (currentImageName != null && !currentImageName.isEmpty()) {
                    int index = currentImageName.lastIndexOf("user/") + 5;
                    if (index > 5) {
                        String fileName = currentImageName.substring(index);
                        Path imagePath = Paths.get("/root/app/image/userimg/" + fileName);
                        Files.deleteIfExists(imagePath);
                    }
                }
            }else {
                throw new Exception("Usuario no encontrado con el ID: " + userId);
            }
            String fileName = UUID.randomUUID().toString();
            byte[] bytes = file.getBytes();
            String fileOriginalName = file.getOriginalFilename();
            String fileExt = fileOriginalName.substring(fileOriginalName.lastIndexOf("."));
            String newFileName = fileName + fileExt;
            File folder = new File("/root/app/image/userimg");
            //File folder = new File("/img/");
            if (!folder.exists()){
                folder.mkdir();
            }
            Path path = Paths.get("/root/app/image/userimg/" + newFileName);
            //Path path = Paths.get("/img/" + newFileName);
            Files.write(path,bytes);
            return path.toString();
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    };
    @Override
    public  User updateUser(User user){
        return  userRepo.save(user);
    }

    @Override
    public User userById(Long userId){
        return userRepo.findUserById(userId);
    }
    public User userByUser(String user){
        return userRepo.findByUser(user);
    }

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
            userDTO.setImage(user.getImage());
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
            userDTO.setImage(user.getImage());
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
