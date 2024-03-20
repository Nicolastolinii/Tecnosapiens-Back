package com.dblog.dblog.service;

import com.dblog.dblog.model.IpView;
import com.dblog.dblog.repo.IpViewRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class IpViewServiceImpl implements IpViewService {

    @Autowired
    private IpViewRepo ipviewrepo;

    @Override
    public boolean findByIpAddressAndPostId(String ipaddress, Long postid) {
        return ipviewrepo.existsByIpaddressAndPostid(ipaddress, postid);
    }

    @Override
    public IpView saveip(IpView ipView) {
        return ipviewrepo.save(ipView);
    }




}
