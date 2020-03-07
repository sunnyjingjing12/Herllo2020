package com.health.hl.views;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.health.hl.R;

public class RecoverDeviceAdvancedDialog extends Dialog {
    public OnCustomDialogListener getListener() {
        return listener;
    }

    public void setListener(OnCustomDialogListener listener) {
        this.listener = listener;
    }

    private Context context = null;
    public String title_name;
    private OnCustomDialogListener listener;
    private TextView tv_title = null;
    private SeekBar sk_traintime; //训练时间滑动条
    private TextView tv_traintime;
    public int traintime = 5;
    public int traintype = 1;
    public String mplan = "A";
    public static final String TRAINTIME = "traintime";




    public RecoverDeviceAdvancedDialog(Context context, String title_name) {
        super(context, R.style.DialogStyleRecover);
        this.context = context;
        this.title_name = title_name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_recover_device_basetrain);
        // 设置标题
        setTitle(title_name);
        Button BtnCancel = (Button) findViewById(R.id.btn_device_dialog_cancel);
        BtnCancel.setOnClickListener(clickListener);
        Button BtnStrartrain = (Button) findViewById(R.id.btn_device_dialog_starttrain);
        BtnStrartrain.setOnClickListener(clickListener);


    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch(v.getId())
            {
                case R.id.btn_device_dialog_cancel:
                    RecoverDeviceAdvancedDialog.this.dismiss();
                    break;
                case R.id.btn_device_dialog_starttrain:
                    //启用回调函数回到Device主界面，之后再跳转到训练界面——ing
                    RecoverDeviceAdvancedDialog.this.dismiss();
                    listener.dialogback();
                    break;
                default:
                    break;

            }
        }
    };

    // 定义回调事件，用于dialog的点击事件
    public interface OnCustomDialogListener {
        public void dialogback();
    }

}
