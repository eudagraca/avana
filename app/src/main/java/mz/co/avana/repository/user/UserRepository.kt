package mz.co.avana.repository.user

import android.content.Context
import mz.co.avana.R
import mz.co.avana.database.FirebaseConfig
import mz.co.avana.model.User
import mz.co.avana.utils.Constants
import mz.co.avana.utils.MessageCallback

class UserRepository(val context: Context, val user: User) {


    fun signInWithEmailAndPassword(messageCallback: MessageCallback) {
        FirebaseConfig.instanceOfAuth().signInWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        messageCallback.onSuccess(context.getString(R.string.welcome))
                    } else {
                        val error = task.exception
                        error!!.message?.let { messageCallback.onError(it) }
                    }
                }
    }

    fun createUserWithEmailPassword(messageCallback: MessageCallback) {
        FirebaseConfig.instanceOfAuth().createUserWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        FirebaseConfig.firebaseFirestore().collection(Constants.USER)
                                .document(FirebaseConfig.instanceOfAuth().currentUser!!.uid).set(user)
                                .addOnSuccessListener {
                                    messageCallback.onSuccess(context.getString(R.string.user_created))
                                }
                    } else {
                        messageCallback.onError(context.getString(R.string.error_creating_user))
                    }
                }
    }

    fun updateNamesOfUser(messageCallback: MessageCallback) {
        val updates = hashMapOf<String, Any>(
                "name" to user.name,
                "surname" to user.surname!!
        )

        FirebaseConfig.firebaseFirestore().collection(Constants.USER)
                .document(user()).update(updates).addOnSuccessListener {
                    messageCallback.onSuccess(context.getString(R.string.user_updated))
                }.addOnFailureListener {
                    messageCallback.onError(context.getString(R.string.error_while_update_user))
                }
    }

    fun updateLocation(messageCallback: MessageCallback) {
        val updates = hashMapOf<String, Any>(
                "location" to user.location!!
        )

        FirebaseConfig.firebaseFirestore().collection(Constants.USER)
                .document(user()).update(updates).addOnSuccessListener {
                    messageCallback.onSuccess(context.getString(R.string.location_updated))
                }.addOnFailureListener {
                    messageCallback.onError(context.getString(R.string.error_while_update_location))
                }
    }


    companion object {
        fun isLogged(): Boolean {
            return FirebaseConfig.instanceOfAuth().currentUser != null
        }

        fun user() = FirebaseConfig.instanceOfAuth().currentUser!!.uid
    }
}