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

        mCursor = this.getContentResolver().query(People.CONTENT_URI, null, null, null, null);
        startManagingCursor(mCursor);

        ListAdapter adapter = new SimpleCursorAdapter(
                 this,
                 android.R.layout.two_line_list_item,
                 mCursor,
                 new String[] {People.NAME, People.NUMBER},
                 new int[] {android.R.id.text1, android.R.id.text2});

         setListAdapter(adapter);
         
         mDBHelper = new DBHelper(getBaseContext());
         SQLiteDatabase db = mDBHelper.getWritableDatabase();
         
         ContentValues values = new ContentValues();
         values.put(User.Meta.COLUMN_USERNAME, "Bob");
         values.put(User.Meta.COLUMN_PASSWORD, "bob_pwd");
         long id = db.insert(User.Meta.TABLE_NAME, null, values);
         Log.d("UsersActivity", Long.toString(id));
    }
}
