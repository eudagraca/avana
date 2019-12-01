package mz.co.avana.database

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage

object FirebaseConfig {

    fun instanceOfAuth(): FirebaseAuth {
        FirebaseApp.getInstance()
        return FirebaseAuth.getInstance()
    }

    fun firebaseFirestore(): FirebaseFirestore{
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()
        val db = FirebaseFirestore.getInstance()
        db.firestoreSettings = settings
        return db
    }

    fun firebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    fun firebaseDatabase(): DatabaseReference = FirebaseDatabase.getInstance().reference

}