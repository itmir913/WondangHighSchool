package wondang.icehs.kr.whdghks913.wondanghighschool.tool;

import android.content.Context;
import android.os.Environment;

import java.io.File;

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
}
