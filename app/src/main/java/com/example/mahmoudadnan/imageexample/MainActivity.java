package com.example.mahmoudadnan.imageexample;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mahmoudadnan.imageexample.image.ImageUtils;
import com.example.mahmoudadnan.imageexample.image.interfaces.ImageCallback;

public class MainActivity extends AppCompatActivity implements ImageCallback{

    Button takeImageBtn;
    ImageView imageV;
    ImageUtils imageUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        takeImageBtn = (Button)findViewById(R.id.take);
        imageV = (ImageView)findViewById(R.id.image);

        imageUtils = new ImageUtils();
        imageUtils.setCallBack(this);
    }
    public void Capture(View view) {
        imageUtils.startCamera(MainActivity.this);
    }

    public void PickImage(View view) {
        imageUtils.pickGallary(MainActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        imageUtils.onActivityResult(requestCode, resultCode, data, MainActivity.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        imageUtils.onRequestPermissionsResult(MainActivity.this, requestCode, permissions, grantResults);
    }

    @Override
    public void returnedImage(Uri imageUri) {
        imageV.setImageURI(imageUri);
        Log.e("Uri is ", String.valueOf(imageUri));

    }
}
