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

public class RecoverDeviceAdvancedActivity extends Activity implements View.OnClickListener {
    private ImageView adjustBack;
    private Button advanced;
    private Button advanced2;
    private String mPlan;               //训练方案
    private int trainTime;//训练时间


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_device_advanced);
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

        @Override
        public void onDestroy() {
            // TODO Auto-generated method stub
            super.onDestroy();
        }

        private void initData() {
            //返回按钮
            adjustBack = (ImageView) findViewById(R.id.iv_recover_device_advanced_back);
            adjustBack.setOnClickListener(this);
            advanced = (Button) findViewById(R.id.btn_device_advanced);
            advanced.setOnClickListener(this);
            advanced2 = (Button) findViewById(R.id.btn_device_advanced02);
            advanced2.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            switch(view.getId()){
                case R.id.iv_recover_device_advanced_back:
                    Intent intent = new Intent(RecoverDeviceAdvancedActivity.this, RecoverDeviceActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_device_advanced:
                    RecoverDeviceTraintimeDialog DeviceBasetrainDialog= null;
                    DeviceBasetrainDialog = new RecoverDeviceTraintimeDialog(RecoverDeviceAdvancedActivity.this,
                            mPlan);
                    setDialogHeight(DeviceBasetrainDialog);
                    DeviceBasetrainDialog.traintime = trainTime;
                    DeviceBasetrainDialog.title_name ="课程1 慢肌训练";
                    DeviceBasetrainDialog.setListener(new com.health.hl.views.RecoverDeviceTraintimeDialog.OnCustomDialogListener() {
                        @Override
                //！！！！！！！！这里训练界面关闭后返回的界面不对，要改
                        public void dialogback() {
                            Intent intent = new Intent(RecoverDeviceAdvancedActivity.this, RecoverDeviceTrainActivity.class);
                            startActivity(intent);
                        }
                    });
                    DeviceBasetrainDialog.show();
                default:
                    break;
            }

        }

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
