package com.lepow.hiremote.record;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lepow.hiremote.R;
import com.lepow.hiremote.record.data.RecordInfo;
import com.lepow.hiremote.widget.HSAlertDialog;

import butterknife.ButterKnife;
import butterknife.FindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by Dalang on 2015/8/20.
 */
public class RecordEditDialog extends HSAlertDialog
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

    private boolean isNameChanged = false;

    public RecordEditDialog(Context context)
    {
        super(context);

        View mainView = LayoutInflater.from(context).inflate(R.layout.edit_play_record_popview, null);
        ButterKnife.bind(this, mainView);
        recordNameEdit.setEnabled(false);

        this.setContentView(mainView);
        this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        this.setOnDismissListener(new OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialog)
            {
                if (isNameChanged)
                {
                    RecordEditDialog.this.listener.onRecordModify(recordInfo);
                }
            }
        });
    }

    public void setData(RecordInfo data)
    {
        this.recordInfo = data;
        recordNameEdit.setText(data.getTitle());
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
                openDeleteConfirmDialog(recordInfo);
                break;

            case R.id.record_play_icon:
                break;

            default:
                break;
        }
    }

    @OnTextChanged(value = R.id.record_name_edit, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        if(!this.recordInfo.getTitle().equals(s.toString()))
        {
            this.isNameChanged = true;
        }
    }

    private void openDeleteConfirmDialog(final RecordInfo recordInfo)
    {
        HSAlertDialog dialog = new HSAlertDialog(getContext());
        dialog.setTitleVisibility(View.GONE);
        dialog.setBodyText(getContext().getString(R.string.make_sure_you_delete_location));
        //设置取消按钮
        dialog.setLeftButton(getContext().getString(R.string.cancel), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });

        //设置确定按钮
        dialog.setRightButton(getContext().getString(R.string.confirm), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                listener.onRecordDelete(recordInfo);
                //更新数据库
                dialog.dismiss();
                RecordEditDialog.this.dismiss();
            }
        });

        dialog.show();
    }

    public void setOnRecordModifyListener(OnRecordModifyListener listener)
    {
        this.listener = listener;
    }

    public interface OnRecordModifyListener
    {
        void onRecordModify(RecordInfo recordInfo);

        void onRecordDelete(RecordInfo recordInfo);
    }
}
