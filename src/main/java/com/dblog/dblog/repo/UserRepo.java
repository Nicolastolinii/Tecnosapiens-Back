package com.dblog.dblog.repo;

import com.dblog.dblog.model.User;
import com.dblog.dblog.model.dtos.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findByUserAndPassword(String user, String password);


}
