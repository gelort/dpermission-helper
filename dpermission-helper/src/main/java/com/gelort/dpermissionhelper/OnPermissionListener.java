package com.gelort.dpermissionhelper;

/**
 * Created by o.tretyakov on 06/05/16.
 */

/**
 * Interface definition for a callback to be invoked when a permission Granted/Revoked and need explanation for user.
 */
public interface OnPermissionListener {

    /**
     * Called when a permissions should be explain user.
     * @param permissionNames A permission your app wants to request.
     */
    void onPermissionNeedExplanation(String[] permissionNames);

    /**
     *  Called when a permissions are granted.
     * @param permissionNames A permissions to granted
     */
    void onPermissionGranted(String[] permissionNames);

    /**
     * Called when a permissions are revoked.
     * @param permissionNames A permissions to revoked
     */
    void onPermissionDeclined(String[] permissionNames);
}
