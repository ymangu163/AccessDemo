package ym.ha.script.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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


    public static void stopApp() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromParts("package", "com.lswl.qfq", null);
        intent.setData(uri);
        AppContext.getInstance().startActivity(intent);
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
}
