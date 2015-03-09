package com.wisedu.scc.love.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by JZ on 2015/3/5.
 */
public class CommonUtil {

    /*显示短消息*/
    public static void shortToast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /*显示长消息*/
    public static void longToast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /*判断字符串是否为空*/
    public static boolean IsEmpty(String s){
        return null==s || "".equals(s);
    }

}
