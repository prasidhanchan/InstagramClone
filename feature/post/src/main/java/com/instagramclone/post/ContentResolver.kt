package com.instagramclone.post

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.instagramclone.util.models.Image
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class ContentResolver @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val projection = arrayOf(
        MediaStore.Images.ImageColumns._ID,
        MediaStore.MediaColumns.DISPLAY_NAME,
        MediaStore.Images.ImageColumns.DATA
    )
    private val selectionClaus = null
    private val selectionArg = null
    private val sortOrder = "${MediaStore.Images.Media.DATE_MODIFIED} DESC"

    /**
     * Function to get all the images from local storage
     * @return returns List of [Image]
     */
    fun getImages(): Flow<List<Image>> {
        val imageList = MutableStateFlow(mutableListOf<Image>())
        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selectionClaus,
            selectionArg,
            sortOrder
        )

        cursor?.use { mCursor ->
            val idColumn = mCursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID)
            val nameColumn = mCursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME)

            while (mCursor.moveToNext()) {
                val id = mCursor.getString(idColumn)
                val name = mCursor.getString(nameColumn)
                val imageUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                imageList.value.add(
                    Image(
                        id = id,
                        name = name,
                        data = imageUri
                    )
                )
            }
        }
        cursor?.close()
        return imageList.asStateFlow()
    }
}