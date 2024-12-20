/*
 * Copyright (C) 2023 ArrowOS-T
 * Copyright (C) 2024 OrionOS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.deviceinfo.firmwareversion.controller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.VisibleForTesting;
import androidx.preference.PreferenceScreen;

import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.core.BasePreferenceController;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.widget.LayoutPreference;

public class FirmwareHeaderPreferenceController extends BasePreferenceController {

    private static final String TAG = "firmwareDialogCtrl";
    private static final int DELAY_TIMER_MILLIS = 500;
    private static final int ACTIVITY_TRIGGER_COUNT = 3;

    private static final String PROP_MIST_DEVICE_CODENAME = "ro.mist.device";
    private static final String PROP_MIST_DEVICE = "ro.product.model";

    private final UserManager mUserManager;
    private final long[] mHits = new long[ACTIVITY_TRIGGER_COUNT];

    private RestrictedLockUtils.EnforcedAdmin mFunDisallowedAdmin;
    private boolean mFunDisallowedBySystem;

    private LayoutPreference mPreference;
    private TextView mDeviceSummery, mDeviceCodename;

    public FirmwareHeaderPreferenceController(Context context, String key) {
        super(context, key);
        mUserManager = (UserManager) mContext.getSystemService(Context.USER_SERVICE);
        initializeAdminPermissions();
    }

    @Override
    public int getAvailabilityStatus() {
        return AVAILABLE;
    }


    @Override
    public void displayPreference(PreferenceScreen screen) {
        super.displayPreference(screen);
        mPreference = screen.findPreference(getPreferenceKey());
        mDeviceSummery = mPreference.findViewById(mContext.getResources().getIdentifier("id/device_summery", null, mContext.getPackageName()));
        mDeviceCodename = mPreference.findViewById(mContext.getResources().getIdentifier("id/device_codename", null, mContext.getPackageName()));

        mDeviceSummery.setText(getDeviceName());
        mDeviceCodename.setText(getDeviceCodename());
    }


    private String getDeviceName() {
        String device = SystemProperties.get(PROP_MIST_DEVICE, "");
        if (device.equals("")) {
            device = Build.MANUFACTURER;
        }
        return device;
    }

    private String getDeviceCodename() {
        String deviceCodename = SystemProperties.get(PROP_MIST_DEVICE_CODENAME, "");
        if (deviceCodename.equals("")) {
            deviceCodename = Build.MODEL;
        }
        return deviceCodename;
    }

    /**
     * Copies the array onto itself to remove the oldest hit.
     */
    @VisibleForTesting
    void arrayCopy() {
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
    }

    @VisibleForTesting
    void initializeAdminPermissions() {
        mFunDisallowedAdmin = RestrictedLockUtilsInternal.checkIfRestrictionEnforced(
                mContext, UserManager.DISALLOW_FUN, UserHandle.myUserId());
        mFunDisallowedBySystem = RestrictedLockUtilsInternal.hasBaseUserRestriction(
                mContext, UserManager.DISALLOW_FUN, UserHandle.myUserId());
    }
}
