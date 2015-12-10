package wondang.icehs.kr.whdghks913.wondanghighschool.activity.exam;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import wondang.icehs.kr.whdghks913.wondanghighschool.R;

/**
 * Created by whdghks913 on 2015-12-10.
 */
public class ExamRangeAdapter extends RecyclerView.Adapter<ExamRangeAdapter.ExamRangeViewHolder> {
    private ArrayList<ExamRangeInfo> mValues = new ArrayList<>();

    public void addItem(int time, String subject) {
        ExamRangeInfo addInfo = new ExamRangeInfo();

        addInfo.time = time;
        addInfo.subject = subject;

        mValues.add(addInfo);
    }

    @Override
    public ExamRangeAdapter.ExamRangeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_schedule_item, parent, false);

        return new ExamRangeViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ExamRangeViewHolder holder, int position) {
        ExamRangeInfo mInfo = getItemData(position);

        holder.mTime.setText(String.valueOf(mInfo.time));
        holder.mSubject.setText(mInfo.subject);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public ExamRangeInfo getItemData(int position) {
        return mValues.get(position);
    }

    public class ExamRangeViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTime, mSubject;

        public ExamRangeViewHolder(View mView) {
            super(mView);

            mTime = (TextView) mView.findViewById(R.id.list_item_entry_title);
            mSubject = (TextView) mView.findViewById(R.id.list_item_entry_summary);
        }
    }

    public class ExamRangeInfo {
        public int time;
        public String subject;
    }
}