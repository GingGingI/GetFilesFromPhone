package com.example.ginggingi.getfiles.models

import android.net.Uri

class MediaModels {
    val VideoUri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI//비디오URI
    val ImageUri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI//이미지URI
    val AudioUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI//오디오URI
}