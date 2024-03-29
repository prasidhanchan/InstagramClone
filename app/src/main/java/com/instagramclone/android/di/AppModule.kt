package com.instagramclone.android.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.instagramclone.firebase.repository.AuthRepositoryImpl
import com.instagramclone.firebase.repository.HomeRepositoryImpl
import com.instagramclone.firebase.repository.ProfileRepositoryImpl
import com.instagramclone.firebase.repository.UploadContentRepositoryImpl
import com.instagramclone.post.ContentResolver
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
}