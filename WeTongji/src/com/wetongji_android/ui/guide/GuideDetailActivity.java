package com.wetongji_android.ui.guide;

import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.wetongji_android.R;

/**
 * Created by leapoahead on 9/22/13.
 */
public class GuideDetailActivity extends SherlockFragmentActivity {
    Bundle data;

    private TextView textFrequentTarget;
    private TextView textTimeInterval1, textTimeIntervalTitle1;
    private TextView textTimeInterval2, textTimeIntervalTitle2;
    private TextView textWhere;
    private TextView textCost;
    private ImageView imgBanner1, imgBanner2;
    private HorizontalScrollView hsBanner1, hsBanner2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_detail);

        data = getIntent().getExtras();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.drawable.img_wt_logo);

        setTitle(data.getString("title"));

        textFrequentTarget = (TextView) findViewById(R.id.guide_text_frequent_target_content);
        textTimeInterval1 = (TextView) findViewById(R.id.guide_text_time_interval_1_content);
        textTimeIntervalTitle1 = (TextView) findViewById(R.id.guide_text_time_interval_1_title);
        textTimeInterval2 = (TextView) findViewById(R.id.guide_text_time_interval_2_content);
        textTimeIntervalTitle2 = (TextView) findViewById(R.id.guide_text_time_interval_2_title);
        textWhere = (TextView) findViewById(R.id.guide_text_frequent_entry_content);
        textCost = (TextView) findViewById(R.id.guide_text_cost_content);
        imgBanner1 = (ImageView) findViewById(R.id.guide_img_1);
        imgBanner2 = (ImageView) findViewById(R.id.guide_img_2);

        textFrequentTarget.setText(data.getString("frequent_target"));
        textTimeInterval1.setText(data.getString("time_interval_1"));
        textTimeInterval2.setText(data.getString("time_interval_2"));
        textWhere.setText(data.getString("where"));
        textCost.setText(data.getString("cost"));

        if (data.getString("type").equals("special")) {
            ((HorizontalScrollView) findViewById(R.id.guide_detail_scroll_1)).setVisibility(View.GONE);
            ((HorizontalScrollView) findViewById(R.id.guide_detail_scroll_2)).setVisibility(View.GONE);
        }
        else {
            int bannerId1 = getResources().getIdentifier("guide_" + data.getString("id") + "_1", "drawable", getPackageName());
            imgBanner1.setImageResource(bannerId1);
            int bannerId2 = getResources().getIdentifier("guide_"+data.getString("id")+"_2", "drawable", getPackageName());
            imgBanner2.setImageResource(bannerId2);
            hsBanner1 = (HorizontalScrollView) findViewById(R.id.guide_detail_scroll_1);
            hsBanner1.setHorizontalScrollBarEnabled(false);
            hsBanner2 = (HorizontalScrollView) findViewById(R.id.guide_detail_scroll_2);
            hsBanner2.setHorizontalScrollBarEnabled(false);
        }


        int timeIntervalTitleId1 = getResources().getIdentifier("guide_detail_time_interval_"+data.getString("type")+"_1", "string", getPackageName());
        textTimeIntervalTitle1.setText(getResources().getString(timeIntervalTitleId1));
        int timeIntervalTitleId2 = getResources().getIdentifier("guide_detail_time_interval_"+data.getString("type")+"_2", "string", getPackageName());
        textTimeIntervalTitle2.setText(getResources().getString(timeIntervalTitleId2));
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
