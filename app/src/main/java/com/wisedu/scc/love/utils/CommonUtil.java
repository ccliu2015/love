package com.wisedu.scc.love.utils;

import android.content.Context;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 常用方法工具类
 * Created by JZ on 2015/3/5.
 */
public class CommonUtil {

    /**
     * 显示短消息
     * @param context
     * @param msg
     */
    public static void shortToast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示长消息
     * @param context
     * @param msg
     */
    public static void longToast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 判断字符串是否为空
     * @param s
     * @return
     */
    public static boolean isEmpty(String s){
        return null==s || "".equals(s);
    }

    /**
     * 判断字符串是否为空
     * @param s
     * @return
     */
    public static boolean isEmpty(StringBuffer s){
        return null==s || s.equals("");
    }

    /**
     * 截掉最后一个字符
     * @param str
     * @return
     */
    public static String cutLastChar(String str){
        return (isEmpty(str)) ? str : str.substring(0, str.length()-1);
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String getCurrentTime(){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置显示格式
        String nowTime = format.format(new Date());
        return nowTime;
    }

}
