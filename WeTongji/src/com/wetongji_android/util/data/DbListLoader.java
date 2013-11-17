package com.wetongji_android.util.data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.wetongji_android.util.common.WTApplication;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class DbListLoader<T, ID> extends AsyncTaskLoader<List<T>> {

	protected Dao<T, ID> mDao = null;
	protected DbHelper dbHelper=null;
    private List<T> mData = null;

    public DbListLoader(Context context, Class<T> clazz)
    {
        super(context);
        dbHelper=WTApplication.getInstance().getDbHelper();
        try {
			mDao=dbHelper.getDao(clazz);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    @Override
    public List<T> loadInBackground()
    {
        List<T> result=new ArrayList<T>();
    	try {
			result = mDao.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
    }

    @Override
    public void deliverResult(List<T> datas)
    {
        if (isReset())
        {
            // An async query came in while the loader is stopped. We
            // don't need the result.
            if (datas != null)
            {
                onReleaseResources(datas);
            }
        }

        List<T> oldDatas = mData;
        mData = datas;

        if (isStarted())
        {
            // If the Loader is currently started, we can immediately
            // deliver its results.
            super.deliverResult(datas);
        }

        if (oldDatas != null && !oldDatas.isEmpty())
        {
            onReleaseResources(oldDatas);
        }
    }

    /**
     * Handles a request to start the Loader.
     */
    @Override
    protected void onStartLoading()
    {
        forceLoad();
    }

    /**
     * Handles a request to stop the Loader.
     */
    @Override
    protected void onStopLoading()
    {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    /**
     * Handles a request to cancel a load.
     */
    @Override
    public void onCanceled(List<T> datas)
    {
        super.onCanceled(datas);

        // At this point we can release the resources associated with 'apps'
        // if needed.
        onReleaseResources(datas);
    }

    /**
     * Handles a request to completely reset the Loader.
     */
    @Override
    protected void onReset()
    {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        // At this point we can release the resources associated with 'apps'
        // if needed.
        if (mData != null)
        {
            onReleaseResources(mData);
            mData = null;
        }
    }

    /**
     * Helper function to take care of releasing resources associated with an
     * actively loaded data set.
     */
    protected void onReleaseResources(List<T> datas)
    {
        // For a simple List<> there is nothing to do. For something
        // like a Cursor, we would close it here.
    }
    
}
