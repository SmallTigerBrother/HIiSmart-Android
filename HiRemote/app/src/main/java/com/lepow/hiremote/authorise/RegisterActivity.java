package com.lepow.hiremote.authorise;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.lepow.hiremote.R;
import com.lepow.hiremote.app.BaseActivity;
import com.lepow.hiremote.misc.IntentKeys;
import com.mn.tiger.authorize.IRegisterCallback;
import com.mn.tiger.utility.StringUtils;

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

    @FindView(R.id.register_agreement)
    TextView agreementView;

    private HSAuthorizer authorizer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        ButterKnife.bind(this);

        authorizer = new HSAuthorizer(this, null, null);
    }

    @OnClick({R.id.register_next_btn, R.id.facebook_login_btn})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.register_next_btn:
                register();
                break;
            case R.id.facebook_login_btn:
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
            return;
        }

        String email = userNameInputView.getText().toString().trim();
        if(TextUtils.isEmpty(email) || StringUtils.isEmailAddress(email))
        {
            return;
        }

        String password = passwordInputView.getText().toString().trim();
        String confirmPassword = passwordConfirmInputView.getText().toString().trim();
        if(TextUtils.isEmpty(password))
        {
            return;
        }

        if(!password.equals(confirmPassword))
        {
            return;
        }

        authorizer.register(userName, password, this, email);
    }

    @Override
    public void onSuccess()
    {
        Intent intent = new Intent();
        intent.putExtra(IntentKeys.USER_NAME, userNameInputView.getText().toString().trim());
        intent.putExtra(IntentKeys.PASSWORD, passwordInputView.getText().toString().trim());
        startActivity(intent);
        finish();
    }

    @Override
    public void onError(int i, String s)
    {
    }
}
