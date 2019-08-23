package mz.co.avana.model

import android.os.Parcel
import android.os.Parcelable

data class Category(val image: String, val name: String) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeString("image")
        dest.writeString(name)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Category> {
        override fun createFromParcel(parcel: Parcel): Category {
            return Category(parcel)
        }

        override fun newArray(size: Int): Array<Category?> {
            return arrayOfNulls(size)
        }
    }
}
