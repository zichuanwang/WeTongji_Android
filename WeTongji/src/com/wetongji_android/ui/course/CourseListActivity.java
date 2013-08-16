package com.wetongji_android.ui.course;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.wetongji_android.R;
import com.wetongji_android.data.Course;
import com.wetongji_android.factory.CourseFactory;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.exception.ExceptionToast;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

public class CourseListActivity extends SherlockFragmentActivity 
implements LoaderCallbacks<HttpRequestResult>, OnScrollListener{

	public static final String BUNDLE_KEY_UID = "BUNDLE_KEY_UID";
	
	private ListView mLvCourses;
	private CourseListAdapter mCourseAdapter;
	private int mCurrentPage = 0;
	private boolean mIsRefresh = false;
	private String mUID;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		mUID = getIntent().getStringExtra(BUNDLE_KEY_UID);
		setContentView(R.layout.activity_course_list);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mLvCourses = (ListView) findViewById(R.id.lv_my_courses);
		mCourseAdapter = new CourseListAdapter(this, mLvCourses);
		
		loadMoreData(1);
	}

	@Override
	public Loader<HttpRequestResult> onCreateLoader(int loadId, Bundle bundle) {
		return new NetworkLoader(this, HttpMethod.Get, bundle);
	}

	@Override
	public void onLoadFinished(Loader<HttpRequestResult> loader,
			HttpRequestResult result) {
		if (result.getResponseCode() == 0) {
			CourseFactory courseFactory = new CourseFactory();
			List<Course> lstCourse = courseFactory.parseObjects(result
					.getStrResponseCon());
			if (mIsRefresh) {
				mCurrentPage = 0;
				mCourseAdapter.clear();
			}
			mCurrentPage ++;
			mCourseAdapter.addAll(lstCourse);
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
}
