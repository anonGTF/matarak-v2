package com.galih.matarakv2.di

import com.galih.matarakv2.data.firebase.FirebaseUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseUtils(): FirebaseUtils = FirebaseUtils
}