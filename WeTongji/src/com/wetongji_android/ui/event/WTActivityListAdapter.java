package com.wetongji_android.ui.event;

import java.io.File;
import java.util.List;
import com.androidquery.AQuery;
import com.androidquery.util.AQUtility;
import com.wetongji_android.R;
import com.wetongji_android.data.Event;
import com.wetongji_android.util.common.WTDateParser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WTActivityListAdapter extends ArrayAdapter<Event> {
	
	private LayoutInflater mInflater;
	private Context mContext;
	private AQuery listAq;
	
	public WTActivityListAdapter(Context context, int resource,
			int textViewResourceId, List<Event> objects) {
		super(context, resource, textViewResourceId, objects);
		mInflater=LayoutInflater.from(context);
		mContext=context;
		listAq=new AQuery(mContext);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		
		if(convertView==null){
			holder=new ViewHolder();
			convertView=mInflater.inflate(R.layout.row_event, parent, false);
			holder.tv_event_title=
					(TextView)convertView.findViewById(R.id.tv_event_title);
			holder.tv_event_time=
					(TextView)convertView.findViewById(R.id.tv_event_time);
			holder.tv_event_location = 
					(TextView)convertView.findViewById(R.id.tv_event_location);
			holder.img_event_thumbnails=
					(ImageView)convertView.findViewById(R.id.img_event_thumbnails);
			convertView.setTag(holder);
		}
		else
			holder=(ViewHolder)convertView.getTag();
		
		Event event=getItem(position);
		
		holder.tv_event_title.setText(event.getTitle());
		holder.tv_event_time.setText(
				WTDateParser.parseBeginAndEndTime(event.getBegin(), event.getEnd()));
		
		String strUrl=event.getDescription();
		AQuery aq = listAq.recycle(convertView);
		File ext=Environment.getExternalStorageDirectory();
        File cacheDir=new File(ext, "WeTongji/cache");
        AQUtility.setCacheDir(cacheDir);
        int imageId = holder.img_event_thumbnails.getId();
        Bitmap resetAvatar = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.default_avatar);
        if(aq.shouldDelay(position, convertView, parent, strUrl))
        	aq.image(resetAvatar);
        else
        	aq.id(imageId).image(strUrl, true, true, 0, R.drawable.default_avatar, resetAvatar,
        			AQuery.FADE_IN_NETWORK, 1.0f);
		
		return convertView;
	}

	static class ViewHolder {
		TextView tv_event_title;
		TextView tv_event_time;
		TextView tv_event_location;
		ImageView img_event_thumbnails;
		
	}
	
	
}
