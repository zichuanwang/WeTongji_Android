package com.wetongji_android.ui.course;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.wetongji_android.R;
import com.wetongji_android.data.Course;
import com.wetongji_android.factory.CourseFactory;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.common.WTBaseFragment;
import com.wetongji_android.util.exception.ExceptionToast;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

public class CourseListActivity extends SherlockFragmentActivity 
implements LoaderCallbacks<HttpRequestResult>, OnScrollListener{
	
	private ListView mLvCourses;
	private CourseListAdapter mCourseAdapter;
	private int mCurrentPage = 0;
	private boolean mIsRefresh = false;
	private String mUID;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		mUID = getIntent().getStringExtra(WTBaseFragment.BUNDLE_KEY_UID);
		setContentView(R.layout.activity_course_list);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.profile_section_participated_course);
		mLvCourses = (ListView) findViewById(R.id.lv_my_courses);
		mCourseAdapter = new CourseListAdapter(this, mLvCourses);
		mLvCourses.setAdapter(mCourseAdapter);
		mLvCourses.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Course course = mCourseAdapter.getItem(arg2);
				Bundle bundle = new Bundle();
				Intent intent = new Intent(CourseListActivity.this, CourseDetailActivity.class);
				bundle.putParcelable(CourseDetailActivity.BUNDLE_COURSE, course);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		
		loadMoreData(1);
	}

	@Override
	public Loader<HttpRequestResult> onCreateLoader(int loadId, Bundle bundle) {
		return new NetworkLoader(this, HttpMethod.Get, bundle);
	}

	@Override
	public void onLoadFinished(Loader<HttpRequestResult> loader,
			HttpRequestResult result) {
		getSupportLoaderManager().destroyLoader(WTApplication.NETWORK_LOADER_DEFAULT);
		if (result.getResponseCode() == 0) {
			CourseFactory courseFactory = new CourseFactory();
			List<Course> lstCourse = courseFactory.parseCoursesForUser(result
					.getStrResponseCon());
			Log.v("course size", "" + lstCourse.size());
			if (mIsRefresh) {
				mCurrentPage = 0;
				mCourseAdapter.clear();
			}
			mCurrentPage ++;
			mCourseAdapter.addAll(lstCourse);
			mCourseAdapter.setIsLoadingData(false);
		} else {
			ExceptionToast.show(this, result.getResponseCode());
		}
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) {
	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		if (mCourseAdapter.shouldRequestNextPage(arg1, arg2, arg3)) {
				loadMoreData(mCurrentPage + 1);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
	}
	
	private void loadMoreData(int page) {
		mCourseAdapter.setIsLoadingData(true);
		ApiHelper apiHelper = ApiHelper.getInstance(this);
		Bundle args = apiHelper.getCourseByUser(mUID, page);
		getSupportLoaderManager().restartLoader(
				WTApplication.NETWORK_LOADER_DEFAULT, args, this);
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			finish();
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
		}
		
		return super.onKeyDown(keyCode, event);
	}
}
