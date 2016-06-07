package com.gelort.dpermissionhelper.sample;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.gelort.dpermissionhelper.AlertDialogHelper;
import com.gelort.dpermissionhelper.OnPermissionListener;
import com.gelort.dpermissionhelper.PermissionHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnPermissionListener {

    private static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String ACCESS_CAMERA = Manifest.permission.CAMERA;

    private static final String[] PERMISSIONS = new String[]{
            ACCESS_FINE_LOCATION,
            ACCESS_CAMERA
    };

    private PermissionHelper mPermissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPermissionHelper = new PermissionHelper(this);
        mPermissionHelper.request(PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionNeedExplanation(final String[] permissionNames) {
        AlertDialogHelper.getAlertDialog(this,
                R.style.AppAlertDialogTheme,
                getExplanationMessage(permissionNames),
                getString(R.string.cancel),
                getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPermissionHelper.requestAfterExplanation(permissionNames);
                    }
                }).show();
    }

    @Override
    public void onPermissionGranted(String[] permissionNames) {
        showToast(getString(R.string.permission_granted_message, Arrays.toString(permissionNames)));
    }

    @Override
    public void onPermissionDeclined(String[] permissionNames) {
        showToast(getString(R.string.permission_declined_message, Arrays.toString(permissionNames)));
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private String getExplanationMessage(String[] permissionNames) {
        StringBuilder sb = new StringBuilder();
        List<String> permissions = new ArrayList<>(Arrays.asList(permissionNames));

        if (permissions.contains(ACCESS_FINE_LOCATION)) {
            sb.append(getString(R.string.location_permission_required));
            sb.append("\n");
        }

        if (permissions.contains(ACCESS_CAMERA)) {
            sb.append(getString(R.string.camera_permission_required));
        }

        return sb.toString();
    }
}
