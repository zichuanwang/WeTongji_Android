package com.wetongji_android.ui.guide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wetongji_android.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by leapoahead on 9/21/13.
 */
public class GuideListAdapter extends BaseAdapter {
    private Context context;
    private static LayoutInflater inflater;
    private JsonArray dataArray;

    private ListView lv;

    public GuideListAdapter(Context _context, ListView _lv) {
        this.lv = _lv;
        this.context = _context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        lv.setOnItemClickListener(new GuideListOnClickListener());

        initData();
    }

    @Override
    public int getCount() {
        return dataArray.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        JsonObject data = dataArray.get(i).getAsJsonObject();

        View retView;
        retView = inflater.inflate(R.layout.row_guide_item, null);

        if (data.get("campus") != null) {
            ((TextView)retView.findViewById(R.id.guide_item_title)).setVisibility(View.GONE);
            ((TextView)retView.findViewById(R.id.guide_item_summary)).setVisibility(View.GONE);
            ((TextView)retView.findViewById(R.id.guide_header_text)).setText(data.get("campus").getAsString());
        }
        else {
            ((FrameLayout)retView.findViewById(R.id.layout_guide_header)).setVisibility(View.GONE);
            ((TextView)retView.findViewById(R.id.guide_item_title)).setText(data.get("title").getAsString());
            ((TextView)retView.findViewById(R.id.guide_item_summary)).setText("常去：" + data.get("frequent_target").getAsString());
        }


        if (i % 2 != 0) {
            ((LinearLayout)retView.findViewById(R.id.layout_guide_bg)).setBackgroundResource(R.drawable.listview_selector_1);
        } else {
            ((LinearLayout)retView.findViewById(R.id.layout_guide_bg)).setBackgroundResource(R.drawable.listview_selector_2);
        }

        return retView;
    }

    private void initData(){
        InputStream is = context.getResources().openRawResource(R.raw.guide);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
            is.close();
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

        String jsonString = writer.toString();
        JsonElement dataElement = (new JsonParser()).parse(jsonString);
        dataArray = dataElement.getAsJsonArray();

    }

    public class GuideListOnClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (dataArray.get(i).getAsJsonObject().get("campus") != null) {
                return;
            }

            Bundle dataBundle = new Bundle();
            JsonObject dataObj = dataArray.get(i).getAsJsonObject();

            dataBundle.putString("type", dataObj.get("type").getAsString());
            dataBundle.putString("id", dataObj.get("id").getAsString());
            dataBundle.putString("title", dataObj.get("title").getAsString());
            dataBundle.putString("frequent_target", dataObj.get("frequent_target").getAsString());
            dataBundle.putString("time_interval_1", dataObj.get("time_interval_1").getAsString());
            dataBundle.putString("time_interval_2", dataObj.get("time_interval_2").getAsString());
            dataBundle.putString("where", dataObj.get("where").getAsString());
            dataBundle.putString("cost", dataObj.get("cost").getAsString());

            Intent intent = new Intent(GuideListAdapter.this.context, GuideDetailActivity.class);
            intent.putExtras(dataBundle);
            context.startActivity(intent);
        }
    }

}
