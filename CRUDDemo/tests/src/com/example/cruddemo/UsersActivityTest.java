package com.example.cruddemo;

import android.annotation.TargetApi;
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
	private DBHelper mDBHelper;
	private SQLiteDatabase mDB;
	private UsersActivity mActivity;

	@TargetApi(3)
	public UsersActivityTest() {
		super("com.example.cruddemo", UsersActivity.class);
	}

	@TargetApi(3)
	public void setUp() throws Exception {
		super.setUp();
		mDBHelper = new DBHelper(getActivity());
		mDB = mDBHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(User.Meta.COLUMN_USERNAME, "Bob");
		values.put(User.Meta.COLUMN_PASSWORD, "bob_pwd");
		long id = mDB.insert(User.Meta.TABLE_NAME, null, values);
		Log.d("setUp", Long.toString(id));
	}

	public void test_create() {
		ContentValues values = new ContentValues();
		values.put(User.Meta.COLUMN_USERNAME, "Bob");
		values.put(User.Meta.COLUMN_PASSWORD, "bob_pwd");
		long id = mDB.insert(User.Meta.TABLE_NAME, null, values);
		assertTrue(id > 0);
		Log.d("DBHelperTest.test_create", Long.toString(id));
	}

	public void test_query() {
		String[] projection = { User.Meta._ID, User.Meta.COLUMN_USERNAME,
				User.Meta.COLUMN_PASSWORD, };
		String order = User.Meta.COLUMN_USERNAME + " DESC";
		Cursor cursor = mDB.query(User.Meta.TABLE_NAME, projection, null, null,
				null, null, order);
		assertTrue(cursor.getCount() > 0);
		cursor.close();
	}

	public void test_delete() {
		String selection = User.Meta._ID + " = ?";
		String[] args = { Integer.toString(1) };
		mDB.delete(User.Meta.TABLE_NAME, selection, args);
	}

	public void test_update() {
		ContentValues values = new ContentValues();
		values.put(User.Meta.COLUMN_USERNAME, "Bob");
		values.put(User.Meta.COLUMN_PASSWORD, "bob_pwd_1");

		String selection = User.Meta.COLUMN_PASSWORD + " = ?";
		String[] args = { "bob_pwd" };

		int count = mDB.update(User.Meta.TABLE_NAME, values, selection, args);
		assertTrue(count >= 1);
	}
}
