package mz.co.avana.callbacks

import android.net.Uri

interface OnUploadSingleImageCallback {

    fun onUpload(images: Uri)
}