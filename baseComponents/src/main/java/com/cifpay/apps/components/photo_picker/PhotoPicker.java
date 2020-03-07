package com.cifpay.apps.components.photo_picker;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.cifpay.apps.components.R;

public class PhotoPicker extends Activity {

    private Intent resultIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_dialog);

        resultIntent = getIntent();

        Button takePhoto = (Button) this.findViewById(R.id.btn_take_photo);
        Button pickPhoto = (Button) this.findViewById(R.id.btn_pick_photo);
        Button cancel = (Button) this.findViewById(R.id.btn_cancel);

        takePhoto.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, 1);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        pickPhoto.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent, 2);
                        }
                        catch (ActivityNotFoundException ignored) {
                        }
                    }
                }
        );
        cancel.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }

        if (data.getExtras() != null) {
            resultIntent.putExtras(data.getExtras());
        }

        if (data.getData() != null) {
            resultIntent.setData(data.getData());
        }

        setResult(1, resultIntent);
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }
}
