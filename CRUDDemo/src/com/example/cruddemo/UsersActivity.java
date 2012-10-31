package com.example.cruddemo;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.cruddemo.models.DBHelper;
import com.example.cruddemo.models.User;

public class UsersActivity extends ListActivity {
	
	private Cursor mCursor;
	private DBHelper mDBHelper;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView text1 = (TextView)view.findViewById(android.R.id.text1);
				TextView text2 = (TextView)view.findViewById(android.R.id.text2);
				Log.d(UsersActivity.class.getName(), 
						"LongClick id: " + id + " " + text1.getText() + " " + text2.getText());
				return false;
			}
		});

        registerForContextMenu(getListView());
        
        mDBHelper = new DBHelper(getBaseContext());
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        
		String[] projection = { User.Meta._ID, User.Meta.COLUMN_USERNAME,
				User.Meta.COLUMN_PASSWORD };
		String order = User.Meta.COLUMN_USERNAME + " DESC";
		mCursor = db.query(User.Meta.TABLE_NAME, projection, null, null,
				null, null, order);
        startManagingCursor(mCursor);

        ListAdapter adapter = new SimpleCursorAdapter(
                 this,
                 android.R.layout.two_line_list_item,
                 mCursor,
                 new String[] { User.Meta.COLUMN_USERNAME,
         				User.Meta.COLUMN_PASSWORD },
                 new int[] {android.R.id.text1, android.R.id.text2});

         setListAdapter(adapter);
         db.close();
    }
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		TextView text1 = (TextView)v.findViewById(android.R.id.text1);
		TextView text2 = (TextView)v.findViewById(android.R.id.text2);
		Log.d(UsersActivity.class.getName(), 
				"Click id: " + id + " " + text1.getText() + " " + text2.getText());
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	                                ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.users_context_menu, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    switch (item.getItemId()) {
	        case R.id.delete_user:	        	
	            Log.d(UsersActivity.class.getName(), "delete user");
	    		String selection = User.Meta._ID + " = ?";
	    		String[] args = { String.valueOf(info.id) };
	    		SQLiteDatabase db = mDBHelper.getWritableDatabase();
	    		db.delete(User.Meta.TABLE_NAME, selection, args);
	    		db.close();
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}	
}
