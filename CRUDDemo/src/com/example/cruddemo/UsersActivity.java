package com.example.cruddemo;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

public class UsersActivity extends ListActivity {
	
	private Cursor mCursor;
	
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
    }
}
