package wondang.icehs.kr.whdghks913.wondanghighschool.timetablescroll;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.mrengineer13.snackbar.SnackBar;

import java.util.Calendar;

import uk.me.lewisdeane.ldialogs.CustomListDialog;
import wondang.icehs.kr.whdghks913.wondanghighschool.R;
import wondang.icehs.kr.whdghks913.wondanghighschool.tool.Database;
import wondang.icehs.kr.whdghks913.wondanghighschool.tool.Preference;
import wondang.icehs.kr.whdghks913.wondanghighschool.tool.TimeTableTool;

public class TimeTableScrollActivity extends ActionBarActivity {

    ViewPager mViewPager;
    SectionsPagerAdapter mSectionsPagerAdapter;

    Toolbar mToolbar;

    Preference mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager);

        mPref = new Preference(getApplicationContext());

        mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.flat_melon_yellow));
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(Color.WHITE);

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

        if (mPref.getInt("myGrade", -1) == -1) {
            resetGrade();
        }

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        setCurrentItem();
    }

    private void setCurrentItem() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        if (dayOfWeek > 1 && dayOfWeek < 7) {
            mViewPager.setCurrentItem(dayOfWeek - 2);
        } else {
            mViewPager.setCurrentItem(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_time_table, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_reset_mygrade) {
            resetGrade();
            return true;

        } else if (id == R.id.action_share_timetable) {
            shareTimeTable();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void resetGrade() {
        mPref.remove("myGrade");

        CustomListDialog.Builder builder = new CustomListDialog.Builder(this, R.string.action_setting_mygrade, R.array.myGrade);

        CustomListDialog customListDialog = builder.build();
        customListDialog.show();
        customListDialog.setListClickListener(new CustomListDialog.ListClickListener() {
            @Override
            public void onListItemSelected(int position, String[] items, String item) {
                mPref.putInt("myGrade", position + 1);
                resetClass();
            }
        });
    }

    private void resetClass() {
        mPref.remove("myClass");

        CustomListDialog.Builder builder = new CustomListDialog.Builder(this, R.string.action_setting_myclass, R.array.myClass);

        CustomListDialog customListDialog = builder.build();
        customListDialog.setListClickListener(new CustomListDialog.ListClickListener() {
            @Override
            public void onListItemSelected(int position, String[] items, String item) {
                mPref.putInt("myClass", position + 1);
                Toast.makeText(getApplicationContext(), "다시 로딩됩니다", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), TimeTableScrollActivity.class));
                finish();
            }
        });
        customListDialog.show();
    }

    private void shareTimeTable() {
        CustomListDialog.Builder builder = new CustomListDialog.Builder(this, R.string.action_share_day, R.array.myWeek);

        CustomListDialog customListDialog = builder.build();
        customListDialog.setListClickListener(new CustomListDialog.ListClickListener() {
            @Override
            public void onListItemSelected(int position, String[] items, String item) {
                shareTimeTable(position, mPref.getInt("myGrade", -1), mPref.getInt("myClass", -1));
            }
        });
        customListDialog.show();
    }

    private void shareTimeTable(int position, int mGrade, int mClass) {

        try {
            String mText = "";

            Database mDatabase = new Database();
            mDatabase.openOrCreateDatabase(TimeTableTool.mFilePath, TimeTableTool.TimeTableDBName, TimeTableTool.tableName, "");

            Cursor mCursor = mDatabase.getData(TimeTableTool.tableName, "*");

            /**
             * Move to Row
             * ---- moveToFirst
             * ---- moveToNext
             * ---- moveToPosition
             * ---- moveToLast
             *
             * Mon : DayOfWeek : 0
             * Tus : DayOfWeek : 1
             * ...
             * Fri : DayOfWeek : 4
             */
            mCursor.moveToPosition((position * 7) + 1);

            for (int period = 1; period <= 7; period++) {
                String mSubject;
                String mRoom;

                /**
                 * | | | |
                 * 0 1 2 3
                 */
                if (mGrade == 1) {
                    mSubject = mCursor.getString((mClass * 2) - 2);
                    mRoom = mCursor.getString((mClass * 2) - 1);
                } else if (mGrade == 2) {
                    mSubject = mCursor.getString(18 + (mClass * 2));
                    mRoom = mCursor.getString(19 + (mClass * 2));
                } else {
                    mSubject = mCursor.getString(39 + mClass);
                    mRoom = "교실";
                }

                if (mSubject != null && !mSubject.isEmpty()
                        && mSubject.indexOf("\n") != -1) {
                    mSubject = mSubject.replace("\n", " (") + ")";
                }

                mText += "\n" + period + "교시 : " + mSubject + "(" + mRoom + ")";
//                mText += "\n" + period + "교시 : " + mSubject;

                mCursor.moveToNext();
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

            SnackBar.Builder mSnackBar = new SnackBar.Builder(this);
            mSnackBar.withMessage(getString(R.string.share_timetable_error));
            mSnackBar.withStyle(SnackBar.Style.INFO);
            mSnackBar.withActionMessage(getString(android.R.string.ok));
            mSnackBar.show();
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return TimeTableFragment.newInstance(position, mPref.getInt("myGrade", -1), mPref.getInt("myClass", -1));
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TimeTableTool.mDisplayName[position];
        }
    }

}
