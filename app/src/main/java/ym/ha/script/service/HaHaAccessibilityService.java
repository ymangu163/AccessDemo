package ym.ha.script.service;

import android.os.Build;
import android.os.Message;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.Random;

import ym.ha.script.R;
import ym.ha.script.activity.AppContext;
import ym.ha.script.utils.AppUtil;


/**
 * @author CoderPig on 2018/04/04 13:46.
 */

public class HaHaAccessibilityService extends BaseAccessibilityService {

    private String TAG = "gao";

    private final int MSG_ENTER_MAIN = 0x10;
    private final int MSG_SCROLL_VIDEO = 0x11;
    private final int MSG_START_HAHA = 0x12;

    private final long SHOW_VIDEO_ALL_TIME = 45 * 60 * 1000;

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
//                Log.e(TAG, event.toString());
//                String className2 = event.getClassName().toString();
//                handleScrolled(className2);

                break;

        }
    }

    private void handleScrolled(String className) {
        switch (className) {
            case "android.support.v4.view.ViewPager":
                scrollTimes++;
                if (scrollTimes % 4 == 0) {
                    mHandler.removeCallbacks(null);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AccessibilityNodeInfo videoContainerInfo = findViewByID("com.lswl.qfq:id/vvp");
                            int count = videoContainerInfo.getChildCount();
                            Log.e("vscroll", "" + count);
                        }
                    }, 2000);
                }
                break;
        }
    }

    private void handlePages(String className) {
        switch (className) {
            case "com.lswl.qfq.activity.HaHaSplashActivity":
                mIsLauned = true;
                AppContext.getInstance().setLaunchTime(System.currentTimeMillis());
                break;
            case "com.lswl.qfq.activity.MainActivity":
                long delayTime = 1000;
                if (mIsLauned) {
                    delayTime = 5000;
                }
                mHandler.sendEmptyMessageDelayed(MSG_ENTER_MAIN, delayTime);
                mIsLauned = false;
                break;
            case "com.lswl.qfq.activity.VideoActivity":
                mIsLauned = false;
                int videoDaley = getVideoDelay(50, 30);
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
                int startDelay = getVideoDelay(180, 60);
                Log.e(TAG, startDelay + " 秒后重启");
                mHandler.removeCallbacksAndMessages(null);
                mHandler.sendEmptyMessageDelayed(MSG_START_HAHA, startDelay);
                break;
        }


    }

    private void autoStop() {
        AccessibilityNodeInfo info = findViewByText(getString(R.string.stop_app_text), true);
        if (info == null) {
            return;
        }
        if (info.isEnabled()) {
            performViewClick(info);
        } else {
            performBackClick();
        }


    }

    /**
     * 视频延时，注意要* 1000
     *
     * @param max
     * @param min
     * @return
     */
    private int getVideoDelay(int max, int min) {
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
//                AccessibilityNodeInfo listViewInfo = findViewByClass("android.widget.ListView");
//                if (listViewInfo != null) {
//                    AccessibilityNodeInfo groupInfo = listViewInfo.getParent();
//
////                    listViewInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
////                    groupInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
////                    groupInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
//                    performGlobalAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
//                    performGlobalAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
//                }
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
                AppUtil.openApp();
                break;

        }
    }

    private void enterMainActivity() {
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
        if (System.currentTimeMillis() - AppContext.getInstance().getLaunchTime() >= SHOW_VIDEO_ALL_TIME) {
            AppUtil.stopApp();
            int startDelay = getVideoDelay(180, 60);
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
                    int childCount = videoContainerInfo.getChildCount();
                    int videoDaley = 3400;
                    if (mLastChildCount == (childCount + 1)) {
                        videoDaley = 3400;
                    } else {
                        videoDaley = getVideoDelay(40, 26);
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


