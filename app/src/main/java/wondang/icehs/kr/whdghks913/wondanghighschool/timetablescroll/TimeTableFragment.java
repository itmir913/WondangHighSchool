package wondang.icehs.kr.whdghks913.wondanghighschool.timetablescroll;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import wondang.icehs.kr.whdghks913.wondanghighschool.R;
import wondang.icehs.kr.whdghks913.wondanghighschool.tool.Database;
import wondang.icehs.kr.whdghks913.wondanghighschool.tool.TimeTableTool;
import wondang.icehs.kr.whdghks913.wondanghighschool.tool.copyAssets;

public class TimeTableFragment extends Fragment {
    ListView mListView;
    TimeTableScrollAdapter mAdapter;

    Database mDatabase;

    TextView mTimeName;

    /**
     * Returns a new instance of this fragment for the given section number.
     */
    public static TimeTableFragment newInstance(int DayOfWeek, int myGrade, int myClass) {
        TimeTableFragment mFragment = new TimeTableFragment();

        Bundle args = new Bundle();
        args.putInt("DayOfWeek", DayOfWeek);
        args.putInt("myGrade", myGrade);
        args.putInt("myClass", myClass);
        mFragment.setArguments(args);

        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_time_table, container, false);

        Bundle args = getArguments();
        int DayOfWeek = args.getInt("DayOfWeek");
        int mGrade = args.getInt("myGrade");
        int mClass = args.getInt("myClass");

        mListView = (ListView) rootView.findViewById(R.id.mListView);
        mAdapter = new TimeTableScrollAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        mListView.setDivider(new ColorDrawable(Color.TRANSPARENT));

        mTimeName = (TextView) rootView.findViewById(R.id.mTimeName);

        showTimeTable(DayOfWeek, mGrade, mClass);

        return rootView;
    }

    private void showTimeTable(int DayOfWeek, int mGrade, int mClass) {

        if (mGrade == -1 || mClass == -1) {
            mTimeName.setText(String.format(getString(R.string.no_setting_mygrade), mGrade, mClass));
            return;
        }

        mAdapter.clearData();
        mTimeName.setText(String.format(getString(R.string.timetable_title), mGrade, mClass));
//        mDayOfTheWeek.setText(TimeTableTool.mDisplayName[DayOfWeek]);

//        if (true) {
//            mAdapter.addItem(Integer.toString(1), "DB 준비중", "2015년 DB를 준비중입니다");
//            mAdapter.notifyDataSetChanged();
//            return;
//        }

        try {
            if (TimeTableTool.getDBUpdate(getActivity())) {
                copyAssets copyAssets = new copyAssets();
                copyAssets.assetsFileCopy(getActivity(), TimeTableTool.mFilePath, TimeTableTool.TimeTableDBName);
            }

            mDatabase = new Database();
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
            mCursor.moveToPosition((DayOfWeek * 7) + 1);

            for (int period = 1; period <= 7; period++) {
                String mSubject;
                String mRoom = null;

                /**
                 * | | | |
                 * 0 1 2 3
                 */
                if (mGrade == 1) {
                    mSubject = mCursor.getString((mClass * 2) - 2);
//                    mRoom = mCursor.getString((mClass * 2) - 1);
                } else if (mGrade == 2) {
                    mSubject = mCursor.getString(18 + (mClass * 2));
//                    mRoom = mCursor.getString(19 + (mClass * 2));
                } else {
                    mSubject = mCursor.getString(39 + mClass);
//                    mRoom = null;
                }

                if (mSubject != null && !mSubject.isEmpty()
                        && mSubject.indexOf("\n") != -1)
                    mSubject = mSubject.replace("\n", " (") + ")";

                mAdapter.addItem(Integer.toString(period), mSubject, mRoom);

                mCursor.moveToNext();
            }

            mAdapter.notifyDataSetChanged();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
