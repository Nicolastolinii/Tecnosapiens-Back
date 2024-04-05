package com.dblog.dblog.utils;

import org.springframework.stereotype.Component;

import java.util.Random;


@Component
public class GenerateOtp {
    public String generate(){
        Random random = new Random();
        int randomNumber = random.nextInt(999999);
        return String.format("%06d", randomNumber);
    }
}
