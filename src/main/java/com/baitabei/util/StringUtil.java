package com.baitabei.util;

import org.springframework.util.StringUtils;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * 
 * @author MiniMax Agent
 */
public class StringUtil {

    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    
    // 邮箱正则表达式
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    
    // 手机号正则表达式
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^1[3-9]\\d{9}$"
    );

    /**
     * 生成UUID
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成指定长度的随机字符串
     */
    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHANUMERIC.charAt(RANDOM.nextInt(ALPHANUMERIC.length())));
        }
        return sb.toString();
    }

    /**
     * 生成项目编号
     */
    public static String generateProjectCode() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new Date());
        String randomStr = generateRandomString(4);
        return "P" + dateStr + randomStr;
    }

    /**
     * 生成随机验证码
     */
    public static String generateVerificationCode(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * 验证邮箱格式
     */
    public static boolean isValidEmail(String email) {
        return StringUtils.hasText(email) && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * 验证手机号格式
     */
    public static boolean isValidPhone(String phone) {
        return StringUtils.hasText(phone) && PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * 验证密码强度
     */
    public static boolean isValidPassword(String password) {
        if (!StringUtils.hasText(password) || password.length() < 6 || password.length() > 20) {
            return false;
        }
        
        // 至少包含一个字母和一个数字
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasNumber = password.matches(".*\\d.*");
        
        return hasLetter && hasNumber;
    }

//    /**
//     * 隐藏邮箱部分内容
//     */
//    public static String hideEmail(String email) {
//        if (!StringUtils.hasText(email) || !email.contains("@")) {
//            return email;
//        }
//
//        String[] parts = email.split("@");
//        String username = parts[0];
//        String domain = parts[1];
//
//        if (username.length() <= 2) {
//            return email;
//        }
//
//        String hiddenUsername = username.charAt(0) +
//                               "*".repeat(username.length() - 2) +
//                               username.charAt(username.length() - 1);
//
//        return hiddenUsername + "@" + domain;
//    }

    /**
     * 隐藏邮箱部分内容
     * 例： abcdef@example.com -> a****f@example.com
     */
    public static String hideEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }

        int atIndex = email.indexOf('@');
        String username = email.substring(0, atIndex);
        String domain   = email.substring(atIndex + 1);

        int len = username.length();
        if (len <= 2) {
            return email;
        }

        // 首字符 + (len-2) 个星号 + 尾字符
        StringBuilder sb = new StringBuilder(len);
        sb.append(username.charAt(0));
        for (int i = 0; i < len - 2; i++) {
            sb.append('*');
        }
        sb.append(username.charAt(len - 1));

        return sb.append('@').append(domain).toString();
    }

    /**
     * 隐藏手机号部分内容
     */
    public static String hidePhone(String phone) {
        if (!StringUtils.hasText(phone) || phone.length() < 7) {
            return phone;
        }
        
        return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4);
    }

    /**
     * 驼峰命名转下划线命名
     */
    public static String camelToSnake(String camelCase) {
        if (!StringUtils.hasText(camelCase)) {
            return camelCase;
        }
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    /**
     * 下划线命名转驼峰命名
     */
    public static String snakeToCamel(String snakeCase) {
        if (!StringUtils.hasText(snakeCase)) {
            return snakeCase;
        }
        
        StringBuilder result = new StringBuilder();
        boolean nextUpper = false;
        
        for (char c : snakeCase.toCharArray()) {
            if (c == '_') {
                nextUpper = true;
            } else {
                if (nextUpper) {
                    result.append(Character.toUpperCase(c));
                    nextUpper = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            }
        }
        
        return result.toString();
    }

    /**
     * 截取指定长度的字符串
     */
    public static String truncate(String str, int maxLength) {
        if (!StringUtils.hasText(str) || str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength) + "...";
    }

    /**
     * 检查字符串是否为空或空白
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 检查字符串是否不为空
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
}
