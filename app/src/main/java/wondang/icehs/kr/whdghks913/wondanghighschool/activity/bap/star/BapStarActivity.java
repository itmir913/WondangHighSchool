package wondang.icehs.kr.whdghks913.wondanghighschool.activity.bap.star;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;

import wondang.icehs.kr.whdghks913.wondanghighschool.R;

public class BapStarActivity extends AppCompatActivity {
    Spinner mGiveStarType;
    RatingBar mPostRatingBar;
    EditText mBapReview;

    Spinner mDateSpinner;
    RatingBar mGetRatingStar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bap_star);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);

        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        LinearLayout giveStarLayout = (LinearLayout) findViewById(R.id.giveStarLayout);
        LinearLayout showStarLayout = (LinearLayout) findViewById(R.id.showStarLayout);

        Intent mIntent = getIntent();
        int starType = mIntent.getIntExtra("starType", 1);

        if (starType == 1) {
            giveStarLayout.setVisibility(View.VISIBLE);
            showStarLayout.setVisibility(View.GONE);

            mGiveStarType = (Spinner) findViewById(R.id.mGiveStarType);
            mPostRatingBar = (RatingBar) findViewById(R.id.mPostRatingBar);
            mBapReview = (EditText) findViewById(R.id.mBapReview);
        } else if (starType == 2) {
            giveStarLayout.setVisibility(View.GONE);
            showStarLayout.setVisibility(View.VISIBLE);

            mDateSpinner = (Spinner) findViewById(R.id.mDateSpinner);
            mGetRatingStar = (RatingBar) findViewById(R.id.mGetRatingStar);
        }
    }

    public void postStar(View v) {

    }

}
