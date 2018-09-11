package ym.ha.script.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;
import android.widget.LinearLayout;

import ym.ha.script.R;

/**
 * File description
 *
 * @author gao
 * @date 2018/9/10
 */

public class WebActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

       final WebView webView = findViewById(R.id.web_view);
        String privacyUrl = "http://v16.muscdn.com/d14002e92e1d58fa4e90aa74a5f11480/5b96fe8c/video/tos/maliva/tos-maliva-v-0068/e50f04fe6ef843c3ade36f60f01c5116/";
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.loadUrl(privacyUrl);

        webView.postDelayed(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1.0f
                );
                webView.setLayoutParams(param);
            }
        }, 2000);
    }
}
