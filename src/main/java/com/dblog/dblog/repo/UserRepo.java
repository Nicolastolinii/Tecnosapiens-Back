package com.dblog.dblog.repo;

import com.dblog.dblog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findByUserAndPassword(String user, String password);

}
