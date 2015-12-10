package wondang.icehs.kr.whdghks913.wondanghighschool.activity.exam;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wondang.icehs.kr.whdghks913.wondanghighschool.R;

/**
 * Created by whdghks913 on 2015-12-10.
 */
public class ExamTimeFragment extends Fragment {

    public static Fragment getInstance(int grade, int type, int position) {
        ExamTimeFragment mFragment = new ExamTimeFragment();

        Bundle args = new Bundle();
        args.putInt("grade", grade);
        args.putInt("type", type);
        args.putInt("position", position);
        mFragment.setArguments(args);

        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recyclerview, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        final ExamTimeAdapter mAdapter = new ExamTimeAdapter();
        recyclerView.setAdapter(mAdapter);

        Bundle args = getArguments();
        int grade = args.getInt("grade");
        int type = args.getInt("type");
        int position = args.getInt("position");

        mAdapter.addItem(1, "문학");
        mAdapter.addItem(2, "미적분2");
        mAdapter.addItem(3, "확률과 통계");

        return recyclerView;
    }
}
