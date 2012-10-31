package com.example.cruddemo;

import android.app.ListActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

import com.example.cruddemo.models.DBHelper;
import com.example.cruddemo.models.User;

public class UsersActivity extends ListActivity {
	
	private Cursor mCursor;
	private DBHelper mDBHelper;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

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
}
