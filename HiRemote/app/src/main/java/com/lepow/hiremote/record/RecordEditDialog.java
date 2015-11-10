package com.lepow.hiremote.record;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lepow.hiremote.R;
import com.lepow.hiremote.record.data.RecordDataManager;
import com.lepow.hiremote.record.data.RecordInfo;
import com.lepow.hiremote.utils.DurationUtils;
import com.lepow.hiremote.widget.HSAlertDialog;
import com.mn.tiger.log.Logger;
import com.mn.tiger.media.TGAudioPlayer;
import com.mn.tiger.utility.DateUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by Dalang on 2015/8/20.
 */
public class RecordEditDialog extends HSAlertDialog implements TGAudioPlayer.OnPlayListener
{
    private static final Logger LOG = Logger.getLogger(RecordEditDialog.class);

    @Bind(R.id.record_name)
    TextView recordNameView;

    @Bind(R.id.record_date)
    TextView recordDateView;

    @Bind(R.id.record_time_length)
    TextView recordLengthView;

    @Bind(R.id.record_play_current_length)
    TextView recordCurrentLengthView;

    @Bind(R.id.record_play_surplus_length)
    TextView recordSurplusLengthView;

    @Bind(R.id.record_play_icon)
    ImageView playButton;

    @Bind(R.id.record_play_progress)
    ProgressBar progressBar;

    private OnRecordModifyListener listener;

    private RecordInfo recordInfo;

    private boolean isNameChanged = false;

    public RecordEditDialog(Context context)
    {
        super(context);

        View mainView = LayoutInflater.from(context).inflate(R.layout.edit_play_record_dialog, null);
        ButterKnife.bind(this, mainView);
        recordNameView.setEnabled(false);

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
        recordNameView.setText(data.getTitle());
        recordDateView.setText(DateUtils.date2String(data.getTimestamp(), DateUtils.DATE_FORMAT));
        recordLengthView.setText(DurationUtils.duration2String(data.getDuration()));
        recordCurrentLengthView.setText("00:00:00");
        recordSurplusLengthView.setText("-" + DurationUtils.duration2String(data.getDuration()));
    }

    @OnClick({R.id.record_edit, R.id.record_delete,R.id.record_play_icon})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.record_edit:
                openRenameConfirmDialog(recordInfo);
                break;

            case R.id.record_delete:
                openDeleteConfirmDialog(recordInfo);
                break;

            case R.id.record_play_icon:
                if(TGAudioPlayer.getInstance().isPlaying())
                {
                    TGAudioPlayer.getInstance().stop();
                }
                else
                {
                    TGAudioPlayer.getInstance().start(this.recordInfo.getRecordFilePath(), this);
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onPlayStart(String dataSource)
    {
        playButton.setImageResource(R.drawable.voice_pause);
    }

    @Override
    public void onPlaying(String dataSource, int playDuration, int audioDuration)
    {
        recordCurrentLengthView.setText(DurationUtils.duration2String(playDuration));
        recordSurplusLengthView.setText("-" + DurationUtils.duration2String(audioDuration - playDuration));
        progressBar.setProgress(playDuration * 100 / audioDuration);
    }

    @Override
    public void onPlayPause(String dataSource)
    {
        playButton.setImageResource(R.drawable.voice_play);
    }

    @Override
    public void onPlayStop(String dataSource)
    {
        playButton.setImageResource(R.drawable.voice_play);
    }

    @Override
    public void onPlayComplete(String dataSource)
    {
        recordCurrentLengthView.setText("00:00:00");
        recordSurplusLengthView.setText("-" + DurationUtils.duration2String(this.recordInfo.getDuration()));
        progressBar.setProgress(0);
        playButton.setImageResource(R.drawable.voice_play);
    }

    @OnTextChanged(value = R.id.record_name, callback = OnTextChanged.Callback.TEXT_CHANGED)
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
                //更新数据库
                RecordDataManager.getInstance().removeRecord(getContext(), recordInfo);
                dialog.dismiss();
                RecordEditDialog.this.dismiss();
                listener.onRecordDelete(recordInfo);
            }
        });

        dialog.show();
    }

    private void openRenameConfirmDialog(final RecordInfo recordInfo)
    {
        HSAlertDialog dialog = new HSAlertDialog(getContext());
        dialog.setTitleText(getContext().getString(R.string.change_name));
        final EditText renameView = new EditText(getContext());
        renameView.setTextColor(getContext().getResources().getColor(R.color.text_color_normal));
        renameView.setBackgroundColor(Color.TRANSPARENT);
        renameView.setText(recordInfo.getTitle());
        renameView.setSelection(recordInfo.getTitle().length());
        int padding = getContext().getResources().getDimensionPixelSize(R.dimen.margin_val_20px);
        renameView.setPadding(padding, padding, padding, padding);

        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setBodyContentView(renameView, layoutParams);
        //设置取消按钮
        dialog.setLeftButton(getContext().getString(R.string.cancel), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                hideKeyBoard(renameView.getWindowToken());
                dialog.dismiss();
            }
        });

        //设置确定按钮
        dialog.setRightButton(getContext().getString(R.string.confirm), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String newTitle = renameView.getText().toString();
                if(!recordInfo.getTitle().equals(newTitle))
                {
                    recordInfo.setTitle(newTitle);
                    //更新数据库
                    RecordDataManager.getInstance().saveRecord(getContext(), recordInfo);
                    recordNameView.setText(newTitle);
                    isNameChanged = true;
                }
                hideKeyBoard(renameView.getWindowToken());
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void hideKeyBoard(IBinder windowToken)
    {
        InputMethodManager inputMethodManager = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
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
