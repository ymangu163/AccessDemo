package ym.ha.script.service;

import android.os.Build;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.Random;

import ym.ha.script.R;
import ym.ha.script.activity.AppContext;
import ym.ha.script.utils.AppSharePref;
import ym.ha.script.utils.AppUtil;
import ym.ha.script.utils.ToastUtils;


/**
 * @author CoderPig on 2018/04/04 13:46.
 */

public class HaHaAccessibilityService extends BaseAccessibilityService {

    private String TAG = "gao";

    private final int MSG_ENTER_MAIN = 0x10;
    private final int MSG_SCROLL_VIDEO = 0x11;
    private final int MSG_START_HAHA = 0x12;
    private final int MSG_GET_HAHA_ID = 0x13;

    private long mVideoAllTime = 45 * 60 * 1000;

    private boolean mIsLauned;
    private Random mRandom = new Random();
    private int scrollTimes = 0;
    private int mLastChildCount = 0;


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
//        if (!event.getClassName().equals("android.widget.TextView")) {
//            Log.e(TAG, event.toString());
//        }

        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                Log.e(TAG, event.toString());
                String className = event.getClassName().toString();
                handlePages(className);

                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:

                break;

        }
    }

    private void handlePages(String className) {
        if (!AppSharePref.getInstance().getAuthorized()) {
            ToastUtils.showToastForLong(getApplication(), "请先注册");
            return;
        }

        switch (className) {
            case "com.lswl.qfq.activity.HaHaSplashActivity":
                mIsLauned = true;
                int randomTime = getRandomDelay(65, 45);
                mVideoAllTime = randomTime * 60;
                AppContext.getInstance().setLaunchTime(System.currentTimeMillis());
                break;
            case "com.lswl.qfq.mvp.activity.HomeActivity":
                if (AppSharePref.getInstance().getAuthoChecked()) {
                    long delayTime = 1000;
                    if (mIsLauned) {
                        delayTime = 5000;
                    }
                    mHandler.sendEmptyMessageDelayed(MSG_ENTER_MAIN, delayTime);
                    mIsLauned = false;
                } else {
                    checkAuthority();
                }
                break;
            case "com.lswl.qfq.activity.VideoActivity":
                mIsLauned = false;
                int videoDaley = getRandomDelay(50, 30);
                mHandler.sendEmptyMessageDelayed(MSG_SCROLL_VIDEO, videoDaley);
                break;
            case "com.android.settings.applications.InstalledAppDetailsTop":
                autoStop();
                break;
            case "android.app.AlertDialog":
                clickTextViewByText(getString(R.string.dialog_ok));
                performBackClick();
                break;
            case "com.lswl.qfq.views.CaptchDialog":
                AppUtil.stopApp();
                int startDelay = getRandomDelay(180, 60);
                Log.e(TAG, startDelay + " 秒后重启");
                mHandler.removeCallbacksAndMessages(null);
                mHandler.sendEmptyMessageDelayed(MSG_START_HAHA, startDelay);
                break;
        }


    }

    private void autoStop() {
        AccessibilityNodeInfo info = findViewByText(getString(R.string.stop_app_text), true);
        if (info == null) {
            info = findViewByText(getString(R.string.stop_app_text2), true);
        }

        if (info == null) {
            return;
        }
        if (info.isEnabled()) {
            performViewClick(info);
        } else {
            performBackClick();
        }


    }

    private void checkAuthority() {
        clickViewByID("com.lswl.qfq:id/rBtnMine");
        mHandler.sendEmptyMessageDelayed(MSG_GET_HAHA_ID, 500);

    }

    /**
     * 视频延时，注意要* 1000
     *
     * @param max
     * @param min
     * @return
     */
    private int getRandomDelay(int max, int min) {
        int delay = mRandom.nextInt(max - min) + min;
        Log.e(TAG, "delay: " + delay);
        return delay * 1000;
    }


    private void scrollListView() {
        AccessibilityNodeInfo searchNodeInfo = findViewByID("free.vpn.unblock.proxy.vpnpro:id/main_tab_server_title");
        performViewClick(searchNodeInfo);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    slideVertical(6, 14);
                }

            }
        }, 2000);
    }


    @Override
    public void onInterrupt() {

    }


    protected void dispatchMessage(Message msg) {
        switch (msg.what) {
            case MSG_ENTER_MAIN:
                enterMainActivity();
                break;
            case MSG_SCROLL_VIDEO:
                scrollVideos();
                break;
            case MSG_START_HAHA:
                if (AppUtil.canOpenApp()) {
                    AppUtil.openApp();
                } else {
                    mHandler.removeCallbacksAndMessages(null);
                    mHandler.sendEmptyMessageDelayed(MSG_START_HAHA, 10 * 60 * 1000);
                }
                break;
            case MSG_GET_HAHA_ID:
                getHahaId();
                break;

        }
    }

    private void getHahaId() {
        AccessibilityNodeInfo idInfo = findViewByID("com.lswl.qfq:id/user_number_tv");
        if (idInfo == null) {
            mHandler.sendEmptyMessageDelayed(MSG_GET_HAHA_ID, 500);
            return;
        }
        String hahaId = idInfo.getText().toString();
        String savedId = AppSharePref.getInstance().getHahaId();
        if (TextUtils.equals(hahaId, savedId)) {
            AppSharePref.getInstance().setAuthoChecked(true);
            clickViewByID("com.lswl.qfq:id/rBtnWatchVideo");
            mHandler.sendEmptyMessageDelayed(MSG_ENTER_MAIN, 1000);
        } else {
            AppSharePref.getInstance().setAuthoChecked(false);
            ToastUtils.showToastForLong(this, "与您注册的HaHa ID 不符");
        }

    }

    private void enterMainActivity() {
        if (!AppSharePref.getInstance().getAuthorized()) {
            ToastUtils.showToastForLong(this, "请先注册");
            return;
        }

        if (!AppSharePref.getInstance().getAuthoChecked()) {
            ToastUtils.showToastForLong(this, "与您注册的HaHa ID 不符");
            return;
        }

        AccessibilityNodeInfo recyclerViewInfo = findViewByID("com.lswl.qfq:id/myxRecyclerView");
        if (recyclerViewInfo == null) {
            mHandler.sendEmptyMessageDelayed(MSG_ENTER_MAIN, 1000);
            return;
        }
        int childCount = recyclerViewInfo.getChildCount();
        AccessibilityNodeInfo videoLayoutInfo = null;
        for (int i = 0; i < childCount; i++) {
            videoLayoutInfo = recyclerViewInfo.getChild(i);
            if (videoLayoutInfo.getClassName().equals("android.widget.LinearLayout")
                    && videoLayoutInfo.isClickable()) {
                break;
            }
        }
        if (videoLayoutInfo == null) {
            mHandler.sendEmptyMessageDelayed(MSG_ENTER_MAIN, 1000);
            return;
        }
        performViewClick(videoLayoutInfo);
    }

    private void scrollVideos() {
        if (System.currentTimeMillis() - AppContext.getInstance().getLaunchTime() >= mVideoAllTime) {
            Log.e(TAG, "mVideoAllTime: " + mVideoAllTime);
            AppUtil.stopApp();
            int startDelay = getRandomDelay(180, 60);
            Log.e(TAG, startDelay + " 秒后重启");
            mHandler.removeCallbacksAndMessages(null);
            mHandler.sendEmptyMessageDelayed(MSG_START_HAHA, startDelay);
            return;
        }

        AccessibilityNodeInfo viewPagerInfo = findViewByID("com.lswl.qfq:id/vvp");
        if (viewPagerInfo == null) {
            performBackClick();
            return;
        }
        if (viewPagerInfo.getClassName().equals("android.support.v4.view.ViewPager")
                && viewPagerInfo.isScrollable()) {
            viewPagerInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            mHandler.removeMessages(MSG_SCROLL_VIDEO);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AccessibilityNodeInfo videoContainerInfo = findViewByID("com.lswl.qfq:id/vvp");
                    if (videoContainerInfo == null) {
                        mHandler.sendEmptyMessageDelayed(MSG_SCROLL_VIDEO, 4000);
                        return;
                    }
                    int childCount = videoContainerInfo.getChildCount();
                    int videoDaley = 3400;
                    if (mLastChildCount == (childCount + 1)) {
                        videoDaley = 3400;
                    } else {
                        videoDaley = getRandomDelay(40, 26);
                    }
                    mLastChildCount = childCount;
                    mHandler.sendEmptyMessageDelayed(MSG_SCROLL_VIDEO, videoDaley);
                }
            }, 2000);
        } else {
            performBackClick();
        }
    }


}


