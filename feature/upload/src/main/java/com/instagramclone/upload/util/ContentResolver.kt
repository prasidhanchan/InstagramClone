package com.instagramclone.upload.util

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import com.instagramclone.ui.R
import com.instagramclone.util.models.Media
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ContentResolver @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val projection = arrayOf(
        MediaStore.MediaColumns._ID,
        MediaStore.MediaColumns.DISPLAY_NAME,
        MediaStore.MediaColumns.DATA,
        MediaStore.MediaColumns.DURATION,
        MediaStore.MediaColumns.DATE_ADDED,
        MediaStore.MediaColumns.MIME_TYPE
    )
    private val selectionClaus = null
    private val selectionArg = null
    private val sortOrder = "${MediaStore.MediaColumns.DATE_ADDED} DESC"

    /**
     * Function to get all the images and videos from local storage
     * @return returns List of [Media]
     */
    suspend fun getMedia(): Flow<List<Media>> {
        val mediaList = MutableStateFlow(mutableListOf<Media>())

        try {
            val cursorImage = context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selectionClaus,
                selectionArg,
                sortOrder
            )
            val cursorVideo = context.contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                selectionClaus,
                selectionArg,
                sortOrder
            )

            cursorImage?.use { mCursor ->
                val idColumn = mCursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID)
                val nameColumn =
                    mCursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME)
                val timeStampColumn =
                    mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
                val mimeTypeColumns = mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)

                while (mCursor.moveToNext()) {
                    val id = mCursor.getString(idColumn)
                    val name = mCursor.getString(nameColumn)
                    val timeStamp = mCursor.getLong(timeStampColumn)
                    val mimeType = mCursor.getString(mimeTypeColumns)
                    val imageUri =
                        Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                    mediaList.value.add(
                        Media(
                            id = id,
                            name = name,
                            data = imageUri,
                            duration = null,
                            timeStamp = timeStamp,
                            mimeType = mimeType
                        )
                    )
                }
            }

            cursorVideo?.use { mCursor ->
                val idColumn = mCursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns._ID)
                val nameColumn =
                    mCursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DISPLAY_NAME)
                val durationColumn =
                    mCursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION)
                val timeStampColumn =
                    mCursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DATE_ADDED)
                val mimeTypeColumns = mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)

                while (mCursor.moveToNext()) {
                    val id = mCursor.getString(idColumn)
                    val name = mCursor.getString(nameColumn)
                    val duration = mCursor.getString(durationColumn)
                    val timeStamp = mCursor.getLong(timeStampColumn)
                    val mimeType = mCursor.getString(mimeTypeColumns)
                    val videoUri =
                        Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)

                    mediaList.value.add(
                        Media(
                            id = id,
                            name = name,
                            data = videoUri,
                            duration = duration,
                            timeStamp = timeStamp,
                            mimeType = mimeType
                        )
                    )
                }
            }

            cursorImage?.close()
            cursorVideo?.close()
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    context.getString(R.string.something_went_wrong),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        mediaList.value.sortByDescending { it.timeStamp }
        return mediaList.asStateFlow()
    }

    /**
     * Function to get all the images from local storage
     * @return returns List of [Media]
     */
    suspend fun getImages(): Flow<List<Media>> {
        val imageList = MutableStateFlow(mutableListOf<Media>())
        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selectionClaus,
            selectionArg,
            sortOrder
        )

        try {
            cursor?.use { mCursor ->
                val idColumn = mCursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID)
                val nameColumn =
                    mCursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME)
                val mimeTypeColumns = mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)

                while (mCursor.moveToNext()) {
                    val id = mCursor.getString(idColumn)
                    val name = mCursor.getString(nameColumn)
                    val mimeType = mCursor.getString(mimeTypeColumns)
                    val imageUri =
                        Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                    imageList.value.add(
                        Media(
                            id = id,
                            name = name,
                            data = imageUri,
                            duration = null,
                            timeStamp = null,
                            mimeType = mimeType
                        )
                    )
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    context.getString(R.string.something_went_wrong),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        cursor?.close()
        return imageList.asStateFlow()
    }

    /**
     * Function to get all the videos from local storage
     * @return returns List of [Media]
     */
    suspend fun getVideos(): Flow<List<Media>> {
        val videoList = MutableStateFlow(mutableListOf<Media>())
        val cursor = context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            selectionClaus,
            selectionArg,
            sortOrder
        )

        try {
            cursor?.use { mCursor ->
                val idColumn = mCursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns._ID)
                val nameColumn =
                    mCursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DISPLAY_NAME)
                val durationColumn =
                    mCursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION)
                val mimeTypeColumns = mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)

                while (mCursor.moveToNext()) {
                    val id = mCursor.getString(idColumn)
                    val name = mCursor.getString(nameColumn)
                    val duration = mCursor.getString(durationColumn)
                    val mimeType = mCursor.getString(mimeTypeColumns)
                    val videoUri =
                        Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)

                    videoList.value.add(
                        Media(
                            id = id,
                            name = name,
                            data = videoUri,
                            duration = duration,
                            timeStamp = null,
                            mimeType = mimeType
                        )
                    )
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    context.getString(R.string.something_went_wrong),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        cursor?.close()
        return videoList.asStateFlow()
    }
}