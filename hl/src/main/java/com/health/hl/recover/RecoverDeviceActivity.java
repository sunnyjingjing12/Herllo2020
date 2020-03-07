package com.health.hl.recover;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.health.hl.R;


/** page：用户信息主页
 *  aditor：Jing
 *  date:20191021
 *  describe:
 *  添加康复器使用教程
 */
public class RecoverDeviceActivity extends Activity implements OnClickListener {
    private ImageView deviceBack;  //返回按钮
    private TextView newguidance;   //新手必读
    private TextView trainguidance;
    private TextView basetrain;
    private TextView advancedtrain;
    private TextView trainrecord;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_recover_device);//跟xml文件联系上
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
        deviceBack = (ImageView) findViewById(R.id.iv_recover_device_back);
        deviceBack.setOnClickListener(this);
        newguidance = (TextView) findViewById(R.id.tv_title_new_guidance);
        newguidance.setOnClickListener(this);
        trainguidance = (TextView) findViewById(R.id.tv_recover_device_guidance);
        trainguidance.setOnClickListener(this);
        basetrain = (TextView) findViewById(R.id.tv_recover_device_base);
        basetrain.setOnClickListener(this);
        advancedtrain= (TextView) findViewById(R.id.tv_recover_device_advanced);
        advancedtrain.setOnClickListener(this);
        trainrecord= (TextView) findViewById(R.id.tv_recover_device_record);
        trainrecord.setOnClickListener(this);
        //            @Override
        //            public void onClick(View v) {
        //                Intent intent1 = new Intent(RecoverDeviceActivity.this, RecoverDeviceNewguidance.class);
        //                startActivity(intent1);
        //            }
        //        });
    }
    @Override
    public void onClick(View view){

        switch(view.getId()){
            //返回按钮
            case R.id.iv_recover_device_back:
                Intent intent1 = new Intent(RecoverDeviceActivity.this, RecoverMainActivity.class);
                startActivity(intent1);
                break;
            //新手必读
            case R.id.tv_title_new_guidance:
                Intent intent2 = new Intent(RecoverDeviceActivity.this, RecoverDeviceNewguidance.class);
                startActivity(intent2);
                break;
            //训练指导
            case R.id.tv_recover_device_guidance:
                Intent intent3 = new Intent(RecoverDeviceActivity.this, RecoverDeviceTrainguidanceActivity.class);
                startActivity(intent3);
                break;
            //基础训练
            case R.id.tv_recover_device_base:
                Intent intent4 = new Intent(RecoverDeviceActivity.this, RecoverDeviceBaseActivity.class);
                startActivity(intent4);
                break;
            //进阶训练
            case R.id.tv_recover_device_advanced:
                Intent intent5 = new Intent(RecoverDeviceActivity.this, RecoverDeviceAdvancedActivity.class);
                startActivity(intent5);
                break;
            //训练记录
            case R.id.tv_recover_device_record:
                Intent intent6 = new Intent(RecoverDeviceActivity.this, RecoverDeviceRecordActivity.class);
                startActivity(intent6);
                break;
                default:
                break;
        }

    }

}
