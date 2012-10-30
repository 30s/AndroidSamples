package com.example.cruddemo;

import android.test.ActivityInstrumentationTestCase2;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.example.cruddemo.UsersActivityTest \
 * com.example.cruddemo.tests/android.test.InstrumentationTestRunner
 */
public class UsersActivityTest extends ActivityInstrumentationTestCase2<UsersActivity> {

    public UsersActivityTest() {
        super("com.example.cruddemo", UsersActivity.class);
    }

    public void test_1() {
        assertTrue(false);
    }
}
