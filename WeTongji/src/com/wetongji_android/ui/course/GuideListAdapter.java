package com.wetongji_android.ui.course;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.wetongji_android.R;

/**
 * Created by leapoahead on 9/21/13.
 */
public class GuideListAdapter extends BaseAdapter {

    private Context context;
    private static LayoutInflater inflater;

    public GuideListAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 2;
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
        View retView;
        if (i == 0) {
            retView = inflater.inflate(R.layout.row_guide_header, null);
        } else {
            retView = inflater.inflate(R.layout.row_guide_item, null);
        }
        return retView;
    }
}
