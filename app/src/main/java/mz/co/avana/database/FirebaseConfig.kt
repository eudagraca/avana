package mz.co.avana.database

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

object FirebaseConfig {

    fun instanceOfAuth(): FirebaseAuth {
        FirebaseApp.getInstance()
        return FirebaseAuth.getInstance()
    }

    fun firebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    fun firebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

}