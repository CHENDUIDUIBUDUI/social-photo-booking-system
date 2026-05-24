package com.photo.booking.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * 对密码进行加密
     */
    public static String encrypt(String password) {
        return encoder.encode(password);
    }

    /**
     * 验证密码
     */
    public static boolean verify(String password, String encryptedPassword) {
        return encoder.matches(password, encryptedPassword);
    }
}
