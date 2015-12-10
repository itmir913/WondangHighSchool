package wondang.icehs.kr.whdghks913.wondanghighschool.activity.schedule;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import wondang.icehs.kr.whdghks913.wondanghighschool.R;

/**
 * Created by whdghks913 on 2015-12-10.
 */
public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {
    private int mBackground;
    private ArrayList<ScheduleInfo> mValues = new ArrayList<>();

    public ScheduleAdapter(Context mContext) {
        TypedValue mTypedValue = new TypedValue();
        mContext.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
    }

    public void addItem(String mSchedule, String mDate) {
        ScheduleInfo addInfo = new ScheduleInfo();

        addInfo.date = mDate;
        addInfo.schedule = mSchedule;

        mValues.add(addInfo);
    }

    public void addItem(String mSchedule, String mDate, boolean isHoliday) {
        ScheduleInfo addInfo = new ScheduleInfo();

        addInfo.date = mDate;
        addInfo.schedule = mSchedule;

        mValues.add(addInfo);
    }

    @Override
    public ScheduleAdapter.ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        mView.setBackgroundResource(mBackground);

        return new ScheduleViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ScheduleViewHolder holder, int position) {
        ScheduleInfo mInfo = getItemData(position);

        holder.mDate.setText(mInfo.date);
        holder.mSchedule.setText(mInfo.schedule);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public ScheduleInfo getItemData(int position) {
        return mValues.get(position);
    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder {
        //        public final View mView;
        public final TextView mDate, mSchedule;

        public ScheduleViewHolder(View mView) {
            super(mView);
//            this.mView = mView;

            mSchedule = (TextView) mView.findViewById(android.R.id.text1);
            mDate = (TextView) mView.findViewById(android.R.id.text2);
        }
    }

    public class ScheduleInfo {
        public String date;
        public String schedule;
    }
}