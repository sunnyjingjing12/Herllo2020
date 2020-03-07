package com.health.hl.recover;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.health.hl.R;

public class RecoverDeviceTrainguidanceActivity extends Activity implements View.OnClickListener {
    private ImageView adjustBack;
    private ImageView imageview;
    private ImageView imageview2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_device_trainguidance);
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
        adjustBack = (ImageView) findViewById(R.id.iv_recover_device_trainguidance_back);
        adjustBack.setOnClickListener(this);
        imageview = (ImageView) findViewById(R.id.image_trainguidance1);
        imageview.setOnClickListener(this);
        imageview2 = (ImageView) findViewById(R.id.image_trainguidance2);
        imageview2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.iv_recover_device_trainguidance_back:
                Intent intent = new Intent(RecoverDeviceTrainguidanceActivity.this, RecoverDeviceActivity.class);
                startActivity(intent);
                break;

            case R.id.image_trainguidance1:
                Intent intent1 =new Intent();
                intent1.setAction(Intent.ACTION_VIEW);
                intent1.addCategory(Intent.CATEGORY_BROWSABLE);
                intent1.setData(Uri.parse("https://mp.weixin.qq.com/s/0GrgNSkC9Tpfb33BgjsrqQ"));
                startActivity(intent1);
                break;

            case R.id.image_trainguidance2:
                Intent intent2 =new Intent();
                intent2.setAction(Intent.ACTION_VIEW);
                intent2.addCategory(Intent.CATEGORY_BROWSABLE);
                intent2.setData(Uri.parse("https://mp.weixin.qq.com/s/2bMs8kSqrXe0GqLG3tvqBA"));
                startActivity(intent2);
                break;
            default:
                break;
        }

    }
}
