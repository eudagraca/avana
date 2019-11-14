package mz.co.avana.model

import android.os.Parcel
import android.os.Parcelable

data class Categorie(val image: String, val name: String) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeString("image")
        dest.writeString(name)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Categorie> {
        override fun createFromParcel(parcel: Parcel): Categorie {
            return Categorie(parcel)
        }

        override fun newArray(size: Int): Array<Categorie?> {
            return arrayOfNulls(size)
        }
    }
}
