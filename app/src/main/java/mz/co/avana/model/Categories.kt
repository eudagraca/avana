package mz.co.avana.model

import android.os.Parcel
import android.os.Parcelable

data class Categories(val image: Int?, val name: String, val nameOnDatabase: String) : Parcelable {

    constructor(name: String, nameOnDatabase: String): this(
        image = null,
        name = name,
        nameOnDatabase = nameOnDatabase
    )


    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readString()!!
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeInt(image!!)
        dest.writeString(name)
        dest.writeString(nameOnDatabase)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Categories> {
        override fun createFromParcel(parcel: Parcel): Categories {
            return Categories(parcel)
        }

        override fun newArray(size: Int): Array<Categories?> {
            return arrayOfNulls(size)
        }
    }
}
