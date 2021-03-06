/*
 * project	SeattleCoffeeLocator
 * 
 * package	com.j2w3.rbarnes.seattlecoffeelocator
 * 
 * @author	Ronaldo Barnes
 * 
 * date		Mar 21, 2013
 */
package com.j2w3.rbarnes.seattlecoffeelocator;

import com.rbarnes.other.LocationContentProvider;
import com.rbarnes.other.LocationDB;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class CoffeeListFragment extends ListFragment implements
        LoaderManager.LoaderCallbacks<Cursor> {
    private OnLocationSelectedListener locationSelectedListener;
    private static final int LOCATION_LIST_LOADER = 1;

    private SimpleCursorAdapter adapter;

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String projection[] = { LocationDB.COL_PHONE };
        Cursor locationCursor = getActivity().getContentResolver().query(
                Uri.withAppendedPath(LocationContentProvider.CONTENT_URI,
                        String.valueOf(id)), projection, null, null, null);
        if (locationCursor.moveToFirst()) {
        	String locationNumber = locationCursor.getString(0);
        	
        	Log.i("ITEM",locationNumber );
        	
            locationSelectedListener.onlocationSelected(locationNumber);
        }
        locationCursor.close();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] uiBindFrom = { LocationDB.COL_TITLE };
        int[] uiBindTo = { R.id.title };

        getLoaderManager().initLoader(LOCATION_LIST_LOADER, null, this);

        adapter = new SimpleCursorAdapter(
                getActivity().getApplicationContext(), R.layout.list_item,
                null, uiBindFrom, uiBindTo,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        setListAdapter(adapter);
        setHasOptionsMenu(true);
    }

    public interface OnLocationSelectedListener {
        public void onlocationSelected(String number);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            locationSelectedListener = (OnLocationSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnLocationSelectedListener");
        }
    }

    

    // LoaderManager.LoaderCallbacks<Cursor> methods

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { LocationDB.ID, LocationDB.COL_TITLE };

        CursorLoader cursorLoader = new CursorLoader(getActivity(),
        		LocationContentProvider.CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
