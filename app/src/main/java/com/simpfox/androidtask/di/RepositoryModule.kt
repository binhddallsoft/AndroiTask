package com.simpfox.androidtask.di

import com.simpfox.androidtask.database.dao.TaskDAO
import com.simpfox.androidtask.repository.TaskRepo
import com.simpfox.androidtask.repository.TaskRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideTaskRepo(taskDAO: TaskDAO) : TaskRepo {
        return TaskRepoImpl(taskDAO)
    }
}