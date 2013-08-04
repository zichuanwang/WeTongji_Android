package com.wetongji_android.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wetongji_android.R;
import com.wetongji_android.ui.event.EventsFragment;
import com.wetongji_android.ui.informations.InformationsFragment;
import com.wetongji_android.ui.now.NowFragment;
import com.wetongji_android.ui.profile.ProfileFragment;
import com.wetongji_android.ui.search.SearchFragment;
import com.wetongji_android.ui.setting.DevSettingActivity;
import com.wetongji_android.ui.today.TodayFragment;
import com.wetongji_android.util.common.WTBaseFragment.StartMode;

public class MainMenuFragment extends Fragment {

	public static final String KEY_MAIN_MENU_ICON = "icon";
	public static final String KEY_MAIN_MENU_TEXT = "text";
	private static final int MAIN_MENU_ICON_RES[] = { R.drawable.ic_main_today,
			R.drawable.ic_main_news, R.drawable.ic_main_events,
			R.drawable.ic_main_now, R.drawable.ic_main_search,
			R.drawable.ic_main_bboard, R.drawable.ic_main_profile, };

	private static final int MAIN_MENU_ICON_SELECTED_RES[] = {
			R.drawable.ic_main_today_pressed, R.drawable.ic_main_news_pressed,
			R.drawable.ic_main_events_pressed, R.drawable.ic_main_now_pressed,
			R.drawable.ic_main_search_pressed,
			R.drawable.ic_main_bboard_pressed,
			R.drawable.ic_main_profile_pressed, };

	private static final int MAIN_MENU_TEXT_RES[] = {
			R.string.title_mainmenu_today, R.string.title_mainmenu_news,
			R.string.title_mainmenu_events, R.string.title_mainmenu_now,
			R.string.title_mainmenu_search, R.string.title_mainmenu_billboard,
			R.string.title_mainmenu_profile, };

	private int mCurrentItemNu = 0;
	private MainMenuListAdapter mMenuListAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.main_menu, null);
		Button btnSetting = (Button) view.findViewById(R.id.btn_main_menu_setting);
		btnSetting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(getActivity(), DevSettingActivity.class));
			}
		});
		
		return view;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		ListView lstViewMenu = (ListView) getActivity().findViewById(
				R.id.main_memu_list);

		mMenuListAdapter = new MainMenuListAdapter(getActivity());

		lstViewMenu.setAdapter(mMenuListAdapter);
		lstViewMenu.setOnItemClickListener(new MainMenuListItemClickListener());
	}

	public class MainMenuListAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public MainMenuListAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return MAIN_MENU_ICON_RES.length;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			convertView = mInflater.inflate(R.layout.row_main_menu, null);
			ImageView image = (ImageView) convertView
					.findViewById(R.id.img_main_menu);
			TextView text = (TextView) convertView
					.findViewById(R.id.tv_main_menu);
			image.setImageResource(MAIN_MENU_ICON_RES[position]);
			text.setText(MAIN_MENU_TEXT_RES[position]);

			if (position == mCurrentItemNu) {
				convertView.setBackgroundColor(getResources().getColor(
						R.color.main_menu_selected));
				image.setImageResource(MAIN_MENU_ICON_SELECTED_RES[position]);
			}

			return convertView;
		}

	}

	public class MainMenuListItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			if (getActivity() == null)
				return;

			((MainActivity) getActivity()).getSlidingMenu().showContent();

			Fragment newContent = null;

			// Change item background
			mCurrentItemNu = position;
			mMenuListAdapter.notifyDataSetChanged();

			// TODO Change the fragment
			switch (position) {
			case 0:
				newContent = TodayFragment.newInstance();
				break;
			case 1:
				newContent = InformationsFragment.newInstance(StartMode.BASIC, null);
				break;
			case 2:
				newContent = EventsFragment.newInstance(StartMode.BASIC, null);
				break;
			case 3:
				newContent = NowFragment.newInstance();
				break;
			case 4:
				newContent = SearchFragment.newInstance();
				break;
			case 6:
				newContent = ProfileFragment.newInstance();
				break;
			default:
				break;
			}

			if (newContent != null) {
				switchFragment(newContent);
			}
		}

	}

	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;
		MainActivity ma = (MainActivity) getActivity();
		ma.switchContent(fragment);
	}

}
