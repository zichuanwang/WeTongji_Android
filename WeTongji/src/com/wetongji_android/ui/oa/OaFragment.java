package com.wetongji_android.ui.oa;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.wetongji_android.R;
import com.wetongji_android.ui.main.MainActivity;
import com.wetongji_android.ui.main.NotificationHandler;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.common.WTBaseFragment;
import com.wetongji_android.util.net.HttpRequestResult;

/**
 * Created by leapoahead on 9/17/13.
 */
public class OaFragment extends WTBaseFragment {

    private View mContentView;

    private WebView wvOaMain;
    private Button btnWvGoBack;
    private Button btnWvGoForward;
    private Button btnWvRefresh;

    public static OaFragment newInstance() {
        OaFragment fragment = new OaFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_oa, null);

        btnWvGoBack = (Button) mContentView.findViewById(R.id.oa_btn_go_back);
        btnWvGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wvOaMain.canGoBack()) {
                    wvOaMain.goBack();
                }
            }
        });
        btnWvGoForward = (Button) mContentView.findViewById(R.id.oa_btn_go_forward);
        btnWvGoForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wvOaMain.canGoForward()) {
                    wvOaMain.goForward();
                }
            }
        });
        btnWvRefresh = (Button) mContentView.findViewById(R.id.oa_btn_refresh);
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
        wvOaMain.loadUrl("http://wapoa.tongji.edu.cn/");
        wvOaMain.getSettings().setSupportZoom(true);
        wvOaMain.getSettings().setBuiltInZoomControls(false);
        wvOaMain.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        wvOaMain.setHorizontalScrollbarOverlay(true);
        wvOaMain.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        getSherlockActivity().getSupportActionBar()
                .setDisplayShowCustomEnabled(true);
        getSherlockActivity().getSupportActionBar().setCustomView(R.layout.actionbar_oa);
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
