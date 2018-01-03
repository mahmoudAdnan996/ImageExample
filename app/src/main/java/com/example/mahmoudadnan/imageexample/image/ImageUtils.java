package com.example.mahmoudadnan.imageexample.image;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.example.mahmoudadnan.imageexample.image.interfaces.ImageCallback;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

/**
 * Created by mahmoud.adnan on 1/3/2018.
 */

public class ImageUtils {

    private static final int CAMERA_REQUEST_CODE = 2020;
    private static final int CAMERA_PERMISSION_CODE = 2030;
    private static final int IMAGE_GALLERY_REQUEST = 3030;

    private ImageCallback callBack;
    private Uri imageUri;

    // ========== start camera
    public boolean startCamera(Activity activity){
        if (!hasPermission(activity)) {
            requestCameraPermission(activity);
            return false;
        }
        Intent takeImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takeImage.resolveActivity(activity.getPackageManager()) != null){
            activity.startActivityForResult(takeImage, CAMERA_REQUEST_CODE);
            return true;
        }
        return true;
    }

    // ============= Pick image
    public boolean pickGallary(Activity activity){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activity.startActivityForResult(Intent.createChooser(intent, "SELECT IMAGE"),IMAGE_GALLERY_REQUEST);
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data, Context context) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (callBack != null) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap)extras.get("data");
                    imageUri= getImageUri(context, imageBitmap);
                    callBack.returnedImage(imageUri);
                }
            }
        }
        if (requestCode == IMAGE_GALLERY_REQUEST){
            if (resultCode == Activity.RESULT_OK){
                if (callBack != null){
                    imageUri = data.getData();
                    callBack.returnedImage(imageUri);
                }
            }
        }
    }

    public void setCallBack(ImageCallback callBack) {
        this.callBack = callBack;
    }

    public void onRequestPermissionsResult(Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_CODE) {
            int index = Arrays.asList(permissions).indexOf(Manifest.permission.CAMERA);
            if (index == -1 || grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                showDialogCameraPermissionNeeded(activity);
            } else {
                startCamera(activity);
            }
        }
    }

    private boolean hasPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
    }

    private void showDialogCameraPermissionNeeded(Activity activity) {
        Toast.makeText(activity, "Camera Permission is a must", Toast.LENGTH_LONG).show();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
