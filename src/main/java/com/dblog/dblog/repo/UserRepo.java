package com.dblog.dblog.repo;

import com.dblog.dblog.model.User;
import com.dblog.dblog.model.dtos.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findByUserAndPassword(String user, String password);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.blogs WHERE u.id = :userId")
    User getUserWithBlogs(Long userId);

    @Query("SELECT u.correo FROM User u")
    List<String> findAllEmails();

    @Query("SELECT u FROM User u")
    List<User> findAllUsers();

    Optional<User> findByCorreo(String email);
    User findUserById(Long id);
    User findByUser(String user);
}
