/*
 * Copyright (C) 2022 The Android Open Source Project
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

package com.android.settings.spa.app.specialaccess

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.android.settings.R
import com.android.settingslib.spa.framework.common.SettingsEntry
import com.android.settingslib.spa.framework.common.SettingsEntryBuilder
import com.android.settingslib.spa.framework.common.SettingsPage
import com.android.settingslib.spa.framework.common.SettingsPageProvider
import com.android.settingslib.spa.framework.compose.navigator
import com.android.settingslib.spa.widget.preference.Preference
import com.android.settingslib.spa.widget.preference.PreferenceModel
import com.android.settingslib.spa.widget.scaffold.RegularScaffold

object SpecialAppAccessPageProvider : SettingsPageProvider {
    override val name = "SpecialAppAccess"

    @Composable
    override fun Page(arguments: Bundle?) {
        SpecialAppAccessPage()
    }

    @Composable
    fun EntryItem() {
        Preference(object : PreferenceModel {
            override val title = stringResource(R.string.special_access)
            override val onClick = navigator(name)
        })
    }

    fun buildInjectEntry() =
        SettingsEntryBuilder.createInject(owner = SettingsPage.create(name)).setIsAllowSearch(false)

    override fun buildEntry(arguments: Bundle?): List<SettingsEntry> {
        val owner = SettingsPage.create(name, parameter, arguments)
        return listOf(
            PictureInPictureListProvider.buildInjectEntry().setLink(fromPage = owner).build(),
            InstallUnknownAppsListProvider.buildInjectEntry().setLink(fromPage = owner).build(),
        )
    }
}

@Composable
private fun SpecialAppAccessPage() {
    RegularScaffold(title = stringResource(R.string.special_access)) {
        PictureInPictureListProvider.EntryItem()
        InstallUnknownAppsListProvider.EntryItem()
    }
}