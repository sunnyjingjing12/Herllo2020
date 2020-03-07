package com.health.hl.recover;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.health.hl.R;

public class RecoverDeviceNewguidance extends Activity implements OnClickListener {
    private ImageView adjustBack;  //返回按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_device_newguidance);
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
        adjustBack = (ImageView) findViewById(R.id.iv_recover_device_newguidance_back);
        adjustBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.iv_recover_device_newguidance_back:
                Intent intent = new Intent(RecoverDeviceNewguidance.this, RecoverDeviceActivity.class);
                startActivity(intent);
                break;
                default:
                break;
        }

    }
}
