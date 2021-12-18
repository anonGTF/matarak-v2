package com.galih.matarakv2.di

import android.content.Context
import com.galih.matarakv2.data.firebase.FirebaseUtils
import com.galih.matarakv2.utils.Classifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseUtils(): FirebaseUtils = FirebaseUtils

    @Provides
    @Singleton
    fun provideClassifier(@ApplicationContext context: Context): Classifier
        = Classifier.getInstance(context)
}