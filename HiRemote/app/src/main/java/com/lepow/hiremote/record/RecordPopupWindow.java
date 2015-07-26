package com.lepow.hiremote.record;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lepow.hiremote.R;
import com.lepow.hiremote.record.data.RecordInfo;

import butterknife.ButterKnife;
import butterknife.FindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by Dalang on 2015/7/26.
 */
public class RecordPopupWindow extends PopupWindow
{
    @FindView(R.id.record_name_edit)
    EditText recordNameEdit;

    @FindView(R.id.record_date)
    TextView recordDateView;

    @FindView(R.id.record_time_length)
    TextView recordLengthView;

    @FindView(R.id.record_play_current_length)
    TextView recordCurrentLengthView;

    @FindView(R.id.record_play_surplus_length)
    TextView recordSurplusLengthView;

    @FindView(R.id.record_play_icon)
    ImageView playButton;

    @FindView(R.id.record_play_progress)
    ProgressBar progressBar;

    private OnRecordModifyListener listener;

    private RecordInfo recordInfo;

    private boolean isNameChanged;

    public RecordPopupWindow(Context context)
    {
        super(context);

        View mainView = LayoutInflater.from(context).inflate(R.layout.edit_play_record_popview, null);
        ButterKnife.bind(mainView);

        this.setContentView(mainView);
        this.setOnDismissListener(new OnDismissListener()
        {
            @Override
            public void onDismiss()
            {
                if(isNameChanged)
                {
                    RecordPopupWindow.this.listener.onRecordModify(recordInfo);
                }
            }
        });
    }

    public void setData(RecordInfo data)
    {
        this.recordInfo = data;
        recordNameEdit.setText(data.getName());
        recordDateView.setText(data.getDateString());
        recordLengthView.setText(data.getDurationString());
        recordCurrentLengthView.setText("0:00");
        recordSurplusLengthView.setText(data.getDurationString());
}

    @OnClick({R.id.record_edit, R.id.record_delete,})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.record_edit:
                recordNameEdit.setEnabled(true);
                break;

            case R.id.record_delete:
                this.listener.onRecordDelete(recordInfo);
                break;

            case R.id.record_play_icon:
                break;

            default:
                break;
        }
    }

    @OnTextChanged(value = R.id.record_name_edit, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void onTextChanged()
    {
        this.isNameChanged = true;
    }

    public void setOnRecordModifyListener(OnRecordModifyListener listener)
    {
        this.listener = listener;
    }

    public static interface OnRecordModifyListener
    {
        void onRecordModify(RecordInfo recordInfo);

        void onRecordDelete(RecordInfo recordInfo);
    }

}
