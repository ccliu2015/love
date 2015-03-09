package com.wisedu.scc.love.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import org.androidannotations.annotations.EBean;

import java.util.HashMap;
import java.util.Map;

@EBean
public class TerminalHelper {

    /**
     * 获取TelephonyManager
     */
    public static TelephonyManager getTelephonyManager(Context c) {
        TelephonyManager tm = (TelephonyManager)
                c.getSystemService(Context.TELEPHONY_SERVICE);
        return tm;
    }

    /**
     * 获取WifiManager
     */
    public WifiManager getWifiManager(Context c) {
        WifiManager wifiManager = (WifiManager)
                c.getSystemService(Context.WIFI_SERVICE);
        return wifiManager;
    }

    /**
     * 获取ActivityManager
     */
    public ActivityManager getActivityManager(Context c) {
        ActivityManager activityManager = (ActivityManager)
                c.getSystemService(Context.ACTIVITY_SERVICE);
        return activityManager;
    }

    /**
     * 获取isConnected()
     */
    public boolean isConnected(Context c) {
        // 网络连接管理器
        ConnectivityManager mConnectivity = (ConnectivityManager)
                c.getSystemService(Context.CONNECTIVITY_SERVICE);
        // 电话管理器
        TelephonyManager mTelephony = (TelephonyManager)
                c.getSystemService(Context.TELEPHONY_SERVICE);
        // 检查网络连接，如果无网络可用，就不需要进行连网操作等
        NetworkInfo info = mConnectivity.getActiveNetworkInfo();
        if (info == null || !mConnectivity.getBackgroundDataSetting()) {
            return false;
        } // 判断网络连接类型，只有在3G或wifi里进行一些数据更新。
        int netType = info.getType();
        int netSubtype = info.getSubtype();
        if (netType == ConnectivityManager.TYPE_WIFI) {
            return info.isConnected();
        } else if (netType == ConnectivityManager.TYPE_MOBILE
                && netSubtype == TelephonyManager.NETWORK_TYPE_UMTS
                && !mTelephony.isNetworkRoaming()) {
            return info.isConnected();
        } else {
            return false;
        }
    }

    /**
     * 获取屏幕相关信息，宽高、密度等
     * @return
     */
    public static Map<String, Object> getScreen(WindowManager wm){
        // 显示度量，显示终端设备的信息
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        float density  = dm.density;		// 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
        int screenWidth  = (int)(dm.widthPixels * density + 0.5f);		// 屏幕宽（px，如：480px）
        int screenHeight = (int)(dm.heightPixels * density + 0.5f);		// 屏幕高（px，如：800px）
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("W", screenWidth);
        map.put("H", screenHeight);
        map.put("D", density);
        return map;
    }

}