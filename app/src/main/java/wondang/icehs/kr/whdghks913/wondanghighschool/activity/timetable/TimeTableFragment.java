package wondang.icehs.kr.whdghks913.wondanghighschool.activity.timetable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

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
        View view = inflater.inflate(R.layout.row_timetable_listview, container, false);
        ListView listView = (ListView) view.findViewById(R.id.mListView);
        TextView mGradeClass = (TextView) view.findViewById(R.id.mGradeClass);

        TimeTableAdapter mAdapter = new TimeTableAdapter(getActivity());
        listView.setAdapter(mAdapter);

        Bundle args = getArguments();
        int mGrade = args.getInt("mGrade");
        int mClass = args.getInt("mClass");
        int dayOfWeek = args.getInt("dayOfWeek");

        TimeTableTool.timeTableData mData = TimeTableTool.getTimeTableData(mGrade, mClass, dayOfWeek);

        if (mGrade == -1 || mClass == -1) {
            mGradeClass.setText(R.string.no_setting_my_grade);
        } else {
            mGradeClass.setText(String.format(getString(R.string.timetable_title), mGrade, mClass));
        }

        for (int position = 0; position < 7; position++) {
            mAdapter.addItem(position + 1, mData.subject[position], mData.room[position]);
        }

        return view;
    }
}
