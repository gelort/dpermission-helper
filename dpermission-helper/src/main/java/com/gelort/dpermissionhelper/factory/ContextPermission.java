package com.gelort.dpermissionhelper.factory;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

/**
 * Created by o.tretyakov on 13/05/16.
 */

/**
 * Abstract context permission
 */
public abstract class ContextPermission {
    public abstract Activity getActivity();

    /**
     * Request permissions to be granted to this application
     * @param permissions The requested permissions
     * @param requestCode Application specific request code
     */
    public abstract void requestPermissions(@NonNull String[] permissions, int requestCode);

    /**
     * Gets whether you should show UI with rationale for requesting a permission.
     * @param permission A permission your app wants to request.
     * @return Whether you can show permission rationale UI.
     */
    public abstract boolean shouldShowRequestPermissionRationale(@NonNull String permission);

    /**
     * Determine whether <em>you</em> have been granted a particular permission.
     * @param permission The name of the permission being checked.
     * @return {@link ContextCompat#checkSelfPermission}
     */
    public int checkSelfPermission(@NonNull String permission) {
        return ContextCompat.checkSelfPermission(getActivity(), permission);
    }
}
