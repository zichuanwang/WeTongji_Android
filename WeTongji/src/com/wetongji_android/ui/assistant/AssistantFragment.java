package com.wetongji_android.ui.assistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.wetongji_android.R;
import com.wetongji_android.ui.main.MainActivity;
import com.wetongji_android.ui.main.NotificationHandler;
import com.wetongji_android.ui.webview.WebViewActivity;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.common.WTBaseFragment;

/**
 * Created by leapoahead on 9/21/13.
 */
public class AssistantFragment extends WTBaseFragment {
    public static final int MSG_SWITCH_CONTENT = 2313;

    private View mContentView;
    private Activity mActivity;

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
                Intent intent = new Intent(mActivity, WebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", getResources().getString(R.string.title_oa));
                bundle.putString("url", "http://wapoa.tongji.edu.cn/");
                intent.putExtras(bundle);
                startActivity(intent);
                mActivity.overridePendingTransition(R.anim.slide_right_in,
                        R.anim.slide_left_out);
            }
        });

        btnLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, WebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", getResources().getString(R.string.title_library));
                bundle.putString("url", "http://www.lib.tongji.edu.cn/m/index.action");
                intent.putExtras(bundle);
                startActivity(intent);
                mActivity.overridePendingTransition(R.anim.slide_right_in,
                        R.anim.slide_left_out);
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mActivity = activity;
        ((MainActivity) mActivity).getSupportActionBar()
                .setDisplayShowTitleEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }



}