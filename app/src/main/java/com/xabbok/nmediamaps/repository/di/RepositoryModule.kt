package com.xabbok.nmediamaps.repository.di

import com.xabbok.nmediamaps.repository.ObjectsRepository
import com.xabbok.nmediamaps.repository.ObjectsRepositorySharedPrefsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {
    @Singleton
    @Binds
    fun bindsPostRepository(impl: ObjectsRepositorySharedPrefsImpl) : ObjectsRepository
}