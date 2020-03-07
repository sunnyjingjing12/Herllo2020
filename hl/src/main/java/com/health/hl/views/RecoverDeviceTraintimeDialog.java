package com.health.hl.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.health.hl.R;

/**
 * 操作说明
 *
 * @author xavi
 *
 */
public class RecoverDeviceTraintimeDialog extends Dialog implements SeekBar.OnSeekBarChangeListener {
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




    public RecoverDeviceTraintimeDialog(Context context, String title_name) {
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
        sk_traintime = (SeekBar) findViewById(R.id.sk_device_dailog_traintime);
        sk_traintime.setOnSeekBarChangeListener(this);
        tv_traintime = (TextView) findViewById(R.id.tv_device_dailog_traintime);
        sk_traintime.setProgress(traintime);
        tv_title = (TextView) findViewById(R.id.tv_deivce_dailog_title);
        tv_title.setText(title_name);

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch(v.getId())
            {
                case R.id.btn_device_dialog_cancel:
                    RecoverDeviceTraintimeDialog.this.dismiss();
                    break;
                case R.id.btn_device_dialog_starttrain:
                    //启用回调函数回到Device主界面，之后再跳转到训练界面——ing
                    RecoverDeviceTraintimeDialog.this.dismiss();
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

    // 数值改变
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId())
        {
            case R.id.sk_device_dailog_traintime:
                if(progress <= 5)
                {
                    progress = 5;
                    sk_traintime.setProgress(progress);

                }else if(progress <= 10)
                {
                    progress = 10;
                    sk_traintime.setProgress(progress);
                }else if(progress <= 15)
                {
                    progress = 15;
                    sk_traintime.setProgress(progress);
                }else if(progress <= 20)
                {
                    progress = 20;
                    sk_traintime.setProgress(progress);
                }else if(progress <= 25)
                {
                    progress = 25;
                    sk_traintime.setProgress(progress);
                }
                sk_traintime.setSecondaryProgress(progress);
                tv_traintime.setText("当前数值:" + progress);
                traintime = progress;
                break;
            default:
                break;
        }
    }

    // 开始拖动
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    // 停止拖动
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }



}
