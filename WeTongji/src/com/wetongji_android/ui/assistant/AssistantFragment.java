package com.wetongji_android.ui.assistant;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.wetongji_android.R;
import com.wetongji_android.ui.main.MainActivity;
import com.wetongji_android.ui.main.NotificationHandler;
import com.wetongji_android.ui.webview.WebviewFragment;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.common.WTBaseFragment;

/**
 * Created by leapoahead on 9/21/13.
 */
public class AssistantFragment extends WTBaseFragment {
    public static final int MSG_SWITCH_CONTENT = 2313;

    private View mContentView;

    private ImageButton btnOA;
    private ImageButton btnLibrary;
    private ImageButton btnTel;
    private ImageButton btnGuide;

    public static AssistantFragment newInstance() {
        AssistantFragment fragment = new AssistantFragment();
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_assistant, null);

        btnOA       = (ImageButton) mContentView.findViewById(R.id.btn_assistant_oa);
        btnLibrary  = (ImageButton) mContentView.findViewById(R.id.btn_assistant_library);
        btnTel      = (ImageButton) mContentView.findViewById(R.id.btn_assistant_tel);
        btnGuide    = (ImageButton) mContentView.findViewById(R.id.btn_assistant_guide);

        btnOA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle data = new Bundle();
                data.putString("url", "http://wapoa.tongji.edu.cn/");
                data.putString("title", "OA System");
                WebviewFragment wv = WebviewFragment.newInstance(data);
                switchFragmentDelay(wv);
            }
        });


        return mContentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        getSherlockActivity().getSupportActionBar()
                .setDisplayShowCustomEnabled(true);
        getSherlockActivity().getSupportActionBar().setCustomView(R.layout.actionbar_assistant);

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


    private void switchFragmentDelay(final Fragment newContent) {
        // delay some time to switch fragment
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MSG_SWITCH_CONTENT) {
                    switchFragment(newContent);
                }
            }
        };
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = MSG_SWITCH_CONTENT;
                handler.sendMessage(msg);
                handler.removeCallbacks(this);
            }
        };
        handler.postDelayed(runnable, 300);
    }

    private void switchFragment(Fragment fragment) {
        if (getActivity() == null)
            return;
        MainActivity ma = (MainActivity) getActivity();
        ma.switchContent(fragment);
    }

}