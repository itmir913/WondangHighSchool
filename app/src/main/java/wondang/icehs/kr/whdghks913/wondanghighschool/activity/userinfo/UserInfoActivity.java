package wondang.icehs.kr.whdghks913.wondanghighschool.activity.userinfo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import itmir.tistory.com.xor.SecurityXor;
import wondang.icehs.kr.whdghks913.wondanghighschool.R;
import wondang.icehs.kr.whdghks913.wondanghighschool.tool.Preference;

public class UserInfoActivity extends AppCompatActivity {
    TextView mUserInfo, mUserText;
    Preference mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);

        mPref = new Preference(this, "userInfo");
        mUserInfo = (TextView) findViewById(R.id.mUserInfo);
        mUserText = (TextView) findViewById(R.id.mUserText);

        int userInfo = mPref.getInt("userInfo", 0);
        if (userInfo == 1) {
            mUserInfo.setText(R.string.user_info_licensed);
            mUserText.setText(R.string.user_info_licensed_help);
        }

        FloatingActionButton mFab = (FloatingActionButton) findViewById(R.id.mFab);
        if (userInfo == 1) {
            mFab.setVisibility(View.GONE);
        } else {
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle(R.string.user_info_class_up_title);

                    final EditText inputText = new EditText(UserInfoActivity.this);

                    builder.setView(inputText);
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            final String passWord = "Aleef)^f{eM";
                            final String inputPassWord = new SecurityXor().getSecurityXor(inputText.getText().toString().trim(), 777);

                            if (inputPassWord.equals(passWord)) {
                                mPref.putInt("userInfo", 1);
                                Snackbar.make(view, R.string.user_info_class_up_success, Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, null);
                    builder.show();
                }
            });
        }
    }

}
