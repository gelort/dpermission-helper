package com.gelort.dpermissionhelper.factory;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * Created by o.tretyakov on 13/05/16.
 */

/**
 * Fragment permission
 */
public class FragmentPermission extends ContextPermission {
    private Fragment mFragment;

    public FragmentPermission(Fragment fragment) {
        this.mFragment = fragment;
    }

    @Override
    public FragmentActivity getActivity() {
        return mFragment.getActivity();
    }

    @Override
    public void requestPermissions(@NonNull String[] permissions, int requestCode) {
        mFragment.requestPermissions(permissions, requestCode);
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
        return mFragment.shouldShowRequestPermissionRationale(permission);
    }
}
