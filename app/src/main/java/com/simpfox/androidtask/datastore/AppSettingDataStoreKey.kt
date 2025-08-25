package com.simpfox.androidtask.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey

object AppSettingDataStoreKey {
    val IS_NOTIFICATION_ON = booleanPreferencesKey("is_notification_on")
}