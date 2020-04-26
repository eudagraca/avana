package mz.co.avana.repository.user

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FieldValue
import com.google.firebase.storage.UploadTask
import mz.co.avana.R
import mz.co.avana.callbacks.MessageCallback
import mz.co.avana.callbacks.OnUploadSingleImageCallback
import mz.co.avana.database.FirebaseConfig
import mz.co.avana.model.User
import mz.co.avana.utils.Constants
import mz.co.avana.utils.Utils
import java.util.*

class UserRepository(val context: Context, val user: User, val activity: Activity) {


    var imageLink = ""
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

    fun updateImage(messageCallback: MessageCallback) {
        val reference = FirebaseConfig.firebaseFirestore().collection(Constants.USER)
            .document(user())
        uploadImage(object : OnUploadSingleImageCallback {
            override fun onUpload(images: Uri) {
                reference.update("image", images.toString()).addOnSuccessListener {
                    reference.update("imageLink", imageLink).addOnCompleteListener {
                        Utils.writeSharedPreferences(
                            Constants.IMAGES,
                            images.toString(), Constants.IMAGES,
                            activity
                        )
                        messageCallback.onSuccess("Photo was changed")
                    }
                }.addOnFailureListener {
                    messageCallback.onError("Won't changed")
                }
            }
        })
    }

    private fun uploadImage(onUploadImageCallback: OnUploadSingleImageCallback) {

        if (user.image.toString().isNotEmpty()) {
            val path = user() + "/" + UUID.randomUUID().toString()
            val ref = FirebaseConfig.firebaseStorage().reference
                .child("image_profile/").child(path)
            imageLink = "image_profile/$path"
            val uploadTask = ref.putFile(user.image!!)
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    onUploadImageCallback.onUpload(
                        downloadUri!!
                    )
                }
            }.addOnFailureListener {
            }
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.please_upload_photo),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun setToken(messageCallback: MessageCallback) {
        FirebaseConfig.firebaseFirestore().collection(Constants.USER)
            .document(user()).update("token", user.token).addOnCompleteListener {
                messageCallback.onSuccess(context.getString(R.string.welcome))
            }
    }

    @SuppressLint("DefaultLocale")
    fun setLocation(messageCallback: MessageCallback) {
        FirebaseConfig.firebaseFirestore().collection(Constants.USER)
            .document(user()).update("location", user.location.toLowerCase())
            .addOnCompleteListener {
                messageCallback.onSuccess(user.location)
            }
    }

    companion object {
        fun isLogged(): Boolean {
            return FirebaseConfig.instanceOfAuth().currentUser != null
        }

        fun user() = FirebaseConfig.instanceOfAuth().currentUser!!.uid
        fun email() = FirebaseConfig.instanceOfAuth().currentUser!!.email

        fun logOut() {
            FirebaseConfig.firebaseFirestore().collection(Constants.USER)
                .document(user()).update("token", FieldValue.delete()).addOnSuccessListener {
                    FirebaseConfig.instanceOfAuth().signOut()
                }
        }
    }
}