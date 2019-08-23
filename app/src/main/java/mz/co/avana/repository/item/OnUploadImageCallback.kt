package mz.co.avana.repository.item

import mz.co.avana.model.Images

interface OnUploadImageCallback {

    fun onUpload(images: Images)
}