package wondang.icehs.kr.whdghks913.wondanghighschool.activity.timetable;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import itmir.tistory.com.spreadsheets.GoogleSheetTask;
import wondang.icehs.kr.whdghks913.wondanghighschool.R;
import wondang.icehs.kr.whdghks913.wondanghighschool.tool.Database;
import wondang.icehs.kr.whdghks913.wondanghighschool.tool.Preference;
import wondang.icehs.kr.whdghks913.wondanghighschool.tool.TimeTableTool;
import wondang.icehs.kr.whdghks913.wondanghighschool.tool.Tools;

public class TimeTableActivity extends AppCompatActivity {
    Preference mPref;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        mPref = new Preference(getApplicationContext());
        int mGrade = mPref.getInt("myGrade", -1);
        int mClass = mPref.getInt("myClass", -1);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        if ((mGrade != -1) && (mClass != -1)) {
            mToolbar.setTitle(String.format(getString(R.string.timetable_title), mGrade, mClass));
        }
        setSupportActionBar(mToolbar);

        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        if ((mGrade == -1) || (mClass == -1)) {
            resetGrade();
            return;
        }

        if (!TimeTableTool.fileExists()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
            builder.setTitle(R.string.no_time_table_db_title);
            builder.setMessage(R.string.no_time_table_db_message);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    downloadingDB();
                }
            });
            builder.setNegativeButton(android.R.string.cancel, null);
            builder.show();
            return;
        }

        viewPager = (ViewPager) findViewById(R.id.mViewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.mTabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);

        FloatingActionButton mFab = (FloatingActionButton) findViewById(R.id.mFab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadingDB();
            }
        });

        setCurrentItem();
    }

    private void setCurrentItem() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        if (dayOfWeek > 1 && dayOfWeek < 7) {
            viewPager.setCurrentItem(dayOfWeek - 2);
        } else {
            viewPager.setCurrentItem(0);
        }
    }

    private void resetGrade() {
        mPref.remove("myGrade");

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(R.string.action_setting_mygrade);
        builder.setItems(R.array.myGrade, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPref.putInt("myGrade", which + 1);
                resetClass();
            }
        });
        builder.show();
    }

    private void resetClass() {
        mPref.remove("myClass");

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(R.string.action_setting_myclass);
        builder.setItems(R.array.myClass, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPref.putInt("myClass", which + 1);
                Toast.makeText(getApplicationContext(), "다시 로딩됩니다", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), TimeTableActivity.class));
                finish();
            }
        });
        builder.show();
    }

    public void downloadingDB() {
        if (Tools.isOnline(getApplicationContext())) {
            if (Tools.isWifi(getApplicationContext())) {
                downloadStart();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
                builder.setTitle(R.string.no_wifi_title);
                builder.setMessage(R.string.no_wifi_msg);
                builder.setNegativeButton(android.R.string.cancel, null);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        downloadStart();
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatErrorAlertDialogStyle);
            builder.setTitle(R.string.no_network_title);
            builder.setMessage(R.string.no_network_msg);
            builder.setPositiveButton(android.R.string.ok, null);
            builder.show();
        }
    }

    private void downloadStart() {
        new File(TimeTableTool.mFilePath + TimeTableTool.TimeTableDBName).delete();
        DBDownloadTask mTask = new DBDownloadTask();
        mTask.execute(TimeTableTool.mGoogleSpreadSheetUrl);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter mAdapter = new Adapter(getSupportFragmentManager());

        for (int dayOfWeek = 0; dayOfWeek < 5; dayOfWeek++) {
            mAdapter.addFragment(TimeTableTool.mDisplayName[dayOfWeek], TimeTableFragment.getInstance(mPref.getInt("myGrade", -1), mPref.getInt("myClass", -1), dayOfWeek));
        }

        viewPager.setAdapter(mAdapter);
    }

    class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        public void addFragment(String mTitle, Fragment mFragment) {
            mFragments.add(mFragment);
            mFragmentTitles.add(mTitle);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    class DBDownloadTask extends GoogleSheetTask {
        private ProgressDialog mDialog;
        private Database mDatabase;
        private String[] columnFirstRow;

        @Override
        public void onPreDownload() {
            mDialog = new ProgressDialog(TimeTableActivity.this);
            mDialog.setIndeterminate(true);
            mDialog.setMessage(getString(R.string.loading_title));
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();

            mDatabase = new Database();
        }

        @Override
        public void onFinish(long result) {
            startActivity(new Intent(TimeTableActivity.this, TimeTableActivity.class));
            finish();

            if (mDialog != null) {
                mDialog.dismiss();
                mDialog = null;
            }

            if (mDatabase != null)
                mDatabase.release();
        }

        @Override
        public void onRow(int startRowNumber, int position, String[] row) {
            if (startRowNumber == position) {
                columnFirstRow = row;

                StringBuilder Column = new StringBuilder();

                for (String column : row) {
                    Column.append(column);
                    Column.append(" text, ");
                }

                mDatabase.openOrCreateDatabase(TimeTableTool.mFilePath, TimeTableTool.TimeTableDBName, TimeTableTool.tableName, Column.substring(0, Column.length() - 2));
            } else {
                int length = row.length;
                for (int i = 0; i < length; i++) {
                    mDatabase.addData(columnFirstRow[i], row[i]);
                }
                mDatabase.commit(TimeTableTool.tableName);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_time_table, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reset_mygrade) {
            resetGrade();
            return true;

        } else if (id == R.id.action_share_timetable) {
            shareTimeTable();
            return true;
        } else if (id == R.id.action_download_db) {
            downloadingDB();
        }

        return super.onOptionsItemSelected(item);
    }

    private void shareTimeTable() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(R.string.action_share_day);
        builder.setItems(R.array.myWeek, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                shareTimeTable(which, mPref.getInt("myGrade", -1), mPref.getInt("myClass", -1));
            }
        });
        builder.show();
    }

    private void shareTimeTable(int position, int mGrade, int mClass) {
        try {
            String mText = "";

            TimeTableTool.timeTableData mData = TimeTableTool.getTimeTableData(mGrade, mClass, position + 2);

            String[] subject = mData.subject;
            String[] room = mData.room;

            for (int period = 0; period < 7; period++) {
                mText += "\n" + (period + 1) + "교시 : " + subject[period] + "(" + room[period] + ")";
            }

            String title = getString(R.string.action_share_timetable);
            Intent msg = new Intent(Intent.ACTION_SEND);
            msg.addCategory(Intent.CATEGORY_DEFAULT);
            msg.putExtra(Intent.EXTRA_TITLE, title);
            msg.putExtra(Intent.EXTRA_TEXT, String.format(
                    getString(R.string.action_share_timetable_msg),
                    TimeTableTool.mDisplayName[position], mText));
            msg.setType("text/plain");
            startActivity(Intent.createChooser(msg, title));

        } catch (Exception ex) {
            ex.printStackTrace();

            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatErrorAlertDialogStyle);
            builder.setTitle(R.string.I_do_not_know_the_error_title);
            builder.setMessage(R.string.I_do_not_know_the_error_message);
            builder.setPositiveButton(android.R.string.ok, null);
            builder.show();
        }
    }
}