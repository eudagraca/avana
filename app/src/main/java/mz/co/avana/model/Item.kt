package mz.co.avana.model

import android.os.Parcel
import android.os.Parcelable


class Item(
        var name: String,
        var location: String,
        var description: String,
        var normalPrice: Double,
        var currentPrice: Double,
        var category: Category,
        var date: Long,
        var images: ArrayList<String>?,
        var store: String,
        var userID: String,
        var itemId: String?) : Parcelable {

    constructor(name: String,
                location: String,
                description: String,
                normalPrice: Double,
                currentPrice: Double,
                category: Category,
                date: Long,
                images: ArrayList<String>,
                store: String,
                userID: String) : this(
            name = name, location = location, description = description,
            normalPrice = normalPrice, currentPrice = currentPrice, category = category, date = date, images = images,
            store = store, userID = userID, itemId = null)

    constructor(name: String,
                location: String,
                description: String,
                normalPrice: Double,
                currentPrice: Double,
                category: Category,
                date: Long,
                store: String,
                userID: String) : this(
            name = name, location = location, description = description,
            normalPrice = normalPrice, currentPrice = currentPrice, category = category, date = date, images = null,
            store = store, userID = userID, itemId = null)

    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readParcelable(Category::class.java.classLoader)!!,
            parcel.readLong(),
            parcel.createStringArrayList()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(location)
        parcel.writeString(description)
        parcel.writeDouble(normalPrice)
        parcel.writeDouble(currentPrice)
        parcel.writeParcelable(category, flags)
        parcel.writeLong(date)
        parcel.writeStringList(images)
        parcel.writeString(store)
        parcel.writeString(userID)
        parcel.writeString(itemId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Item> {
        const val KEY_IMAGE = "image_key"

        override fun createFromParcel(parcel: Parcel): Item {
            return Item(parcel)
        }

        override fun newArray(size: Int): Array<Item?> {
            return arrayOfNulls(size)
        }
    }

}