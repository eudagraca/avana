package mz.co.avana.model

import android.net.Uri

data class Images(var imageOne: Uri?, var imageTwo: Uri?, var imageThree: Uri?) {
    constructor() : this(null, null, null)
}
