package com.wetongji_android.ui.guide;

import android.os.Bundle;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.wetongji_android.R;

/**
 * Created by leapoahead on 9/21/13.
 */
public class GuideListActivity extends SherlockFragmentActivity {

    private ListView guideList;
    private GuideListAdapter guideListAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_guide);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.drawable.img_wt_logo);

        setTitle(R.string.title_guide);
        setUpList();
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

    private void setUpList() {
        guideList = (ListView) findViewById(R.id.guide_list);
        guideListAdapter = new GuideListAdapter(this, guideList);
        guideList.setAdapter(guideListAdapter);
    }



}
