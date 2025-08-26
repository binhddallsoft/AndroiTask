package com.simpfox.androidtask.di

import android.content.Context
import com.simpfox.androidtask.database.AppDatabase
import com.simpfox.androidtask.database.dao.TaskDAO
import com.simpfox.androidtask.repository.TaskRepo
import com.simpfox.androidtask.repository.TaskRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) : AppDatabase {
        return AppDatabase.invoke(context)
    }
    @Singleton
    @Provides
    fun provideTaskDao(appDatabase: AppDatabase) : TaskDAO {
        return appDatabase.taskDao()
    }
}