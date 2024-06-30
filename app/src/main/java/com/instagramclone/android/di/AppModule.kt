package com.instagramclone.android.di

import android.content.Context
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.instagramclone.remote.repository.AuthRepositoryImpl
import com.instagramclone.remote.repository.HomeRepositoryImpl
import com.instagramclone.remote.repository.ProfileRepositoryImpl
import com.instagramclone.remote.repository.UploadContentRepositoryImpl
import com.instagramclone.upload.util.ContentResolver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideHomeRepository() = HomeRepositoryImpl(
        dbPosts = FirebaseDatabase.getInstance().getReference("Posts")
    )

    @Singleton
    @Provides
    fun provideProfileRepository() =
        ProfileRepositoryImpl(
            queryUser = FirebaseFirestore.getInstance().collection("Users"),
            dbPostRef = FirebaseDatabase.getInstance().getReference("Posts")
        )

    @Singleton
    @Provides
    fun provideShareContentRepository() = UploadContentRepositoryImpl()

    @Singleton
    @Provides
    fun provideContentResolver(@ApplicationContext context: Context) =
        ContentResolver(context = context)

    @UnstableApi
    @Singleton
    @Provides
    fun provideExoplayer(@ApplicationContext context: Context): ExoPlayer {
        return ExoPlayer.Builder(context)
            .setHandleAudioBecomingNoisy(true)
            .setDeviceVolumeControlEnabled(true)
            .build()
    }
}