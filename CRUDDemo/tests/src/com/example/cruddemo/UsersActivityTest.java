package com.example.cruddemo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.example.cruddemo.UsersActivity;
import com.example.cruddemo.models.DBHelper;
import com.example.cruddemo.models.User;

/**
 * This is a simple framework for a test of an Application. See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more
 * information on how to write and extend Application tests.
 * <p/>
 * To run this test, you can type: adb shell am instrument -w \ -e class
 * com.example.cruddemo.UsersActivityTest \
 * com.example.cruddemo.tests/android.test.InstrumentationTestRunner
 */
public class UsersActivityTest extends
		ActivityInstrumentationTestCase2<UsersActivity> {
	private Cursor mCursor;
	private DBHelper mDBHelper;
	private SQLiteDatabase mDB;

	public UsersActivityTest() {
		super("com.example.cruddemo", UsersActivity.class);
	}

	public void setUp() {
		mDBHelper = new DBHelper(getActivity());
		mDB = mDBHelper.getWritableDatabase();
	}

	public void test_create() {
		ContentValues values = new ContentValues();
		values.put(User.Meta.COLUMN_USERNAME, "Bob");
		values.put(User.Meta.COLUMN_PASSWORD, "bob_pwd");
		long id = mDB.insert(User.Meta.TABLE_NAME, null, values);
		assertTrue(id > 0);
		Log.d("DBHelperTest.test_create", Long.toString(id));
	}
}
