package wondang.icehs.kr.whdghks913.wondanghighschool.activity.timetable;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wondang.icehs.kr.whdghks913.wondanghighschool.R;
import wondang.icehs.kr.whdghks913.wondanghighschool.tool.TimeTableTool;

/**
 * Created by whdghks913 on 2015-12-02.
 */
public class TimeTableFragment extends Fragment {

    public static Fragment getInstance(int mGrade, int mClass, int dayOfWeek) {
        TimeTableFragment mFragment = new TimeTableFragment();

        Bundle args = new Bundle();
        args.putInt("mGrade", mGrade);
        args.putInt("mClass", mClass);
        args.putInt("dayOfWeek", dayOfWeek);
        mFragment.setArguments(args);

        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recyclerview, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        final TimeTableAdapter mAdapter = new TimeTableAdapter(getActivity());
        recyclerView.setAdapter(mAdapter);

        Bundle args = getArguments();
        int mGrade = args.getInt("mGrade");
        int mClass = args.getInt("mClass");
        int dayOfWeek = args.getInt("dayOfWeek");

        TimeTableTool.timeTableData mData = TimeTableTool.getTimeTableData(mGrade, mClass, dayOfWeek);

        for (int position = 0; position < 7; position++) {
            mAdapter.addItem(position + 1, mData.subject[position], mData.room[position]);
        }

        return recyclerView;
    }
}
