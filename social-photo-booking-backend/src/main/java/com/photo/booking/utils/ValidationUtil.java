package com.photo.booking.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class ValidationUtil {

    // 手机号正则表达式
    private static final String PHONE_REGEX = "^1[3-9]\\d{9}$";

    // 邮箱正则表达式
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    // 身份证号正则表达式
    private static final String ID_CARD_REGEX = "^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[\\dXx]$";

    /**
     * 验证手机号
     */
    public static boolean validatePhone(String phone) {
        if (StringUtils.isBlank(phone)) {
            return false;
        }
        return Pattern.matches(PHONE_REGEX, phone);
    }

    /**
     * 验证邮箱
     */
    public static boolean validateEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        return Pattern.matches(EMAIL_REGEX, email);
    }

    /**
     * 验证身份证号
     */
    public static boolean validateIdCard(String idCard) {
        if (StringUtils.isBlank(idCard)) {
            return false;
        }
        return Pattern.matches(ID_CARD_REGEX, idCard);
    }

    /**
     * 验证密码强度
     */
    public static boolean validatePassword(String password) {
        if (StringUtils.isBlank(password) || password.length() < 6) {
            return false;
        }
        // 密码至少包含字母和数字
        boolean hasLetter = false;
        boolean hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }
        }
        return hasLetter && hasDigit;
    }

    /**
     * 验证字符串长度
     */
    public static boolean validateLength(String str, int min, int max) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        int length = str.length();
        return length >= min && length <= max;
    }
}
