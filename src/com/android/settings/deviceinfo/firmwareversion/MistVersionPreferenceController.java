package com.android.settings.deviceinfo.firmwareversion;

import android.content.Context;
import android.os.SystemProperties;
import android.text.TextUtils;

import androidx.annotation.VisibleForTesting;

import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.core.BasePreferenceController;

public class MistVersionPreferenceController extends BasePreferenceController {

    @VisibleForTesting
    static final String MIST_VERSION_PROPERTY = "ro.mist.version";
    static final String MIST_CODENAME_PROPERTY = "ro.mist.code";
    static final String MIST_PACKAGES = "ro.mist.packagetype";

    public MistVersionPreferenceController(Context context, String preferenceKey) {
        super(context, preferenceKey);
    }

    @Override
    public int getAvailabilityStatus() {
        return !TextUtils.isEmpty(SystemProperties.get(MIST_VERSION_PROPERTY)) && !TextUtils.isEmpty(SystemProperties.get(MIST_CODENAME_PROPERTY)) && !TextUtils.isEmpty(SystemProperties.get(MIST_PACKAGES))
                ? AVAILABLE : UNSUPPORTED_ON_DEVICE;
    }

    @Override
    public CharSequence getSummary() {
        String mistVersion = SystemProperties.get(MIST_VERSION_PROPERTY);
        String mistCodename = SystemProperties.get(MIST_CODENAME_PROPERTY);
        String mistPackages = SystemProperties.get(MIST_PACKAGES);
        if (!mistVersion.isEmpty() && !mistCodename.isEmpty() && !mistPackages.isEmpty()) {
            return mistVersion + " | " + mistCodename + " | " + mistPackages;
        } else {
            return
                mContext.getString(R.string.device_info_default);
        }
    }
}
