package wondang.icehs.kr.whdghks913.wondanghighschool.schedule;

import java.util.ArrayList;

import wondang.icehs.kr.whdghks913.wondanghighschool.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class ScheduleViewHolder {
	public TextView mDay;

	public TextView mDayOfTheWeek;

	public TextView mText;
}

class ScheduleListViewAdapter extends BaseAdapter {
	private Context mContext = null;
	public ArrayList<ScheduleListData> mListData = new ArrayList<ScheduleListData>();

	public ScheduleListViewAdapter(Context mContext) {
		super();
		this.mContext = mContext;
	}

	public void addItem(String mDay, String mDayOfTheWeek, String mText,
			boolean isHoliday, int year, int month, int day) {

		ScheduleListData addInfo = new ScheduleListData();
		addInfo.mDay = mDay;
		addInfo.mDayOfTheWeek = mDayOfTheWeek;
		addInfo.mText = mText;
		addInfo.isHoliday = isHoliday;

		addInfo.year = year;
		addInfo.month = month;
		addInfo.day = day;

		mListData.add(addInfo);
	}

	public void clearData() {
		mListData.clear();
	}

	// public void sort() {
	// Collections.sort(mListData, ScheduleListData.ALPHA_COMPARATOR);
	// }

	@Override
	public int getCount() {
		return mListData.size();
	}

	@Override
	public ScheduleListData getItem(int position) {
		return mListData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ScheduleViewHolder holder;
		if (convertView == null) {
			holder = new ScheduleViewHolder();

			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.schedule_list_item, null);

			holder.mDay = (TextView) convertView.findViewById(R.id.mDay);
			holder.mDayOfTheWeek = (TextView) convertView
					.findViewById(R.id.mDayOfTheWeek);
			holder.mText = (TextView) convertView.findViewById(R.id.mText);

			convertView.setTag(holder);
		} else {
			holder = (ScheduleViewHolder) convertView.getTag();
		}

		ScheduleListData mData = mListData.get(position);

		String mDayOfTheWeek = mData.mDayOfTheWeek;
		if (mDayOfTheWeek.equals("일") || mData.isHoliday) {
			holder.mDay.setTextColor(Color.RED);
			holder.mDayOfTheWeek.setTextColor(Color.RED);
			holder.mText.setTextColor(Color.RED);
		} else if (mDayOfTheWeek.equals("토")) {
			holder.mDay.setTextColor(Color.BLUE);
			holder.mDayOfTheWeek.setTextColor(Color.BLUE);
			holder.mText.setTextColor(Color.BLUE);
		} else {
			holder.mDay.setTextColor(Color.BLACK);
			holder.mDayOfTheWeek.setTextColor(Color.BLACK);
			holder.mText.setTextColor(Color.BLACK);
		}

		holder.mDay.setText(mData.mDay);
		holder.mDayOfTheWeek.setText(mDayOfTheWeek);
		holder.mText.setText(mData.mText);

		return convertView;
	}
}

class ScheduleListData {
	/**
	 * 리스트 정보를 담고 있을 객체 생성
	 */
	public String mDay;
	public String mDayOfTheWeek;
	public String mText;
	public boolean isHoliday;

	public int year, month, day;

	// public static final Comparator<ScheduleListData> ALPHA_COMPARATOR = new
	// Comparator<ScheduleListData>() {
	// private final Collator sCollator = Collator.getInstance();
	//
	// @Override
	// public int compare(ScheduleListData mListDate_1, ScheduleListData
	// mListDate_2) {
	// return sCollator.compare(mListDate_1.mCalender,
	// mListDate_2.mCalender);
	// }
	// };
}