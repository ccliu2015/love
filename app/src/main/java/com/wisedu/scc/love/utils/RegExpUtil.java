package com.wisedu.scc.love.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by JZ on 2015/3/13.
 */
public class RegExpUtil {

    /**
     * 判断是否手机号码
     * @param number
     * @return
     */
    public static boolean validatePhone(String number){
        String regex = "((13[0-9])|(15[0-9])|(18[0-9]))[0-9]{8}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(number.trim());
        return matcher.matches();
    }

    /**
     * 验证Email
     * @param email
     * @author 罗嗣金
     * @date 2009-11-10 上午12:34:50
     */
    public static boolean validateEmail(String email){
        String regex = "[0-9a-zA-Z]+@[0-9a-zA-Z]+//.[0-9a-zA-Z]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}