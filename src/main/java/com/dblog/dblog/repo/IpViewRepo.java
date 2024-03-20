package com.dblog.dblog.repo;

import com.dblog.dblog.model.IpView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;


public interface IpViewRepo extends JpaRepository<IpView, Long> {

    List<IpView> findByCreatedAtBefore(Date date);
    boolean existsByIpaddressAndPostid(String ipaddress, Long postid);
}
