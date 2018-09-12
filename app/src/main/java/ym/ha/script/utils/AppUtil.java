package ym.ha.script.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.security.MessageDigest;
import java.util.Calendar;

import ym.ha.script.activity.AppContext;

/**
 * File description
 *
 * @author gao
 * @date 2018/9/10
 */

public class AppUtil {

    public static void openApp() {
        Context context = AppContext.getInstance();
        Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.lswl.qfq");
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        } else {
            ToastUtils.showToastForLong(context, "请先安装HAHA小视频");
        }
    }

    public static boolean canOpenApp() {
        if (!AppSharePref.getInstance().getNightMask()) {
            return true;
        }

        Calendar calendar = Calendar.getInstance();//
        int curHor = calendar.get(Calendar.HOUR_OF_DAY);//时
        int curMin = calendar.get(Calendar.MINUTE);//分
        String startTime = AppSharePref.getInstance().getMaskStartTime();
        String endTime = AppSharePref.getInstance().getMaskEndTime();
        String[] start1 = startTime.split(":");
        String[] end1 = endTime.split(":");
        if (curHor < Integer.valueOf(start1[0])) {
            return true;
        } else if (curHor == Integer.valueOf(start1[0])) {
            if (curMin < Integer.valueOf(start1[1])) {
                return true;
            } else {
                return false;
            }
        } else if (curHor > Integer.valueOf(end1[0])) {
            return true;
        } else if (curHor == Integer.valueOf(end1[0])) {
            if (curMin < Integer.valueOf(end1[1])) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }


    public static void stopApp() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromParts("package", "com.lswl.qfq", null);
        intent.setData(uri);
        AppContext.getInstance().startActivity(intent);
        Log.e("gao", "stop time :" + System.currentTimeMillis());
    }

    public static void openKeybord(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.RESULT_SHOWN);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    public static void closeKeybord(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static String getMd5(String _key){
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(_key.getBytes("UTF-8"));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & 0xFF) < 0x10) hex.append("0");
                hex.append(Integer.toHexString(b & 0xFF));
            }
            return hex.toString();
        }catch (Exception e) {
            return null;
        }
    }

}
