package wondang.icehs.kr.whdghks913.wondanghighschool.tool;

import android.content.Context;
import android.database.Cursor;

import java.io.File;
import java.util.Calendar;

import wondang.icehs.kr.whdghks913.wondanghighschool.R;

/**
 * Created by 종환 on 2015-02-17.
 */
public class TimeTableTool {
    public static final String TimeTableDBName = "WondangTimeTable.db";
    public static final String tableName = "WondangTimeTable";

    //    public final static String mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WondangHS/";
    public final static String mFilePath = "/data/data/wondang.icehs.kr.whdghks913.wondanghighschool/databases/";
    public final static int dbVersion = 6;

    public final static String[] mDisplayName = {"월요일", "화요일", "수요일", "목요일", "금요일"};

    public static boolean getDBUpdate(Context mContext) {
        Preference mPref = new Preference(mContext);

        boolean fileInfo = !(new File(TimeTableTool.mFilePath + TimeTableTool.TimeTableDBName).exists());
        boolean versionInfo = mPref.getInt("TimeTableDBVersion", 0) != TimeTableTool.dbVersion;

        if (fileInfo || versionInfo) {
            mPref.putInt("TimeTableDBVersion", TimeTableTool.dbVersion);
            return true;
        }
        return false;
    }

    public static todayTimeTableData getTodayTimeTable(Context mContext) {
        Preference mPref = new Preference(mContext);
        todayTimeTableData mData = new todayTimeTableData();

        Calendar mCalendar = Calendar.getInstance();
        int DayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK);

        int mGrade = mPref.getInt("myGrade", -1);
        int mClass = mPref.getInt("myClass", -1);

        mData.title = String.format(mContext.getString(R.string.today_timetable), TimeTableTool.mDisplayName[DayOfWeek]);

        if (mGrade == -1 || mClass == -1) {
            mData.info = mContext.getString(R.string.no_setting_my_grade);
            return mData;
        }

        if (DayOfWeek > 1 && DayOfWeek < 7) {
            DayOfWeek -= 2;
        } else {
            mData.info = mContext.getString(R.string.not_go_to_school);
            return mData;
        }

        boolean mFileExists = new File(TimeTableTool.mFilePath + TimeTableTool.TimeTableDBName).exists();
        if (!mFileExists) {
            mData.info = mContext.getString(R.string.not_exists_data);
            return mData;
        }

        String mTimeTable = "";

        Database mDatabase = new Database();
        mDatabase.openDatabase(TimeTableTool.mFilePath, TimeTableTool.TimeTableDBName);

        Cursor mCursor = mDatabase.getData(TimeTableTool.tableName, "*");

        mCursor.moveToPosition((DayOfWeek * 7) + 1);

        for (int period = 1; period <= 7; period++) {
            String mSubject;

            if (mGrade == 1) {
                mSubject = mCursor.getString((mClass * 2) - 2);
//                mRoom = mCursor.getString((mClass * 2) - 1);
            } else if (mGrade == 2) {
                mSubject = mCursor.getString(18 + (mClass * 2));
//                mRoom = mCursor.getString(19 + (mClass * 2));
            } else {
                mSubject = mCursor.getString(39 + mClass);
//                mRoom = null;
            }

            if (mSubject != null && !mSubject.isEmpty()
                    && mSubject.contains("\n"))
                mSubject = mSubject.replace("\n", " (") + ")";

            String tmp = Integer.toString(period) + ". " + mSubject;
            mTimeTable += tmp;

            if (mCursor.moveToNext()) {
                mTimeTable += "\n";
            }
        }

        mData.info = mTimeTable;

        return mData;
    }

    public static class todayTimeTableData {
        public String title;
        public String info;
    }
}
