package com.gelort.dpermissionhelper;

import android.support.annotation.NonNull;

/**
 * Created by o.tretyakov on 06/05/16.
 */

/**
 * Interface definition for callback the result from requesting permissions
 */
public interface OnActivityPermissionListener {

    /**
     * This method is invoked for every call on activity/fragment request permission
     * @param requestCode The request code passed in activity/fragment request permission
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     */
    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
}
