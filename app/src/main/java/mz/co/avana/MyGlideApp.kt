package mz.co.avana

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class MyGlideApp : AppGlideModule() {
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

}