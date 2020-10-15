package wondang.icehs.kr.whdghks913.wondanghighschool.activity.main;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import wondang.icehs.kr.whdghks913.wondanghighschool.R;
import wondang.icehs.kr.whdghks913.wondanghighschool.activity.settings.SettingsActivity;
import wondang.icehs.kr.whdghks913.wondanghighschool.tool.Preference;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.mViewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.mTabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);

        showUpdateNotification();
    }

    private void showUpdateNotification() {
        try {
            Preference mPref = new Preference(getApplicationContext());
            PackageManager packageManager = getPackageManager();
            PackageInfo info = packageManager.getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);

            int versionCode = info.versionCode;

            if (mPref.getInt("versionCode", 0) != versionCode) {
                mPref.putInt("versionCode", versionCode);

                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
                builder.setTitle(R.string.update_notification_title);
                builder.setMessage(R.string.update_notification_message);
                builder.setPositiveButton(android.R.string.ok, null);
                builder.show();
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter mAdapter = new Adapter(getSupportFragmentManager());

        mAdapter.addFragment(getString(R.string.activity_main_fragment_simple), MainFragment.getInstance(1));
        mAdapter.addFragment(getString(R.string.activity_main_fragment_detailed), MainFragment.getInstance(2));

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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
