package com.lepow.hiremote.authorise;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;

import com.lepow.hiremote.R;
import com.lepow.hiremote.app.BaseActivity;
import com.lepow.hiremote.misc.ActivityResultCode;
import com.lepow.hiremote.misc.IntentKeys;
import com.mn.tiger.authorize.IRegisterCallback;
import com.mn.tiger.utility.StringUtils;
import com.mn.tiger.utility.ToastUtils;

import butterknife.ButterKnife;
import butterknife.FindView;
import butterknife.OnClick;

/**
 * Created by peng on 15/8/16.
 */
public class RegisterActivity extends BaseActivity implements IRegisterCallback
{
    @FindView(R.id.register_username_input)
    EditText userNameInputView;

    @FindView(R.id.login_email_input)
    EditText emailInputView;

    @FindView(R.id.register_password_input)
    EditText passwordInputView;

    @FindView(R.id.register_password_confirm_input)
    EditText passwordConfirmInputView;

    @FindView(R.id.register_agreement_checkbox)
    CheckBox agreementCheckBox;

    private HSAuthorizer authorizer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.default_green_bg));
        ButterKnife.bind(this);
        setBarTitleText(getString(R.string.register_btn_text));
        getNavigationBar().getMiddleTextView().setTextColor(Color.WHITE);
        authorizer = new HSAuthorizer(this, null, null);
    }

    @OnClick({R.id.register_btn, R.id.register_agreement})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.register_btn:
                register();
                break;
            case R.id.register_agreement:
                break;
            default:
                break;
        }
    }

    private void register()
    {
        String userName = userNameInputView.getText().toString().trim();
        if(TextUtils.isEmpty(userName))
        {
            ToastUtils.showToast(this, R.string.account_can_not_be_null);
            return;
        }

        String email = emailInputView.getText().toString().trim();
        if(TextUtils.isEmpty(email) || !StringUtils.isEmailAddress(email))
        {
            ToastUtils.showToast(this, R.string.email_invalid);
            return;
        }

        String password = passwordInputView.getText().toString().trim();
        String confirmPassword = passwordConfirmInputView.getText().toString().trim();
        if(TextUtils.isEmpty(password) || password.length() < 5)
        {
            ToastUtils.showToast(this, R.string.password_can_not_be_null_more_than_5_letter);
            return;
        }

        if(!password.equals(confirmPassword))
        {
            ToastUtils.showToast(this, R.string.password_not_same);
            return;
        }

        if(!agreementCheckBox.isChecked())
        {
            return;
        }

        authorizer.register(email, password, this, userName);

        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(userNameInputView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onSuccess()
    {
        Intent data = new Intent();
        data.putExtra(IntentKeys.USER_NAME, userNameInputView.getText().toString().trim());
        data.putExtra(IntentKeys.PASSWORD, passwordInputView.getText().toString().trim());
        setResult(ActivityResultCode.REGISTER_SUCCESS, data);
        finish();
    }

    @Override
    public void onError(int i, String s)
    {
        //TODO 提示注册失败
    }
}
