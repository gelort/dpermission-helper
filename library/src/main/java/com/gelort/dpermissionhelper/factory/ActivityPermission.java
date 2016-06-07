package com.gelort.dpermissionhelper.factory;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

/**
 * Created by o.tretyakov on 13/05/16.
 */

/**
 * Activity permission
 */
public class ActivityPermission extends ContextPermission {
    private Activity mActivity;

    public ActivityPermission(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public Activity getActivity() {
        return mActivity;
    }

    @Override
    public void requestPermissions(@NonNull String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(mActivity, permissions,requestCode);
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission);
    }
}
