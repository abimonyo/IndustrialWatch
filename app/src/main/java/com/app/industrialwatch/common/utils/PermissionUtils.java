package com.app.industrialwatch.common.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.M)
public class PermissionUtils {

    // TODO: handle never ask use case
    // https://inthecheesefactory.com/blog/things-you-need-to-know-about-android-m-permission-developer-edition/en

    // region PERMISSION_CONSTANTS
    public static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1199;
    // calendar
    public static final int PERMISSION_READ_CALENDAR = 1;
    public static final int PERMISSION_WRITE_CALENDAR = 2;
    // camera
    public static final int PERMISSION_CAMERA = 3;

    // storage
    public static final int PERMISSION_READ_EXTERNAL_STORAGE = 22;
    public static final int PERMISSION_EXTERNAL_STORAGE = 23;
    public static final int READ_MEDIA_IMAGES = 24;
    // endregion


    public static boolean hasPermissionGranted(Context context, String[] permissions) {
        boolean hasGranted = false;
        for (String permission : permissions) {
            hasGranted = (ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED);
            if (!hasGranted)
                return false;
        }
        return hasGranted;
    }

    public static boolean shouldShowPermissionRationale(Activity activity, String[] permissions) {
        boolean shouldShowRequestPermissionRationale = false;
        for (String permission : permissions) {
            shouldShowRequestPermissionRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
            if (!shouldShowRequestPermissionRationale)
                return false;
        }
        return shouldShowRequestPermissionRationale;
    }

    public static void requestPermission(Object object, int permissionId, String[] permission) {
        if (object instanceof Activity)
            ActivityCompat.requestPermissions((Activity) object, permission, permissionId);
        else {
            Fragment fragment = ((Fragment) object);
            if (fragment.isAdded() && fragment.getActivity() != null)
                fragment.requestPermissions(permission, permissionId);
        }
    }

    public static boolean verifyPermission(int[] grantResults) {
        // at least one result must be checked.
        if (grantResults.length < 1)
            return false;
        // verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    public static boolean requestPermission(Context context, String permission, int requestCode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED)
            return true;
        else {
            if (context instanceof AppCompatActivity) {
                ((AppCompatActivity) context).requestPermissions(new String[]{permission}, requestCode);
            }
        }
        return false;
    }

    /**
     * Check and ask for disabled permissions
     *
     * @param activity    Activity calling the method
     * @param permissions permissions array needed to be checked
     * @param requestCode request code associated with the request call
     * @return flag specifying permission are enabled or not
     */
    public static boolean checkAndRequestPermissions(Activity activity, String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        List<String> requiredPerm = new ArrayList<>();
        for (String permission : permissions)
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED)
                requiredPerm.add(permission);

        if (requiredPerm.size() == 0)
            return true;
        String[] mPermission = new String[requiredPerm.size()];
        mPermission = requiredPerm.toArray(mPermission);
        if (mPermission != null)
            activity.requestPermissions(mPermission, requestCode);
        return false;
    }

}
