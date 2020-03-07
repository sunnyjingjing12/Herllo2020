package com.health.hl.recover;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.health.hl.R;
import com.health.hl.views.RecoverDeviceTraintimeDialog;

public class RecoverDeviceBaseActivity extends Activity implements View.OnClickListener {
    private ImageView adjustBack;  //返回按钮
    private Button slowtrain;
    private Button fasttrain;
    private Button mixtrain;
    private Button scenetrain;
    private String mPlan;               //训练方案
    private int trainTime;//训练时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_device_base);
        initData();
    }

    private void initData() {
        //返回按钮
        adjustBack = (ImageView) findViewById(R.id.iv_recover_device_base_back);
        adjustBack.setOnClickListener(this);
        slowtrain = (Button)findViewById(R.id.btn_recover_slow) ;
        slowtrain.setOnClickListener(this);
        fasttrain = (Button)findViewById(R.id.btn_recover_fast) ;
        fasttrain.setOnClickListener(this);
        mixtrain = (Button)findViewById(R.id.btn_recover_mix) ;
        mixtrain.setOnClickListener(this);
        scenetrain = (Button)findViewById(R.id.btn_recover_scene) ;
        scenetrain.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {

        switch (arg0.getId()) {
            case R.id.iv_recover_device_base_back:
                Intent intent = new Intent(RecoverDeviceBaseActivity.this, RecoverDeviceActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_recover_slow:
                RecoverDeviceTraintimeDialog DeviceBasetrainDialog= null;
                DeviceBasetrainDialog = new RecoverDeviceTraintimeDialog(RecoverDeviceBaseActivity.this,
                        mPlan);
                setDialogHeight(DeviceBasetrainDialog);
                DeviceBasetrainDialog.traintime = trainTime;
                DeviceBasetrainDialog.title_name ="课程1 慢肌训练";
                DeviceBasetrainDialog.setListener(new com.health.hl.views.RecoverDeviceTraintimeDialog.OnCustomDialogListener() {
                    @Override
                    public void dialogback() {
                        Intent intent = new Intent(RecoverDeviceBaseActivity.this, RecoverDeviceTrainActivity.class);
                        startActivity(intent);
                    }
                });
                DeviceBasetrainDialog.show();

             break;

            case R.id.btn_recover_fast:
                RecoverDeviceTraintimeDialog DeviceBasetrainDialog2= null;
                DeviceBasetrainDialog2 = new RecoverDeviceTraintimeDialog(RecoverDeviceBaseActivity.this,
                        mPlan);
                setDialogHeight(DeviceBasetrainDialog2);
                DeviceBasetrainDialog2.traintime = trainTime;
                DeviceBasetrainDialog2.title_name ="课程2 快肌训练";
                DeviceBasetrainDialog2.setListener(new com.health.hl.views.RecoverDeviceTraintimeDialog.OnCustomDialogListener() {
                    @Override
                    public void dialogback() {
                        Intent intent = new Intent(RecoverDeviceBaseActivity.this, RecoverDeviceTrainActivity.class);
                        startActivity(intent);
                    }
                });
                DeviceBasetrainDialog2.show();
             break;

            case R.id.btn_recover_mix:
                RecoverDeviceTraintimeDialog DeviceBasetrainDialog3= null;
                DeviceBasetrainDialog3 = new RecoverDeviceTraintimeDialog(RecoverDeviceBaseActivity.this,
                        mPlan);
                setDialogHeight(DeviceBasetrainDialog3);
                DeviceBasetrainDialog3.traintime = trainTime;
                DeviceBasetrainDialog3.title_name ="课程3 混合训练";
                DeviceBasetrainDialog3.setListener(new com.health.hl.views.RecoverDeviceTraintimeDialog.OnCustomDialogListener() {
                    @Override
                    public void dialogback() {
                        Intent intent = new Intent(RecoverDeviceBaseActivity.this, RecoverDeviceTrainActivity.class);
                        startActivity(intent);
                    }
                });
                DeviceBasetrainDialog3.show();
             break;

            case R.id.btn_recover_scene:
                RecoverDeviceTraintimeDialog DeviceBasetrainDialog4= null;
                DeviceBasetrainDialog4 = new RecoverDeviceTraintimeDialog(RecoverDeviceBaseActivity.this,
                        mPlan);
                setDialogHeight(DeviceBasetrainDialog4);
                DeviceBasetrainDialog4.traintime = trainTime;
                DeviceBasetrainDialog4.title_name ="课程4 场景训练";
                DeviceBasetrainDialog4.setListener(new com.health.hl.views.RecoverDeviceTraintimeDialog.OnCustomDialogListener() {
                    @Override
                    public void dialogback() {
                        Intent intent = new Intent(RecoverDeviceBaseActivity.this, RecoverDeviceTrainActivity.class);
                        startActivity(intent);
                    }
                });
                DeviceBasetrainDialog4.show();
             break;
             default:
             break;
        }
    }//onclick结束


    /**
     * 设置dialog高度
     *
     * @param dialog
     */
    private void setDialogHeight(Dialog dialog) {
        Window dialogWindow = dialog.getWindow();
        WindowManager m = dialogWindow.getWindowManager();
        Display d = m.getDefaultDisplay();
        android.view.WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        // lp.width = 300; // 宽度
        //        lp.width = 200;
        lp.height = (int) (d.getHeight() * 0.4); // 高度
        dialogWindow.setAttributes(lp);
    }

}
