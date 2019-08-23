package mz.co.avana.model

import com.google.firebase.firestore.Exclude

class User() {

    var name: String = ""
    @Exclude
    var email: String = ""
    @Exclude
    var password: String = ""
    var surname: String? = null
    var location: String? = null

    constructor(name: String, email: String, password: String, surname: String) : this() {
        this.email = email
        this.name = name
        this.password = password
        this.surname = surname
    }

    constructor(name: String, surname: String, location: String) : this() {
        this.name = name
        this.surname = surname
        this.location = location
    }

    constructor(name: String, surname: String) : this() {
        this.name = name
        this.surname = surname
    }


}