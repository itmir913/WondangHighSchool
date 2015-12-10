package wondang.icehs.kr.whdghks913.wondanghighschool.tool;

import android.database.Cursor;

import java.io.File;

/**
 * Created by whdghks913 on 2015-12-10.
 */
public class ExamTimeTool {
    public static final String ExamDBName = "ExamInfo.db";
    public static final String ExamTableName = "ExamInfo";
    public final static String mGoogleSpreadSheetUrl = "https://docs.google.com/spreadsheets/d/1s-_F2vNNQ0yTBuqu_NORbeCJGBoaEHvsA4i84IBKWfA/pubhtml?gid=1155107873&single=true";

    public static boolean fileExists() {
        return new File(TimeTableTool.mFilePath + ExamDBName).exists();
    }

    public static examData getExamInfoData() {
        Database mDatabase = new Database();
        mDatabase.openDatabase(TimeTableTool.mFilePath, ExamDBName);

        Cursor mCursor = mDatabase.getData(ExamTableName, "examdata");
        mCursor.moveToNext();

        examData mData = new examData();
        mData.date = mCursor.getString(0);

        mCursor.moveToNext();
        mData.type = mCursor.getString(0);

        mCursor.moveToNext();
        mData.days = mCursor.getString(0);

        return mData;
    }

    public static class examData {
        public String date, type, days;
    }

    public static class examTimeTableData {
        public String date, type, days;
    }

}
