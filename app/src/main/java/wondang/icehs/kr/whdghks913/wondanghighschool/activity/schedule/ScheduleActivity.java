package wondang.icehs.kr.whdghks913.wondanghighschool.activity.schedule;

import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import wondang.icehs.kr.whdghks913.wondanghighschool.R;

/**
 * Created by whdghks913 on 2015-12-10.
 */
public class ScheduleActivity extends AppCompatActivity {
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.mToolbar);
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

        viewPager = (ViewPager) findViewById(R.id.mViewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.mTabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);

        setCurrentItem();
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter mAdapter = new Adapter(getSupportFragmentManager());

        mAdapter.addFragment("3월", ScheduleFragment.getInstance(3));
        mAdapter.addFragment("4월", ScheduleFragment.getInstance(4));
        mAdapter.addFragment("5월", ScheduleFragment.getInstance(5));
        mAdapter.addFragment("6월", ScheduleFragment.getInstance(6));
        mAdapter.addFragment("7월", ScheduleFragment.getInstance(7));
        mAdapter.addFragment("8월", ScheduleFragment.getInstance(8));
        mAdapter.addFragment("9월", ScheduleFragment.getInstance(9));
        mAdapter.addFragment("10월", ScheduleFragment.getInstance(10));
        mAdapter.addFragment("11월", ScheduleFragment.getInstance(11));
        mAdapter.addFragment("12월", ScheduleFragment.getInstance(12));
        mAdapter.addFragment("2016년 1월", ScheduleFragment.getInstance(1));
        mAdapter.addFragment("2016년 2월", ScheduleFragment.getInstance(2));

        viewPager.setAdapter(mAdapter);
    }

    private void setCurrentItem() {
        int month = Calendar.getInstance().get(Calendar.MONTH);

        if (month >= 2) month -= 2;
        else month += 9;

        viewPager.setCurrentItem(month);
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

}
