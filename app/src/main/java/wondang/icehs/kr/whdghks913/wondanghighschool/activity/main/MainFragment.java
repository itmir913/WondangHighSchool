package wondang.icehs.kr.whdghks913.wondanghighschool.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wondang.icehs.kr.whdghks913.wondanghighschool.R;
import wondang.icehs.kr.whdghks913.wondanghighschool.activity.bap.BapActivity;
import wondang.icehs.kr.whdghks913.wondanghighschool.activity.notice.NoticeActivity;
import wondang.icehs.kr.whdghks913.wondanghighschool.activity.schedule.ScheduleActivity;
import wondang.icehs.kr.whdghks913.wondanghighschool.activity.timetable.TimeTableActivity;
import wondang.icehs.kr.whdghks913.wondanghighschool.tool.BapTool;
import wondang.icehs.kr.whdghks913.wondanghighschool.tool.Preference;
import wondang.icehs.kr.whdghks913.wondanghighschool.tool.RecyclerItemClickListener;
import wondang.icehs.kr.whdghks913.wondanghighschool.tool.TimeTableTool;

/**
 * Created by whdghks913 on 2015-11-30.
 */
public class MainFragment extends Fragment {

    public static Fragment getInstance(int code) {
        MainFragment mFragment = new MainFragment();

        Bundle args = new Bundle();
        args.putInt("code", code);
        mFragment.setArguments(args);

        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recyclerview, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        final MainAdapter mAdapter = new MainAdapter(getActivity());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View mView, int position) {
                boolean isSimple = mAdapter.getItemData(position).isSimple;

                if (isSimple) {
                    switch (position) {
                        case 0:
                            startActivity(new Intent(getActivity(), BapActivity.class));
                            break;
                        case 1:
                            startActivity(new Intent(getActivity(), TimeTableActivity.class));
                            break;
                    }
                } else {
                    switch (position) {
                        case 0:
                            startActivity(new Intent(getActivity(), NoticeActivity.class));
                            break;
                        case 1:
                            startActivity(new Intent(getActivity(), ScheduleActivity.class));
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                    }
                }
            }
        }));

        Bundle args = getArguments();
        int code = args.getInt("code");
        Preference mPref = new Preference(getActivity());

        if (code == 1) {
            // SimpleView
            if (mPref.getBoolean("simpleShowBap", true)) {
                BapTool.todayBapData mBapData = BapTool.getTodayBap(getActivity());
                mAdapter.addItem(R.drawable.rice,
                        getString(R.string.title_activity_bap),
                        getString(R.string.message_activity_bap),
                        mBapData.title,
                        mBapData.info);
            } else {
                mAdapter.addItem(R.drawable.rice,
                        getString(R.string.title_activity_bap),
                        getString(R.string.message_activity_bap), true);
            }

            if (mPref.getBoolean("simpleShowTimeTable", true)) {
                TimeTableTool.todayTimeTableData mTimeTableData = TimeTableTool.getTodayTimeTable(getActivity());
                mAdapter.addItem(R.drawable.timetable,
                        getString(R.string.title_activity_time_table),
                        getString(R.string.message_activity_time_table),
                        mTimeTableData.title,
                        mTimeTableData.info);
            } else {
                mAdapter.addItem(R.drawable.timetable,
                        getString(R.string.title_activity_time_table),
                        getString(R.string.message_activity_time_table), true);
            }
        } else {
            // DetailedView
            mAdapter.addItem(R.drawable.notice,
                    getString(R.string.title_activity_notice),
                    getString(R.string.message_activity_notice));
            mAdapter.addItem(R.drawable.calendar,
                    getString(R.string.title_activity_schedule),
                    getString(R.string.message_activity_schedule));
//            mAdapter.addItem(R.drawable.ic_launcher_big,
//                    getString(R.string.title_activity_school_info),
//                    getString(R.string.message_activity_school_info));
        }

        return recyclerView;
    }
}
