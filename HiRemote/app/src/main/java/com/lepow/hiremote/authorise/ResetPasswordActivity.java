package com.lepow.hiremote.authorise;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.lepow.hiremote.R;
import com.lepow.hiremote.app.BaseActivity;
import com.lepow.hiremote.request.SimpleLoadCallback;
import com.mn.tiger.request.receiver.TGHttpResult;
import com.mn.tiger.utility.StringUtils;
import com.mn.tiger.utility.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dalang on 2015/8/19.
 */
public class ResetPasswordActivity extends BaseActivity
{
    @Bind(R.id.reset_password_email_input)
    EditText emailInputView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_layout);
        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.default_green_bg));
        getMiddleTextView().setTextColor(Color.WHITE);
        setBarTitleText(getString(R.string.reset_password_title));
        ButterKnife.bind(this);
    }

    @OnClick({R.id.reset_password_btn})
    void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.reset_password_btn:
                resetPassword();
                break;
            default:
                break;
        }
    }

    private void resetPassword()
    {
        String email = emailInputView.getText().toString().trim();
        if(TextUtils.isEmpty(email) || !StringUtils.isEmailAddress(email))
        {
            ToastUtils.showToast(this, R.string.email_invalid);
            return;
        }

        HSAuthorization.getInstance().requestResetPassword(this, email, new SimpleLoadCallback<Void>(this)
        {
            @Override
            public void onLoadSuccess(Void result, TGHttpResult tgHttpResult)
            {
                ToastUtils.showToast(ResetPasswordActivity.this, R.string.please_check_your_email_later);
                finish();
            }
        });

        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(emailInputView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
