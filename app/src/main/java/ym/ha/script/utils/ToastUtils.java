package ym.ha.script.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

	private static long lastShowTime;

	public static void showToastForShort(Context context, int msgId) {
		if (!isShow()) {
			return;
		}
		Toast.makeText(context, context.getResources().getString(msgId),
				Toast.LENGTH_SHORT).show();
		lastShowTime = System.currentTimeMillis();
	}

	public static void showToastForShort(Context context, String msg) {
		if (!isShow()) {
			return;
		}
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
		lastShowTime = System.currentTimeMillis();
	}

	public static void showToastForLong(Context context, int msgId) {
		if (!isShow()) {
			return;
		}
		Toast.makeText(context, context.getResources().getString(msgId),
				Toast.LENGTH_LONG).show();
		lastShowTime = System.currentTimeMillis();
	}

	public static void showToastForLong(Context context, String msg) {
		if (!isShow()) {
			return;
		}
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
		lastShowTime = System.currentTimeMillis();
	}

	private static boolean isShow() {
		if (System.currentTimeMillis() - lastShowTime < 2000) {
			return false;
		}
		return true;
	}
}
