package com.wetongji_android.ui.webview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.wetongji_android.R;

/**
 * Created by leapoahead on 9/17/13.
 */
public class WebViewActivity extends SherlockFragmentActivity {

    private View mContentView;

    private WebView wvOaMain;
    private ImageButton btnWvGoBack;
    private ImageButton btnWvGoForward;
    private ImageButton btnWvRefresh;

    Bundle data;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.drawable.img_wt_logo);

        data = getIntent().getExtras();
        setTitle(data.getString("title"));
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.activity_webview, null);

        btnWvGoBack = (ImageButton) mContentView.findViewById(R.id.oa_btn_go_back);
        btnWvGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wvOaMain.canGoBack()) {
                    wvOaMain.goBack();
                }
            }
        });
        btnWvGoForward = (ImageButton) mContentView.findViewById(R.id.oa_btn_go_forward);
        btnWvGoForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wvOaMain.canGoForward()) {
                    wvOaMain.goForward();
                }
            }
        });
        btnWvRefresh = (ImageButton) mContentView.findViewById(R.id.oa_btn_refresh);
        btnWvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wvOaMain.reload();
            }
        });

        wvOaMain = (WebView) mContentView.findViewById(R.id.oa_webView);
        initWebView();
        return mContentView;
    }

    private void initWebView() {
        wvOaMain.getSettings().setJavaScriptEnabled(true);
        wvOaMain.getSettings().setSupportZoom(true);
        wvOaMain.getSettings().setBuiltInZoomControls(true);
        if (data.getString("title") == "My Library") {
            wvOaMain.setInitialScale(150); // Good for eyes! Simida!
        }
        wvOaMain.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        wvOaMain.setHorizontalScrollbarOverlay(true);
        wvOaMain.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        wvOaMain.loadUrl(data.getString("url"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        }
        return super.onOptionsItemSelected(item);
    }

}
