package wondang.icehs.kr.whdghks913.wondanghighschool.activity.bap.star;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.Vector;

import wondang.icehs.kr.whdghks913.wondanghighschool.R;

public class BapStarActivity extends AppCompatActivity {
    Spinner mGiveStarType;
    RatingBar mPostRatingBar;
    EditText mBapReview;

    Spinner mDateSpinner;
    RatingBar mGetRatingStar;

    ProgressDialog mDialog;

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
//            mGetRatingStar.setRating();
        }
    }

    public void postStar(View v) {
        // 0 : Lunch, 1 : Dinner
        int position = mGiveStarType.getSelectedItemPosition();
        float rate = mPostRatingBar.getRating();

        (new HttpTask()).execute(String.valueOf(position), String.valueOf(rate), mBapReview.getText().toString(), "my");
    }

    private class HttpTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mDialog = new ProgressDialog(BapStarActivity.this);
            mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mDialog.setTitle(R.string.post_bap_star_posting);
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                HttpPost postRequest = new HttpPost("https://script.google.com/macros/s/AKfycbwR755X_mEWKZ8LKQuQf81t5rVerzOLCg1ztZyHisNr7rB8rIo/exec");

                //전달할 값들
                Vector<NameValuePair> nameValue = new Vector<>();
                nameValue.add(new BasicNameValuePair("sheet_name", "RiceStar"));
                nameValue.add(new BasicNameValuePair("type", params[0]));
                nameValue.add(new BasicNameValuePair("rate", params[1]));
                nameValue.add(new BasicNameValuePair("memo", params[2]));
                nameValue.add(new BasicNameValuePair("deviceId", params[3]));

                //웹 접속 - UTF-8으로
                HttpEntity Entity = new UrlEncodedFormEntity(nameValue, "UTF-8");
                postRequest.setEntity(Entity);

                HttpClient mClient = new DefaultHttpClient();
                mClient.execute(postRequest);

//                웹 서버에서 값받기
//                HttpResponse res = mClient.execute(postRequest);
//                HttpEntity entityResponse = res.getEntity();
//                InputStream im = entityResponse.getContent();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(im, "UTF-8"));
//
//                String total = "";
//                String tmp = "";
//                //버퍼에있는거 전부 더해주기
//                //readLine -> 파일내용을 줄 단위로 읽기
//                while ((tmp = reader.readLine()) != null) {
//                    if (tmp != null) {
//                        total += tmp;
//                    }
//                }
//                im.close();

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean value) {
            super.onPostExecute(value);

            if (mDialog != null) {
                mDialog.dismiss();
                mDialog = null;
            }

            if (value) {
                mBapReview.setText("");

                AlertDialog.Builder builder = new AlertDialog.Builder(BapStarActivity.this, R.style.AppCompatAlertDialogStyle);
                builder.setTitle(R.string.post_bap_star_title);
                builder.setMessage(R.string.post_bap_star_success);
                builder.setPositiveButton(android.R.string.ok, null);
                builder.show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(BapStarActivity.this, R.style.AppCompatErrorAlertDialogStyle);
                builder.setTitle(R.string.post_bap_star_title);
                builder.setMessage(R.string.post_bap_star_failed);
                builder.setPositiveButton(android.R.string.ok, null);
                builder.show();
            }
        }
    }

}
