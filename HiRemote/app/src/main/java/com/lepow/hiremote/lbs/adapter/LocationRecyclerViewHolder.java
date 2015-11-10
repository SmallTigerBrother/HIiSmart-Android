package com.lepow.hiremote.lbs.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.lepow.hiremote.R;
import com.lepow.hiremote.app.HSApplication;
import com.lepow.hiremote.lbs.data.LocationDataManager;
import com.lepow.hiremote.lbs.data.LocationInfo;
import com.lepow.hiremote.widget.BGASwipeItemLayout;
import com.lepow.hiremote.widget.HSAlertDialog;
import com.mn.tiger.utility.DateUtils;
import com.mn.tiger.widget.recyclerview.TGRecyclerViewHolder;

import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;

/**
 *定位记录列表行填充类
 */
public class LocationRecyclerViewHolder extends TGRecyclerViewHolder<LocationInfo>
{
    @Bind(R.id.location_date)
    TextView locationDate;

    @Bind(R.id.location_address)
    TextView locationAddress;

    @Bind(R.id.location_time)
    TextView locationTime;

    private LocationInfo locationInfo;

    private BGASwipeItemLayout swipeItemLayout;

    private Locale locale;

    @Override
    protected int getLayoutId()
    {
        return R.layout.location_list_item;
    }

    @Override
    public View initView(ViewGroup parent, int viewType)
    {
        locale = HSApplication.getInstance().getResources().getConfiguration().locale;

        swipeItemLayout = (BGASwipeItemLayout)super.initView(parent, viewType);
        swipeItemLayout.setDelegate((BGASwipeItemLayout.BGASwipeItemLayoutDelegate)getAdapter());
        return swipeItemLayout;
    }

    @Override
    public void fillData(ViewGroup parent, View convertView, LocationInfo itemData, int position, int viewType)
    {
        this.locationInfo = itemData;
        String date = DateUtils.date2String(itemData.getTimestamp(),DateUtils.DATE_FORMAT);
        if(!locale.getLanguage().equalsIgnoreCase("zh"))
        {
            date = DateUtils.date2String(itemData.getTimestamp(), "MM-dd-yyyy");
        }

        locationDate.setText(date);
        locationTime.setText(itemData.getTimeString());
        if(TextUtils.isEmpty(itemData.getRemark()))
        {
            locationAddress.setText(itemData.getAddress());
        }
        else
        {
            locationAddress.setText(itemData.getRemark());
        }
    }

    @OnClick({R.id.location_delete_btn, R.id.location_edit_btn})
    void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.location_edit_btn:
                openEditLocationDialog();
                break;
            case R.id.location_delete_btn:
                openDeleteConfirmDialog();
                break;
            default:
                break;
        }
    }

    /**
     * 打开编辑对话框
     */
    private void openEditLocationDialog()
    {
        HSAlertDialog dialog = new HSAlertDialog(getContext());
        dialog.setTitleText(getContext().getResources().getString(R.string.edit_location_title));
        //设置输入框
        final EditText editText = new EditText(getContext());
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setTextColor(getContext().getResources().getColor(R.color.text_color_normal));
        String address = TextUtils.isEmpty(locationInfo.getRemark()) ? locationInfo.getAddress() : locationInfo.getRemark();
        editText.setText(address);
        editText.setSelection(address.length());
        editText.setSelection(locationInfo.getAddress().length());
        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editText.setPadding(getContext().getResources().getDimensionPixelSize(R.dimen.margin_val_20px),
                getContext().getResources().getDimensionPixelSize(R.dimen.margin_val_20px),
                getContext().getResources().getDimensionPixelSize(R.dimen.margin_val_20px),
                getContext().getResources().getDimensionPixelSize(R.dimen.margin_val_30px));
        dialog.setBodyContentView(editText, layoutParams);
        //设置取消按钮
        dialog.setLeftButton(getContext().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                ((InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(editText.getWindowToken(), 0);
                dialog.dismiss();
            }
        });

        //设置确定按钮
        dialog.setRightButton(getContext().getResources().getString(R.string.confirm), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //更新数据库
                locationInfo.setRemark(editText.getText().toString().trim());
                switch (locationInfo.getDataType())
                {
                    case LocationInfo.DATA_TYPE_PINNED_LOCATION:
                        LocationDataManager.getInstance().savePinnedLocation(getContext(), locationInfo);
                        break;

                    case LocationInfo.DATA_TYPE_DISCONNECT_LOCATION:
                        LocationDataManager.getInstance().saveDisconnectedLocation(getContext(), locationInfo);
                        break;
                    default:
                        break;
                }
                //更新列表
                getAdapter().updatePartData(locationInfo);
                ((InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(editText.getWindowToken(), 0);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openDeleteConfirmDialog()
    {
        HSAlertDialog dialog = new HSAlertDialog(getContext());
        dialog.setTitleVisibility(View.GONE);
        dialog.setBodyText(getContext().getResources().getString(R.string.make_sure_you_delete_location));
        //设置取消按钮
        dialog.setLeftButton(getContext().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });

        //设置确定按钮
        dialog.setRightButton(getContext().getResources().getString(R.string.confirm), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                ((LocationAdapter)getAdapter()).closeOpenedItems();
                removeLocation();
                //更新数据库
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * 删除一条定位记录
     */
    private void removeLocation()
    {
        switch (locationInfo.getDataType())
        {
            case LocationInfo.DATA_TYPE_PINNED_LOCATION:
                LocationDataManager.getInstance().removePinnedLocation(getContext(), locationInfo);
                break;

            case LocationInfo.DATA_TYPE_DISCONNECT_LOCATION:
                LocationDataManager.getInstance().removeDisconnectedLocation(getContext(), locationInfo);
                break;

            default:
                break;
        }

        getAdapter().removeItem(locationInfo);
    }

}

