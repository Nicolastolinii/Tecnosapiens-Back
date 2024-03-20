package com.dblog.dblog.service;

import com.dblog.dblog.model.IpView;
import com.dblog.dblog.repo.IpViewRepo;
import com.dblog.dblog.utils.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class CookieCleanServiceImpl implements CookieCleanService{
    @Autowired
    private IpViewRepo ipviewrepo;

    private static final Logger logger = new Logger();
    @Scheduled(fixedDelay = 86400000)
    public void cleanExpiredCookies() {
        try {
            Date currentDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.HOUR_OF_DAY, -24);
            Date twentyFourHoursAgo = calendar.getTime();
            List<IpView> obsoleteEntries = ipviewrepo.findByCreatedAtBefore(twentyFourHoursAgo);
            if (!obsoleteEntries.isEmpty()){
                ipviewrepo.deleteAll(obsoleteEntries);
                logger.log(Logger.LogLevel.INFO,("Cookies borradas: "+ obsoleteEntries.size()));
            }
            logger.log(Logger.LogLevel.INFO,"No hay Cookies expiradas");

        }catch (Exception e){
            logger.log(Logger.LogLevel.ERROR, e.getMessage());
        }
    }
}
