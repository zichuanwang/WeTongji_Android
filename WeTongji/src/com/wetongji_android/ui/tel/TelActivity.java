package com.wetongji_android.ui.tel;

import android.os.Bundle;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.wetongji_android.R;

/**
 * Created by leapoahead on 9/25/13.
 */
public class TelActivity extends SherlockFragmentActivity {
    private ListView telList;
    private TelListAdapter telListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tel);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.drawable.img_wt_logo);

        setTitle(R.string.title_tel);
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
        telList = (ListView) findViewById(R.id.tel_list);
        telListAdapter = new TelListAdapter(this, telList);
        telList.setAdapter(telListAdapter);
    }
}
