package com.lepow.hiremote.authorise;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.lepow.hiremote.R;
import com.lepow.hiremote.app.BaseActivity;
import com.lepow.hiremote.home.HomeActivity;
import com.lepow.hiremote.misc.ActivityResultCode;
import com.lepow.hiremote.misc.IntentKeys;
import com.mn.tiger.authorize.IAuthorizeCallback;
import com.mn.tiger.authorize.TGAuthorizeResult;
import com.mn.tiger.utility.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by peng on 15/8/16.
 */
public class LoginActivity extends BaseActivity implements IAuthorizeCallback
{
    @Bind(R.id.login_account_input)
    EditText accountInputView;

    @Bind(R.id.login_password_input)
    EditText passwordInputView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.default_green_bg));
        ButterKnife.bind(this);

        setBarTitleText(getString(R.string.login_title));
        getNavigationBar().getMiddleTextView().setTextColor(Color.WHITE);
    }

    @OnClick({R.id.login_forget_password, R.id.login_btn, R.id.login_register_btn})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.login_btn:
                login();
                break;
            case R.id.login_forget_password:
                startResetPasswordActivity();
                break;

            case R.id.login_register_btn:
                startRegisterActivity();
                break;

            default:
                break;
        }
    }

    @OnTextChanged(value = R.id.login_password_input, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
    }

    private void startResetPasswordActivity()
    {
        startActivity(new Intent(this, ResetPasswordActivity.class));
    }

    private void startRegisterActivity()
    {
        startActivityForResult(new Intent(this, RegisterActivity.class), 0);
    }

    private void login()
    {
        String account = accountInputView.getText().toString().trim();
        if(TextUtils.isEmpty(account))
        {
            ToastUtils.showToast(this, R.string.account_can_not_be_null);
            return;
        }

        String password = passwordInputView.getText().toString().trim();
        if(TextUtils.isEmpty(password) || password.length() < 5)
        {
            ToastUtils.showToast(this, R.string.password_can_not_be_null_more_than_5_letter);
            return;
        }

        HSAuthorization.getInstance().setAccount(account);
        HSAuthorization.getInstance().setPassword(password);
        HSAuthorization.getInstance().executeAuthorize(this, this);

        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(accountInputView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onAuthorizeSuccess(TGAuthorizeResult tgAuthorizeResult)
    {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onAuthorizeCancel()
    {

    }

    @Override
    public void onAuthorizeError(int i, String s, String s1)
    {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == ActivityResultCode.REGISTER_SUCCESS)
        {
            accountInputView.setText(data.getStringExtra(IntentKeys.USER_NAME));
            passwordInputView.setText(data.getStringExtra(IntentKeys.PASSWORD));
            login();
        }
    }
}
