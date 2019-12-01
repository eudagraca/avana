package mz.co.avana.callbacks

import mz.co.avana.model.Images

interface OnUploadImageCallback {

    fun onUpload(images: Images)
}