package mz.co.avana.model

import android.os.Parcel
import android.os.Parcelable

class Item(
    var name: String,
    var location: String,
    var description: String,
    var normalPrice: Double,
    var currentPrice: Double,
    var categories: String,
    var date: Long,
    var images: ArrayList<String>?,
    var store: String,
    var userID: String,
    var itemId: String?,
    var likes: Long,
    var percent: Long,
    var createdAt: Long
) : Parcelable {

    constructor(
        name: String,
        location: String,
        description: String,
        normalPrice: Double,
        currentPrice: Double,
        categories: String,
        date: Long,
        images: ArrayList<String>,
        store: String,
        userID: String
    ) : this(
        name = name,
        location = location,
        description = description,
        normalPrice = normalPrice,
        currentPrice = currentPrice,
        categories = categories,
        date = date,
        images = images,
        store = store,
        userID = userID,
        likes = 0,
        itemId = null,
        percent = (((currentPrice - normalPrice) / normalPrice) * 100).toLong(),
        createdAt = System.currentTimeMillis()
    )

    constructor(
        name: String,
        location: String,
        description: String,
        normalPrice: Double,
        currentPrice: Double,
        categories: String,
        date: Long,
        store: String,
        userID: String
    ) : this(
        name = name,
        location = location,
        description = description,
        normalPrice = normalPrice,
        currentPrice = currentPrice,
        categories = categories,
        date = date,
        images = null,
        store = store,
        userID = userID,
        likes = 0,
        itemId = null,
        percent = (((currentPrice - normalPrice) / normalPrice) * 100).toLong(),
        createdAt = System.currentTimeMillis()
    )

    constructor(
        name: String,
        location: String,
        description: String,
        normalPrice: Double,
        currentPrice: Double,
        categories: String,
        date: Long,
        store: String,
        userID: String,
        itemId: String
    ) : this(
        name = name,
        location = location,
        description = description,
        normalPrice = normalPrice,
        currentPrice = currentPrice,
        categories = categories,
        date = date,
        images = null,
        store = store,
        userID = userID,
        likes = 0,
        itemId = itemId,
        percent = (((currentPrice - normalPrice) / normalPrice) * 100).toLong(),
        createdAt = System.currentTimeMillis()
    )

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString()!!,
        parcel.readLong(),
        parcel.createStringArrayList()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(location)
        parcel.writeString(description)
        parcel.writeDouble(normalPrice)
        parcel.writeDouble(currentPrice)
        parcel.writeString(categories)
        parcel.writeLong(date)
        parcel.writeStringList(images)
        parcel.writeString(store)
        parcel.writeString(userID)
        parcel.writeString(itemId)
        parcel.writeLong(likes)
        parcel.writeLong(percent)
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