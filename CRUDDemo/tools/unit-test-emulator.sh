cd tests
ant clean
ant debug
adb -e install -r bin/CRUDDemoTest-debug.apk
adb -e shell am instrument -w com.example.cruddemo.tests/android.test.InstrumentationTestRunner
cd ..