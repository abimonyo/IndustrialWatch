package com.app.industrialwatch.common.base;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.industrialwatch.common.utils.PermissionUtils;

import java.io.File;
import java.io.IOException;


public class TakePhotoActivity extends BaseActivity {

    /**
     * Flag to identify camera access permission is requested
     */
    public static final int TAKE_PHOTO_PERMISSION = 101;
    /**
     * Flag to identify gallery access permission is requested
     */
    public static final int CHOOSE_PHOTO_PERMISSION = 102;
    /**
     * Flag to identify camera intent is called
     */
    public static final int TAKE_PHOTO_RESULT_CODE = 110;
    /**
     * Flag to identify gallery intent is called
     */
    public static final int CHOOSE_PHOTO_RESULT_CODE = 120;
    /**
     * Flag to identify gallery intent is called
     */
    public static final int CHOOSE_MULTIPLE_RESULT_CODE = 130;

    public Uri selectedPhotoUri;
    public ActivityResultLauncher<Intent> imagePickerLauncher;

    public void initializeImagePicker() {
        imagePickerLauncher = this.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if (o.getResultCode() == Activity.RESULT_OK) {
                    if (o.getData() != null) {
                        handlePhotoResult();
                    } else
                        handlePhotoResult();

                } else {
                    selectedPhotoUri = null;
                }
            }
        });
    }


    /**
     * show user dialog to select either open camera Or gallery
     */
    protected void showChoosePhotoDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (PermissionUtils.checkAndRequestPermissions(TakePhotoActivity.this
                    , new String[]{Manifest.permission.READ_MEDIA_IMAGES}, CHOOSE_PHOTO_PERMISSION))
                dispatchChoosePictureIntent();
        } else {
            if (PermissionUtils.checkAndRequestPermissions(TakePhotoActivity.this
                    , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, CHOOSE_PHOTO_PERMISSION))
                dispatchChoosePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CHOOSE_PHOTO_PERMISSION && PermissionUtils.verifyPermission(grantResults)) {
            dispatchChoosePictureIntent();
        } else
            showToast("permission not granted.");
    }

    /**
     * Check build os version and then dispatch single or multiple choose picture intent
     */


    /**
     * create and call open camera intent
     */
   /* public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = getPhotoFile();
        if (photo != null && takePictureIntent.resolveActivity(TakePhotoActivity.this.getPackageManager()) != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedPhotoUri = Uri.fromFile(photo));
            imagePickerLauncher.launch(takePictureIntent);
            //startActivityForResult(Intent.createChooser(takePictureIntent, ""), TAKE_PHOTO_RESULT_CODE);
        }
    }*/
    private void handlePhotoResult() {
        Intent resultIntent = new Intent();
        resultIntent.setData(selectedPhotoUri);
        setResult(RESULT_OK, resultIntent);
    }


    /**
     * create and open choose from gallery/photos intent
     */
    public void dispatchChoosePictureIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        imagePickerLauncher.launch(intent);
    }


    /**
     * create and return a file to store the taken image in the device storage
     *
     * @return created file in device storage
     */
    /*final public File getPhotoFile() {
        String timeStamp = DateUtils.convertDate(System.currentTimeMillis(), DateUtils.FILE_NAME);
        String imageFileName = "IMG" + timeStamp + "";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File photoFile = null;
        try {
            if (!storageDir.exists())
                storageDir.mkdirs();

            photoFile = File.createTempFile(imageFileName, ".jpg", storageDir);

        } catch (IOException e) {
            Logger.caughtException(e);
        }
        return photoFile;
    }*/
}
