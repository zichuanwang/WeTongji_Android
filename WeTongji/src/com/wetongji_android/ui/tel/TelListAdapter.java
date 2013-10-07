package com.wetongji_android.ui.tel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
 * Created by leapoahead on 9/25/13.
 */
public class TelListAdapter extends BaseAdapter {
    private Context context;
    private static LayoutInflater inflater;
    private JsonArray dataArray;

    private ListView lv;

    public TelListAdapter(Context _context, ListView _lv) {
        this.context = _context;
        this.lv = _lv;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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

        View retView = inflater.inflate(R.layout.row_tel_item, null);

        if (i == 0 ||
                !dataArray.get(i - 1).getAsJsonObject().get("campus").getAsString().equals(data.get("campus").getAsString()))
        {
            ((TextView)retView.findViewById(R.id.tel_header_text)).setText(data.get("campus").getAsString());
        }
        else {
            ((FrameLayout)retView.findViewById(R.id.layout_tel_header)).setVisibility(View.GONE);
        }

        ((TextView)retView.findViewById(R.id.tel_item_title)).setText(data.get("addr").getAsString());
        ((TextView)retView.findViewById(R.id.tel_item_summary)).setText(data.get("tel").getAsString());
        ((ImageButton)retView.findViewById(R.id.tel_call)).setOnClickListener(new TelIconOnClickListener(data.get("tel").getAsString()));

        if (i % 2 != 0) {
            ((RelativeLayout)retView.findViewById(R.id.layout_tel_bg)).setBackgroundResource(R.drawable.listview_selector_1);
            ((FrameLayout)retView.findViewById(R.id.layout_tel_header)).setBackgroundResource(R.drawable.listview_selector_2);
        } else {
            ((RelativeLayout)retView.findViewById(R.id.layout_tel_bg)).setBackgroundResource(R.drawable.listview_selector_2);
            ((FrameLayout)retView.findViewById(R.id.layout_tel_header)).setBackgroundResource(R.drawable.listview_selector_1);
        }

        return retView;
    }

    private void initData(){
        InputStream is = context.getResources().openRawResource(R.raw.tel);
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

    private void call(String phone) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phone));
        context.startActivity(callIntent);
    }

    public class TelIconOnClickListener implements View.OnClickListener {
        private String num;

        public TelIconOnClickListener(String _num){
            this.num = _num;
        }
        @Override
        public void onClick(View view) {
            call(num);
        }
    }
}
