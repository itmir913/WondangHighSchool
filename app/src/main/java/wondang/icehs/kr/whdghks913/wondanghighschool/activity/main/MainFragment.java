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
import wondang.icehs.kr.whdghks913.wondanghighschool.activity.timetable.TimeTableActivity;
import wondang.icehs.kr.whdghks913.wondanghighschool.tool.BapTool;
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
                            break;
                        case 1:
                            break;
                        case 2:
                            break;
                    }
                }
            }
        }));

        Bundle args = getArguments();
        int code = args.getInt("code");

        if (code == 1) {
            // SimpleView
            BapTool.todayBapData mBapData = BapTool.getTodayBap(getActivity());
            mAdapter.addItem(R.drawable.rice,
                    getString(R.string.title_activity_bap),
                    getString(R.string.message_activity_bap),
                    mBapData.title,
                    mBapData.info);

            TimeTableTool.todayTimeTableData mTimeTableData = TimeTableTool.getTodayTimeTable(getActivity());
            mAdapter.addItem(R.drawable.timetable,
                    getString(R.string.title_activity_time_table),
                    getString(R.string.message_activity_time_table),
                    mTimeTableData.title,
                    mTimeTableData.info);
        } else {
            // DetailedView
            mAdapter.addItem(R.drawable.ic_launcher_big,
                    "업데이트 할 예정",
                    "급식 위젯 + 시간표 위젯 한번에 보기\n" +
                            "급식 영양 성분 그래프로 보여주기\n" +
                            "오늘 급식 ★★★★☆ 평가하기\n" +
                            "방명록, 학생회에 건의하기\n" +
                            "버스 도착 정보 띄우기\n" +
                            "학교 일정 온라인화");
            mAdapter.addItem(R.drawable.calendar,
                    getString(R.string.title_activity_schedule),
                    getString(R.string.message_activity_schedule));
            mAdapter.addItem(R.drawable.clock,
                    getString(R.string.title_activity_today_list),
                    getString(R.string.message_activity_today_list));
            mAdapter.addItem(R.drawable.ic_launcher_big,
                    getString(R.string.title_activity_school_info),
                    getString(R.string.message_activity_school_info));
            mAdapter.addItem(R.drawable.ic_launcher_big,
                    getString(R.string.title_activity_user_info),
                    getString(R.string.message_activity_user_info));
        }

        return recyclerView;
    }
}
