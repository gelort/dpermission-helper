package com.gelort.dpermissionhelper.factory;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * Created by o.tretyakov on 07/06/16.
 */
public class ContextPermissionCreator {
    public static ContextPermission createFragmentPermission(Fragment fragment) {
        return new FragmentPermission(fragment);
    }

    public static ContextPermission createActivityPermission(Activity activity) {
        return new ActivityPermission(activity);
    }
}
