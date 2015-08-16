package com.lepow.hiremote.authorise;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lepow.hiremote.R;
import com.lepow.hiremote.app.BaseActivity;

import butterknife.ButterKnife;
import butterknife.FindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by peng on 15/8/16.
 */
public class LoginActivity extends BaseActivity
{
    @FindView(R.id.login_account_input)
    EditText accountInputView;

    @FindView(R.id.login_password_input)
    EditText passwordInputView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.login_forget_password, R.id.login_next_btn})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.login_next_btn:
                break;
            case R.id.login_forget_password:
                break;
            default:
                break;
        }
    }

    @OnTextChanged(value = R.id.login_password_input, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
    }

    private void login()
    {

    }
}
