package com.wetongji_android.util.data;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class DbListSaver<T, ID> extends AsyncTaskLoader<Void> implements Callable<Void>{

	private Dao<T, ID> mDao = null;
    private List<T> mData = null;
    private Context context;
    private DbHelper dbHelper;
    private Class<T> clazz;

    public DbListSaver(Context context, Class<T> clazz, List<T> data)
    {
        super(context);
        this.context=context;
        this.clazz=clazz;
        this.mData=data;
    }

	@Override
	public Void loadInBackground() {
		dbHelper=OpenHelperManager.getHelper(context, DbHelper.class);
		try {
			mDao=dbHelper.getDao(clazz);
			mDao.callBatchTasks(this);
		} catch (SQLException e) {
			e.printStackTrace();
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
	
	/**
     * Handles a request to cancel a load.
     */
    @Override
    public void onCanceled(Void data)
    {
        super.onCanceled(data);

        // At this point we can release the resources associated with 'apps'
        // if needed.
        onReleaseResources(data);
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
        onReleaseResources(null);
    }

    /**
     * Helper function to take care of releasing resources associated with an
     * actively loaded data set.
     */
    protected void onReleaseResources(Void data)
    {
        // For a simple List<> there is nothing to do. For something
        // like a Cursor, we would close it here.
    	OpenHelperManager.releaseHelper();
    }
	@Override
	public Void call() throws Exception {
		for(T t:mData){
			mDao.createOrUpdate(t);
		}
		return null;
	}
	
}
