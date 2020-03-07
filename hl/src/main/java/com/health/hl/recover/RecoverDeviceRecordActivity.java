package com.health.hl.recover;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.health.hl.R;

public class RecoverDeviceRecordActivity extends Activity implements View.OnClickListener {
    private ImageView adjustBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_device_record);
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
        adjustBack = (ImageView) findViewById(R.id.iv_recover_device_record_back);
        adjustBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.iv_recover_device_record_back:
                Intent intent = new Intent(RecoverDeviceRecordActivity.this, RecoverDeviceActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

    }
}

