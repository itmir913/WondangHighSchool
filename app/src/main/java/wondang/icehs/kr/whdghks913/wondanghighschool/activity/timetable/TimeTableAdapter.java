package wondang.icehs.kr.whdghks913.wondanghighschool.activity.timetable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import wondang.icehs.kr.whdghks913.wondanghighschool.R;

/**
 * Created by whdghks913 on 2015-12-02.
 */
public class TimeTableAdapter extends BaseAdapter {
    //    private int mBackground;
    private ArrayList<TimeTableInfo> mValues = new ArrayList<>();
    private Context mContext;

    public TimeTableAdapter(Context mContext) {
        this.mContext = mContext;
//        TypedValue mTypedValue = new TypedValue();
//        mContext.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
//        mBackground = mTypedValue.resourceId;
    }

    public void addItem(int time, String name, String room) {
        TimeTableInfo addInfo = new TimeTableInfo();

        addInfo.time = time;
        addInfo.name = name;
        addInfo.room = room;

        mValues.add(addInfo);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        TimeTableViewHolder holder;

        if (convertView == null) {
            holder = new TimeTableViewHolder();

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_timetable_item, null);

            holder.mTimeText = (TextView) convertView.findViewById(R.id.mTimeText);
            holder.mTimeName = (TextView) convertView.findViewById(R.id.mTimeName);
            holder.mRoom = (TextView) convertView.findViewById(R.id.mRoom);

            convertView.setTag(holder);

        } else {
            holder = (TimeTableViewHolder) convertView.getTag();
        }

        TimeTableInfo mInfo = getItemData(position);

//        boolean isGrade = mInfo.isGrade;

//        if (isGrade) {
//            if (mInfo.mGrade == -1 || mInfo.mClass == -1) {
//                holder.mGradeClass.setText(mContext.getString(R.string.no_setting_my_grade));
//            } else {
//                holder.mGradeClass.setText(String.format(mContext.getString(R.string.timetable_title), mInfo.mGrade, mInfo.mClass));
//            }
//        } else {
        holder.mTimeText.setText(Integer.toString(mInfo.time));
        holder.mTimeName.setText(mInfo.name);
        holder.mRoom.setText(mInfo.room);


        return convertView;
    }

    @Override
    public int getCount() {
        return mValues.size();
    }

    @Override
    public TimeTableInfo getItem(int position) {
        return mValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public TimeTableInfo getItemData(int position) {
        return mValues.get(position);
    }

    public class TimeTableViewHolder {
        public TextView mTimeText, mTimeName, mRoom;
    }

    public class TimeTableInfo {
//        public boolean isGrade;
        public int time;
        public String name;
        public String room;

//        public int mGrade;
//        public int mClass;
    }
}