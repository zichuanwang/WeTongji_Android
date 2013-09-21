package com.wetongji_android.ui.webview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.wetongji_android.R;
import com.wetongji_android.ui.main.MainActivity;
import com.wetongji_android.ui.main.NotificationHandler;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.common.WTBaseFragment;

/**
 * Created by leapoahead on 9/17/13.
 */
public class WebviewFragment extends WTBaseFragment {

    private View mContentView;

    private WebView wvOaMain;
    private ImageButton btnWvGoBack;
    private ImageButton btnWvGoForward;
    private ImageButton btnWvRefresh;

    Bundle data;

    public static WebviewFragment newInstance(Bundle data) {
        WebviewFragment fragment = new WebviewFragment();
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = WebviewFragment.this.getArguments();
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_webview, null);

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        getSherlockActivity().getSupportActionBar()
                .setDisplayShowCustomEnabled(true);
        if (data.getString("title") == "OA System") {
            getSherlockActivity().getSupportActionBar().setCustomView(R.layout.actionbar_oa);
        } else {
            getSherlockActivity().getSupportActionBar().setCustomView(R.layout.actionbar_library);
        }
        getActivity().findViewById(R.id.notification_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NotificationHandler.getInstance().finish();
                        if (WTApplication.getInstance().hasAccount) {
                            ((MainActivity) getActivity()).showRightMenu();
                        } else {
                            Toast.makeText(
                                    getActivity(),
                                    getResources().getText(
                                            R.string.no_account_error),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
