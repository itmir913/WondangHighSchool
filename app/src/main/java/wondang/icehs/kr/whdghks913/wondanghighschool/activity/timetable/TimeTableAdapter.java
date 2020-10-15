package wondang.icehs.kr.whdghks913.wondanghighschool.activity.timetable;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import wondang.icehs.kr.whdghks913.wondanghighschool.R;

/**
 * Created by whdghks913 on 2015-12-10.
 */
public class TimeTableAdapter extends RecyclerView.Adapter<TimeTableAdapter.TimeTableViewHolder> {
//    private int mBackground;
    private ArrayList<TimeTableInfo> mValues = new ArrayList<>();

    public TimeTableAdapter(Context mContext) {
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
    public TimeTableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_timetable_item, parent, false);
//        mView.setBackgroundResource(mBackground);

        return new TimeTableViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final TimeTableViewHolder holder, int position) {
        TimeTableInfo mInfo = getItemData(position);

        holder.mTimeText.setText(String.valueOf(mInfo.time));
        holder.mTimeName.setText(mInfo.name);
        holder.mRoom.setText(mInfo.room);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public TimeTableInfo getItemData(int position) {
        return mValues.get(position);
    }

    public class TimeTableViewHolder extends RecyclerView.ViewHolder {
        //        public final View mView;
        public final TextView mTimeText, mTimeName, mRoom;

        public TimeTableViewHolder(View mView) {
            super(mView);
//            this.mView = mView;

            mTimeText = (TextView) mView.findViewById(R.id.mTimeText);
            mTimeName = (TextView) mView.findViewById(R.id.mTimeName);
            mRoom = (TextView) mView.findViewById(R.id.mRoom);
        }
    }

    public class TimeTableInfo {
        public int time;
        public String name;
        public String room;
    }
}