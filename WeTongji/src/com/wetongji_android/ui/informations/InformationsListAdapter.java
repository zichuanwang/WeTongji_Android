package com.wetongji_android.ui.informations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foound.widget.AmazingAdapter;
import com.wetongji_android.R;
import com.wetongji_android.data.Information;
import com.wetongji_android.factory.InformationFactory;
import com.wetongji_android.net.WTClient;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.data.information.InformationLoader;
import com.wetongji_android.util.data.information.InformationUtil;
import com.wetongji_android.util.date.DateParser;
import com.wetongji_android.util.exception.WTException;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

public class InformationsListAdapter extends AmazingAdapter implements
		LoaderCallbacks<List<Information>> 
{
	private Fragment mFragment;
	private List<Pair<Date, List<Information>>> mListInfos;
	private Context mContext;
	private LayoutInflater mInflater;
	private List<Information> originList;
	private boolean isLoadingData;
	private int iSelectType;
	
	private AsyncTask<Integer, Void, List<Pair<Date, List<Information>>>> backgroundTask;
	
	public InformationsListAdapter(Fragment fragment, AbsListView listView)
	{
		this.mFragment = fragment;
		this.mContext = this.mFragment.getActivity();
		this.mInflater = LayoutInflater.from(this.mContext);
		this.isLoadingData = true;
		mListInfos = new ArrayList<Pair<Date, List<Information>>>();
		setOriginList(new ArrayList<Information>());
	}
	
	@Override
	public int getCount() 
	{
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
		return position;
	}

	@Override
	protected void onNextPageRequested(int page) 
	{	
		if(backgroundTask != null){
			backgroundTask.cancel(false);
		}
		
		backgroundTask = new AsyncTask<Integer, Void, List<Pair<Date, List<Information>>>>(){

			@Override
			protected List<Pair<Date, List<Information>>> doInBackground(
					Integer... params) {
				int p = params[0];
				WTClient client = WTClient.getInstance();
				ApiHelper apiHelper = ApiHelper.getInstance(mContext);
				try {
					HttpRequestResult result = client.execute(HttpMethod.Get, apiHelper.getInformations(p, iSelectType));
					InformationFactory factory = new InformationFactory(mFragment);
					Pair<Integer, List<Information>> infos = factory.createObjects(result.getStrResponseCon(), 1);
					List<Information> lists = infos.second;
					return InformationUtil.getSectionedInformationList(lists);
				} catch (WTException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(
					List<Pair<Date, List<Information>>> result) {
				if(isCancelled()) return;
				
				if(result != null){
					mListInfos.addAll(result);
					nextPage();
					notifyDataSetChanged();
				}else{
					notifyNoMorePages();
					Toast.makeText(mContext, R.string.text_no_more_data, Toast.LENGTH_SHORT).show();
				}
			}
			
		}.execute(page);
	}

	@Override
	protected void bindSectionHeader(View view, int position,
			boolean displaySectionHeader) 
	{	
		if(displaySectionHeader)
		{
			view.findViewById(R.id.layout_information_header).setVisibility(View.VISIBLE);
			configureHeader(view, position);
		}else
		{
			view.findViewById(R.id.layout_information_header).setVisibility(View.GONE);
		}
	}

	public static class ViewHolder
	{
		TextView tv_type;
		TextView tv_title;
		TextView tv_description;
		RelativeLayout rl_item;
		ImageView iv_ticketIcon;
	}
	
	@Override
	public View getAmazingView(int position, View convertView, ViewGroup parent) 
	{
		ViewHolder holder;
	
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.row_information_item, parent, false);
			holder.tv_type = (TextView)convertView.findViewById(R.id.information_list_item_type);
			holder.tv_title = (TextView)convertView.findViewById(R.id.information_list_item_title);
			holder.tv_description = (TextView)convertView.findViewById(R.id.information_list_item_description);
			holder.rl_item = (RelativeLayout)convertView.findViewById(R.id.information_list_item);
			holder.iv_ticketIcon = (ImageView) convertView.findViewById(R.id.icon_ticket);
			convertView.setTag(holder);
		}else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		
		Information information = (Information)getItem(position);
		int section = getSectionForPosition(position);
		int pos = getPositionForSection(section);
		if((position - pos) % 2 == 0)
		{
			holder.rl_item.setBackgroundResource(R.drawable.listview_selector_1);
		}else
		{
			holder.rl_item.setBackgroundResource(R.drawable.listview_selector_2);
		}
		if (information.getCategory().equals("周边推荐")
				&& !TextUtils.isEmpty(information.getTicketService())) {
			holder.iv_ticketIcon.setVisibility(View.VISIBLE);
		} else {
			holder.iv_ticketIcon.setVisibility(View.GONE);
		}
		holder.tv_type.setText(information.getCategory());
		holder.tv_title.setText(information.getTitle());
		if(information.getCategory().equals("组织动态")) {
			holder.tv_description.setText(information.getContext());
		} else {
			holder.tv_description.setText(information.getSummary());
		}
		
		return convertView;
	}

	public boolean isLoadingData() {
		return isLoadingData;
	}

	public void setLoadingData(boolean isLoadingData) {
		this.isLoadingData = isLoadingData;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) 
	{
		configureHeader(header, position);
	}

	private void configureHeader(View header, int position)
	{
		TextView tvSectionHeader = (TextView)header.findViewById(R.id.information_list_header);
		if(DateParser.isToday(getSections()[getSectionForPosition(position)]))
		{
			tvSectionHeader.setText("Today");
		}else
		{
			tvSectionHeader.setText(DateParser.parseDateForInformation(getSections()[getSectionForPosition(position)]));
		}
	}
	
	@Override
	public int getPositionForSection(int section) 
	{
		if(section < 0) section = 0;
		if(section >= mListInfos.size()) section = mListInfos.size() - 1;
		
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
		int c = 0;
		for(int i = 0; i < mListInfos.size(); i++)
		{
			if(position >= c && position < c + mListInfos.get(i).second.size())
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
		return new InformationLoader(this.mContext, arg1);
	}

	@Override
	public void onLoadFinished(Loader<List<Information>> arg0, List<Information> list) 
	{
		if(list != null && list.size() != 0)
		{
			this.setInformations(InformationUtil.getSectionedInformationList(list));
			this.setOriginList(list);
			this.setLoadingData(false);
			this.notifyMayHaveMorePages();
		}else
		{
			((InformationsFragment)mFragment).refreshData();
		}
	}

	@Override
	public void onLoaderReset(Loader<List<Information>> arg0) 
	{
		
	}

	public List<Pair<Date, List<Information>>> getNews() 
	{
		return mListInfos;
	}

	public void setInformations(List<Pair<Date, List<Information>>> mListNews) 
	{
		this.mListInfos.addAll(mListNews);
		notifyDataSetChanged();
	}
	
	public void clear()
	{
		this.mListInfos.clear();
		notifyDataSetChanged();
	}
	
	public void loadDataFromDB(Bundle bundle)
	{	
		mFragment.getLoaderManager().initLoader(WTApplication.INFORMATION_LOADER, bundle, this);
	}

	public List<Information> getOriginList() {
		return originList;
	}

	public void setOriginList(List<Information> originList) {
		this.originList = originList;
	}

	public void setiSelectType(int iSelectType) {
		this.iSelectType = iSelectType;
	}
}