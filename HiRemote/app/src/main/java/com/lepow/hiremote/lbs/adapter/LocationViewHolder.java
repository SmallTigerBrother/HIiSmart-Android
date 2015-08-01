package com.lepow.hiremote.lbs.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lepow.hiremote.dialog.HRDialog;
import com.lepow.hiremote.R;
import com.lepow.hiremote.lbs.PinnedLocationMapActivity;
import com.lepow.hiremote.lbs.data.LocationInfo;
import com.lepow.hiremote.lbs.data.LocationDataManager;
import com.lepow.hiremote.misc.ActivityResultCode;
import com.lepow.hiremote.misc.IntentKeys;
import com.mn.tiger.widget.swipelistview.SwipeListViewHolder;

import butterknife.FindView;
import butterknife.OnClick;

/**
 *定位记录列表行填充类
 */
public class LocationViewHolder extends SwipeListViewHolder<LocationInfo>
{
    @FindView(R.id.location_list_item_above)
    RelativeLayout aboveLayout;

    @FindView(R.id.location_date)
    TextView locationDate;

    @FindView(R.id.location_address)
    TextView locationAddress;

    @FindView(R.id.location_time)
    TextView locationTime;

    private LocationInfo itemData;

    @Override
    protected int getFrontViewId()
    {
        return R.id.location_list_item_above;
    }

    @Override
    protected int getBackViewId()
    {
        return R.id.location_list_item_below;
    }

    @Override
    public void fillData(ViewGroup parent, View convertView, LocationInfo itemData, int position)
    {
        this.itemData = itemData;

        locationDate.setText(((LocationInfo) itemData).getDateString());
        locationTime.setText(((LocationInfo) itemData).getTimeString());
        if(TextUtils.isEmpty(itemData.getRemark()))
        {
            locationAddress.setText(itemData.getAddress());
        }
        else
        {
            locationAddress.setText(itemData.getRemark());
        }
    }

    @OnClick({R.id.location_list_item_above, R.id.location_delete, R.id.location_edit})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.location_list_item_above:
                startMapActivity(itemData.getDataType());
                break;

            case R.id.location_delete:
                removeLocation(itemData);
                break;

            case R.id.location_edit:
                openEditLocationDialog();
                break;
            default:
                break;
        }
    }

    /**
     * 删除一条定位记录
     * @param itemData
     */
    private void removeLocation(LocationInfo itemData)
    {
        switch (itemData.getDataType())
        {
            case TYPE_PINNED_LOCATION:
                LocationDataManager.getInstanse().removePinnedLocation(getContext(), itemData);
                break;

            case TYPE_DISCONNECT_LOCATIONA:
                LocationDataManager.getInstanse().removeDisconnectedLocation(getContext(), itemData);
                break;

            default:
                break;
        }

        getAdapter().removeItem(itemData);
    }

    /**
     *启动地图界面
     * @param dataType
     */
    private void startMapActivity(LocationInfo.DataType dataType)
    {
        Intent intent = new Intent();
        switch (dataType)
        {
            case TYPE_PINNED_LOCATION:
                //启动地图界面
                intent.setClass(getContext(), PinnedLocationMapActivity.class);
                getContext().startActivity(intent);
                break;

            case TYPE_DISCONNECT_LOCATIONA:
                //返回到上一界面
                intent.putExtra(IntentKeys.LOCATION_INFO, itemData);
                ((Activity) getContext()).setResult(ActivityResultCode.DISCONNECT_LOCATION_HISTORY, intent);
                ((Activity) getContext()).finish();

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
        HRDialog dialog = new HRDialog(getContext());
        dialog.setTitle(R.string.edit_location_title);
        //设置输入框
        final EditText editText = new EditText(getContext());
        dialog.setBodyContentView(editText, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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
                itemData.setRemark(editText.getText().toString().trim());
                switch (itemData.getDataType())
                {
                    case TYPE_PINNED_LOCATION:
                        LocationDataManager.getInstanse().savePinnedLocation(getContext(), itemData);
                        break;

                    case TYPE_DISCONNECT_LOCATIONA:
                        LocationDataManager.getInstanse().saveDisconnectedLocation(getContext(), itemData);
                        break;
                    default:
                        break;
                }
                //更新列表
                getAdapter().notifyDataSetChanged();
            }
        });

        dialog.show();
    }

}
