package com.gelort.dpermissionhelper;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.gelort.dpermissionhelper.factory.FragmentPermission;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import static android.os.Build.VERSION_CODES.M;
import static android.os.Build.VERSION_CODES.LOLLIPOP;

public class PermissionTest {
    private static final String FAKE_PERMISSION1 = "fake_permission_1";
    private static final String FAKE_PERMISSION2 = "fake_permission_2";

    private static final String[] FAKE_PERMISSIONS = new String[]{
            FAKE_PERMISSION1,
            FAKE_PERMISSION2
    };

    private static final String[] FAKE_DECLINED_PERMISSIONS = new String[]{
            FAKE_PERMISSION1
    };

    @Mock
    protected OnPermissionListener mPermissionListener;

    @Mock
    protected FragmentActivity mFragmentActivity;

    @Mock
    protected FragmentPermission mFragmentPermission;

    @Mock
    protected Fragment mFragment;

    @InjectMocks
    PermissionHelper mPermissionHelper = new PermissionHelper(mFragment, mPermissionListener);

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);

        mPermissionHelper.setContextPermission(mFragmentPermission);
    }

    @Test
    public void checkPermissionHelperMethod() {
        assertTrue(mPermissionHelper.isPermission(FAKE_PERMISSIONS, FAKE_PERMISSION1));
        assertTrue(mPermissionHelper.isPermission(FAKE_PERMISSIONS, FAKE_PERMISSION2));
    }

    @Test
    public void permission_isGranted() throws Exception {
        assumeApiLevel(M);

        when(mFragmentPermission.getActivity()).thenReturn(mFragmentActivity);
        when(mFragmentPermission.checkSelfPermission(FAKE_PERMISSION1)).thenReturn(PackageManager.PERMISSION_GRANTED);

        mPermissionHelper.request(FAKE_PERMISSION1);

        verify(mPermissionListener, times(1)).onPermissionGranted(new String[]{FAKE_PERMISSION1});
        verify(mPermissionListener, times(0)).onPermissionDeclined(null);
        verify(mPermissionListener, times(0)).onPermissionNeedExplanation(null);
    }

    @Test
    public void permission_isRevoked() throws Exception {
        assumeApiLevel(M);

        when(mFragmentPermission.getActivity()).thenReturn(mFragmentActivity);
        when(mFragmentPermission.checkSelfPermission(FAKE_PERMISSION1)).thenReturn(PackageManager.PERMISSION_DENIED);
        when(mFragmentPermission.shouldShowRequestPermissionRationale(FAKE_PERMISSION1)).thenReturn(false);

        mPermissionHelper.request(FAKE_PERMISSION1);

        verify(mPermissionListener, times(0)).onPermissionGranted(null);
        verify(mPermissionListener, times(0)).onPermissionNeedExplanation(null);

        // test request permission result from Activity/Fragment

        mPermissionHelper.onRequestPermissionsResult(1, FAKE_DECLINED_PERMISSIONS, new int[]{PackageManager.PERMISSION_DENIED});
        verify(mPermissionListener, times(1)).onPermissionDeclined(FAKE_DECLINED_PERMISSIONS);

        mPermissionHelper.onRequestPermissionsResult(1, new String[]{FAKE_PERMISSION1}, new int[]{PackageManager.PERMISSION_GRANTED});
        verify(mPermissionListener, times(1)).onPermissionGranted(new String[]{FAKE_PERMISSION1});
    }

    @Test
    public void permission_isNeedRationale() throws Exception {
        assumeApiLevel(M);

        when(mFragmentPermission.getActivity()).thenReturn(mFragmentActivity);
        when(mFragmentPermission.checkSelfPermission(FAKE_PERMISSION1)).thenReturn(PackageManager.PERMISSION_DENIED);
        when(mFragmentPermission.shouldShowRequestPermissionRationale(FAKE_PERMISSION1)).thenReturn(true);

        mPermissionHelper.request(FAKE_PERMISSION1);

        verify(mPermissionListener, times(0)).onPermissionGranted(null);
        verify(mPermissionListener, times(1)).onPermissionNeedExplanation(new String[]{FAKE_PERMISSION1});
    }

    @Test
    public void permission_isBeforeMarshmellow() throws Exception {
        assumeApiLevel(LOLLIPOP);

        when(mFragmentPermission.getActivity()).thenReturn(mFragmentActivity);
        when(mFragmentPermission.checkSelfPermission(FAKE_PERMISSION1)).thenReturn(PackageManager.PERMISSION_DENIED);

        mPermissionHelper.request(FAKE_PERMISSION1);

        verify(mPermissionListener, times(1)).onPermissionGranted(new String[]{FAKE_PERMISSION1});
        verify(mPermissionListener, times(0)).onPermissionNeedExplanation(null);
        verify(mPermissionListener, times(0)).onPermissionDeclined(null);
    }

    private void assumeApiLevel(int apiLevel) throws Exception {
        // Adjust the value of Build.VERSION.SDK_INT statically using reflection
        Field sdkIntField = Build.VERSION.class.getDeclaredField("SDK_INT");
        sdkIntField.setAccessible(true);

        // Temporarily remove the SDK_INT's "final" modifier
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(sdkIntField, sdkIntField.getModifiers() & ~Modifier.FINAL);

        // Update the SDK_INT value, re-finalize the field, and lock it again
        sdkIntField.set(null, apiLevel);
        modifiersField.setInt(sdkIntField, sdkIntField.getModifiers() | Modifier.FINAL);
        sdkIntField.setAccessible(false);
    }
}