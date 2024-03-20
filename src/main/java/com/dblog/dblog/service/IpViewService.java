package com.dblog.dblog.service;

import com.dblog.dblog.model.IpView;

public interface IpViewService {
    boolean findByIpAddressAndPostId(String ipaddress, Long post_id);

    IpView saveip(IpView ipView );

}
