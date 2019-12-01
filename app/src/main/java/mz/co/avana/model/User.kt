package mz.co.avana.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.Exclude

class User() : Parcelable {

    var image: Uri? = null
    var name: String = ""
    @Exclude
    var email: String = ""
    @Exclude
    var password: String = ""
    var surname: String? = null
    var posts: Long = 0
    var token: String = ""
    var location = ""

    constructor(parcel: Parcel) : this() {
        parcel.readString()!!
        parcel.readString()!!
        parcel.readString()!!
        parcel.readString()!!
        parcel.readLong()
        parcel.readParcelable<Uri>(ClassLoader.getSystemClassLoader())
    }

    constructor(name: String, surname: String) : this() {
        this.name = name
        this.surname = surname
    }

    constructor(name: String, surname: String, posts: Long) : this() {
        this.name = name
        this.surname = surname
        this.posts = posts
    }

    constructor(name: String, surname: String, posts: Long, image: Uri) : this() {
        this.name = name
        this.surname = surname
        this.posts = posts
        this.image = image
    }

    constructor(name: String, surname: String, posts: Long, image: Uri, email: String) : this() {
        this.name = name
        this.surname = surname
        this.posts = posts
        this.image = image
        this.email = email
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeString(surname)
        parcel.writeLong(posts)
        parcel.writeParcelable(image, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }


}