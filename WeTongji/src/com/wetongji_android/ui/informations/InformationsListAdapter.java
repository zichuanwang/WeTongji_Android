package com.wetongji_android.ui.informations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.foound.widget.AmazingAdapter;
import com.wetongji_android.R;
import com.wetongji_android.data.Information;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.data.information.InformationLoader;
import com.wetongji_android.util.data.information.InformationUtil;

public class InformationsListAdapter extends AmazingAdapter implements
		LoaderCallbacks<List<Information>> 
{
	private Fragment mFragment;
	private List<Pair<Date, List<Information>>> mListInfos;
	private Context mContext;
	private LayoutInflater mInflater;
	
	public InformationsListAdapter(Fragment fragment)
	{
		this.mFragment = fragment;
		this.mContext = this.mFragment.getActivity();
		this.mInflater = LayoutInflater.from(this.mContext);
		mListInfos = new ArrayList<Pair<Date, List<Information>>>();
	}
	
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		int res = 0;
		for(int i = 0; i < mListInfos.size(); i++)
		{
			res += mListInfos.get(i).second.size();
		}
		
		return res;
	}

	@Override
	public Object getItem(int position) 
	{
		// TODO Auto-generated method stub
		int res = 0;
		for(int i = 0; i < mListInfos.size(); i++)
		{
			if(position >= res && position < res + mListInfos.get(i).second.size())
			{
				return mListInfos.get(i).second.get(position - res); 
			}
			
			res += mListInfos.get(i).second.size();
		}
		
		return null;
	}

	@Override
	public long getItemId(int position) 
	{
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	protected void onNextPageRequested(int page) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void bindSectionHeader(View view, int position,
			boolean displaySectionHeader) 
	{
		// TODO Auto-generated method stub
		if(displaySectionHeader)
		{
			view.findViewById(R.id.information_list_header).setVisibility(View.VISIBLE);
			TextView tv_header = (TextView)view.findViewById(R.id.information_list_header);
			tv_header.setText(getSections()[getSectionForPosition(position)].toString());
		}else
		{
			view.findViewById(R.id.information_list_header).setVisibility(View.GONE);
		}
	}

	public static class ViewHolder
	{
		TextView tv_type;
		TextView tv_title;
		TextView tv_description;
	}
	
	@Override
	public View getAmazingView(int position, View convertView, ViewGroup parent) 
	{
		// TODO Auto-generated method stub
		ViewHolder holder;
		
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.information_list_item, parent, false);
			holder.tv_type = (TextView)convertView.findViewById(R.id.information_list_item_type);
			holder.tv_title = (TextView)convertView.findViewById(R.id.information_list_item_title);
			holder.tv_description = (TextView)convertView.findViewById(R.id.information_list_item_description);
			convertView.setTag(holder);
		}else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		
		Information information = (Information)getItem(position);
		holder.tv_type.setText(information.getCategory());
		holder.tv_title.setText(information.getTitle());
		holder.tv_description.setText(information.getSummary());
		
		return convertView;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) 
	{
		// TODO Auto-generated method stub
		TextView tvSectionHeader = (TextView)header;
		tvSectionHeader.setText(getSections()[getSectionForPosition(position)].toString());
	}

	@Override
	public int getPositionForSection(int section) 
	{
		// TODO Auto-generated method stub
		if(section < 0)
		{
			section = 0;
		}else if(section >= mListInfos.size())
		{
			section = mListInfos.size() - 1;
		}
		
		int c = 0;
		for(int i = 0; i < mListInfos.size(); i++)
		{
			if(section == i)
			{
				return c;
			}
			
			c += mListInfos.get(i).second.size();
		}
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) 
	{
		// TODO Auto-generated method stub
		int c = 0;
		for(int i = 0; i < mListInfos.size(); i++)
		{
			if(position >= c && position < mListInfos.get(i).second.size())
			{
				return i;
			}
			
			c += mListInfos.get(i).second.size();
		}
		
		return -1;
	}

	@Override
	public Date[] getSections() 
	{
		// TODO Auto-generated method stub
		Date[] dates = new Date[mListInfos.size()];
		for(int i = 0; i < mListInfos.size(); i++)
		{
			dates[i] = mListInfos.get(i).first;
		}
		
		return dates;
	}

	@Override
	public Loader<List<Information>> onCreateLoader(int arg0, Bundle arg1) 
	{
		// TODO Auto-generated method stub
		return new InformationLoader(this.mContext, null);
	}

	@Override
	public void onLoadFinished(Loader<List<Information>> arg0, List<Information> list) 
	{
		// TODO Auto-generated method stub
		if(list != null && list.size() != 0)
		{
			this.setInformations(InformationUtil.getSectionedInformationList(list));
		}else
		{
			((InformationsFragment)mFragment).refreshData();
		}
	}

	@Override
	public void onLoaderReset(Loader<List<Information>> arg0) 
	{
		// TODO Auto-generated method stub
		
	}

	public List<Pair<Date, List<Information>>> getNews() 
	{
		return mListInfos;
	}

	public void setInformations(List<Pair<Date, List<Information>>> mListNews) 
	{
		this.mListInfos.clear();
		this.mListInfos = mListNews;
		notifyDataSetChanged();
	}
	
	public void loadDataFromDB()
	{
		mFragment.getLoaderManager().initLoader(WTApplication.INFORMATION_LOADER, null, this);
	}
}