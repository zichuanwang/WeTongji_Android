package com.wetongji_android.util.data;

import java.util.List;
import java.util.concurrent.Callable;

import com.j256.ormlite.dao.Dao;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class DbSaver<T, ID> extends AsyncTaskLoader<Void> {

	private Dao<T, ID> mDao = null;
    private List<T> mData = null;

    public DbSaver(Context context, Dao<T, ID> dao, List<T> data)
    {
        super(context);
        mDao = dao;
        mData=data;
    }

	@Override
	public Void loadInBackground() {
		try {
			mDao.callBatchTasks(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					for(T t:mData)
						mDao.createOrUpdate(t);
					return null;
				}

			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	protected void onStartLoading() {
		super.onStartLoading();
		forceLoad();
	}

	@Override
	protected void onStopLoading() {
		super.onStopLoading();
		cancelLoad();
	}
	
}
