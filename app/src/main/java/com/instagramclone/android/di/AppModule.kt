package com.instagramclone.android.di

import com.google.firebase.firestore.FirebaseFirestore
import com.instagramclone.firebase.repository.AuthRepositoryImpl
import com.instagramclone.firebase.repository.HomeRepositoryImpl
import com.instagramclone.firebase.repository.ProfileRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideAuthRepository() = AuthRepositoryImpl()

    @Singleton
    @Provides
    fun provideHomeRepository() = HomeRepositoryImpl()

    @Singleton
    @Provides
    fun provideProfileRepository() = ProfileRepositoryImpl(query = FirebaseFirestore.getInstance().collection("Users"))
}