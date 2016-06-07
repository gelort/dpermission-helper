package com.gelort.dpermissionhelper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.gelort.dpermissionhelper.factory.ContextPermissionCreator;
import com.gelort.dpermissionhelper.factory.ContextPermission;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by o.tretyakov on 06/05/16.
 */
public class PermissionHelper implements OnActivityPermissionListener {
    private static final int PERMISSIONS_REQUEST_CODE = 1;

    private ContextPermission mContextPermission;
    private OnPermissionListener mListener;

    /**
     * Internal constructor
     * @param contextPermission
     */
    private PermissionHelper(ContextPermission contextPermission) {
        mContextPermission = contextPermission;
    }

    /**
     * Constructor
     *
     * @param activity Activity should implement OnPermissionListener else throw Exception
     */
    public PermissionHelper(@NonNull Activity activity) {
        this(ContextPermissionCreator.createActivityPermission(activity));

        if (activity instanceof OnPermissionListener) {
            mListener = (OnPermissionListener) activity;
        } else {
            throw new IllegalArgumentException("Activity should implement OnPermissionListener");
        }
    }

    /**
     * Constructor
     *
     * @param fragment Activity should implement OnPermissionListener else throw Exception
     */
    public PermissionHelper(@NonNull Fragment fragment) {
        this(ContextPermissionCreator.createFragmentPermission(fragment));

        if (fragment instanceof OnPermissionListener) {
            mListener = (OnPermissionListener) fragment;
        } else {
            throw new IllegalArgumentException("Fragment should implement OnPermissionListener");
        }
    }

    /**
     * Constructor
     *
     * @param activity
     * @param permissionListener {@link OnPermissionListener}
     */
    public PermissionHelper(@NonNull Activity activity, @NonNull OnPermissionListener permissionListener) {
        this(ContextPermissionCreator.createActivityPermission(activity));

        this.mListener = permissionListener;
    }

    /**
     * Constructor
     *
     * @param fragment
     * @param permissionListener {@link OnPermissionListener}
     */
    public PermissionHelper(@NonNull Fragment fragment, @NonNull OnPermissionListener permissionListener) {
        this(ContextPermissionCreator.createFragmentPermission(fragment));

        this.mListener = permissionListener;
    }

    public static PermissionHelper getInstance(@NonNull FragmentActivity fragmentActivity) {
        return new PermissionHelper(fragmentActivity);
    }

    public static PermissionHelper getInstance(@NonNull Fragment fragment) {
        return new PermissionHelper(fragment);
    }

    public static PermissionHelper getInstance(@NonNull FragmentActivity fragmentActivity, @NonNull OnPermissionListener permissionListener) {
        return new PermissionHelper(fragmentActivity, permissionListener);
    }

    public static PermissionHelper getInstance(@NonNull Fragment fragment, @NonNull OnPermissionListener permissionListener) {
        return new PermissionHelper(fragment, permissionListener);
    }

    public boolean isPermissionGranted(@NonNull String permissionsName) {
        return mContextPermission.checkSelfPermission(permissionsName) == PackageManager.PERMISSION_GRANTED;
    }

    public void setContextPermission(ContextPermission contextPermission) {
        this.mContextPermission = contextPermission;
    }

    /**
     * Check context has access to a given permission
     *
     * @param permissionName
     * @return always return true for platforms below M
     */
    public boolean hasSelfPermission(@NonNull String permissionName) {
        if (isMarshmellowOrLater()) {
            return isPermissionGranted(permissionName);
        }

        return true;
    }

    /**
     * Request single permission
     *
     * @param permissionName The permission name
     */
    public void request(@NonNull String permissionName) {
        requestPermission(permissionName);
    }

    /**
     * Request multi permissions
     *
     * @param permissionNames The array permissions names
     */
    public void request(@NonNull String[] permissionNames) {
        requestPermission(permissionNames);
    }

    /**
     * This method returns true if the app has requested this permission
     * previously and the user denied the request.
     *
     * @param permissionName It's permission name
     * @return Should show permission rationale UI
     */
    public boolean isExplanationNeeded(@NonNull String permissionName) {
        return mContextPermission.shouldShowRequestPermissionRationale(permissionName);
    }

    /**
     * The list of permissions that the user declined or not yet granted
     *
     * @return String[] array permission not yet granted
     */
    public String[] declinedPermissions(@NonNull String[] permissions) {
        List<String> permissionsNeeded = new ArrayList<>();

        for (String permission : permissions) {
            if (!hasSelfPermission(permission)) {
                permissionsNeeded.add(permission);
            }
        }

        return permissionsNeeded.toArray(new String[permissionsNeeded.size()]);
    }

    /**
     *  Help method to check permission is present in the array permissions
     * @param permissions
     * @param permissionNeeded
     * @return
     */
    public boolean isPermission(@NonNull String[] permissions, @NonNull String permissionNeeded) {
        for (String permission : permissions) {
            if (permission.equals(permissionNeeded)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (verifyPermissions(grantResults)) {
                mListener.onPermissionGranted(permissions);
            } else {
                mListener.onPermissionDeclined(declinedPermissions(permissions));
            }
        }
    }

    /**
     * Request permission after the user is accept explanation
     * @param permissionNames
     */
    public void requestAfterExplanation(@NonNull String[] permissionNames) {
        String[] declinedPermissions = declinedPermissions(permissionNames);

        if (declinedPermissions.length == 0) {
            mListener.onPermissionGranted(permissionNames);
            return;
        }

        mContextPermission.requestPermissions(declinedPermissions, PERMISSIONS_REQUEST_CODE);
    }

    /**
     * Request single permission request, internal usage.
     * @param permissionName A permission request
     */
    private void requestPermission(@NonNull String permissionName) {
        requestPermission(new String[] {permissionName});
    }

    /**
     * Request multi permissions request, internal usage.
     * @param permissionNames A permissions request
     */
    private void requestPermission(@NonNull String[] permissionNames) {
        String[] declinedPermissions = declinedPermissions(permissionNames);

        if (declinedPermissions.length == 0) {
            mListener.onPermissionGranted(permissionNames);
            return;
        }

        List<String> explanations = new ArrayList<>();
        List<String> requestPermissions = new ArrayList<>();

        for (String declinedPermissionName : declinedPermissions) {
            if (declinedPermissionName != null) {
                if (isExplanationNeeded(declinedPermissionName)) {
                    explanations.add(declinedPermissionName);
                } else {
                    requestPermissions.add(declinedPermissionName);
                }
            }
        }

        if (!explanations.isEmpty()) {
            mListener.onPermissionNeedExplanation(explanations.toArray(new String[explanations.size()]));
        }

        if (!requestPermissions.isEmpty()) {
            mContextPermission.requestPermissions(
                    requestPermissions.toArray(new String[requestPermissions.size()]),
                    PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Check permissions granted
     * @param grantResults
     * @return true if all permissions is granted
     */
    private boolean verifyPermissions(int[] grantResults) {
        if (grantResults.length < 1) {
            return false;
        }

        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

    /**
     * The build version is M or later
     *
     * @return true for M or later
     */
    private boolean isMarshmellowOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
