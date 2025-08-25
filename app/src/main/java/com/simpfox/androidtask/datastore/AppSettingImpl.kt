package com.simpfox.androidtask.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class AppSettingImpl(private val context: Context) : AppSetting {

    private val Context.dataStoreAppSetting : DataStore<Preferences>
        by preferencesDataStore(name = "app-setting-pref")

    // emit lai data khi co su thay doi
    // map chuyen doi tu Preferences -> AppSettingData
    override val appSettingFlow: Flow<AppSettingData>
        get() = context.dataStoreAppSetting.data.map { pref ->
            AppSettingData(
                isNotificationOn = pref[AppSettingDataStoreKey.IS_NOTIFICATION_ON] ?: false
            )
        }


    override suspend fun setIsNotificationOn(isNotificationOn: Boolean) = withContext(Dispatchers.IO) {
        context.dataStoreAppSetting.edit { pref ->
            pref[AppSettingDataStoreKey.IS_NOTIFICATION_ON] = isNotificationOn
        }
        Unit
    }

    override suspend fun getIsNotificationOn() : Boolean = withContext(Dispatchers.IO) {
//        context.dataStoreAppSetting.data.map { pref ->
//            pref[AppSettingDataStoreKey.IS_NOTIFICATION_ON] ?: false
//        }.first()

        appSettingFlow.first().isNotificationOn // Lay gia tri  gan nhat sau khi emit

    }
}