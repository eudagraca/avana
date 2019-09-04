package mz.co.avana.repository.item

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask
import mz.co.avana.R
import mz.co.avana.database.FirebaseConfig
import mz.co.avana.model.Images
import mz.co.avana.model.Item
import mz.co.avana.repository.user.UserRepository
import mz.co.avana.utils.Constants
import mz.co.avana.utils.MessageCallback
import java.util.*

class ItemRepository(val context: Context, val item: Item, val images: ArrayList<Uri>) {
    var imagesLink = arrayListOf<String>()

    fun shareItemDetails(messageCallback: MessageCallback) {
        val map = HashMap<String, Any>()
        val imgLink = HashMap<String, Any>()
        val reference = FirebaseConfig.firebaseFirestore().collection(Constants.ITEM)
        reference.add(item).addOnSuccessListener { documentReference ->
            uploadImage(UserRepository.user(), object : OnUploadImageCallback {
                override fun onUpload(images: Images) {
                    val array = arrayOf(
                            images.imageOne.toString(),
                            images.imageTwo.toString(), images.imageThree.toString()
                    )
                    map["images"] = listOf(*array)

                    val imgbackup = arrayOf(
                            imagesLink[0],
                            imagesLink[1],
                            imagesLink[2]
                    )

                    imgLink["imagesLink"] = listOf(*imgbackup)
                    reference.document(documentReference.id).update(map).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            reference.document(documentReference.id).update(imgLink).addOnCompleteListener {

                                messageCallback.onSuccess(successMessage = "Item shared successfull")
                            }
                        } else {
                            reference.document(documentReference.id).delete()
                            messageCallback.onError(errorMessage = "Failed to share")
                        }
                    }.addOnFailureListener {
                        reference.document(documentReference.id).delete()
                    }
                }
            })
        }.addOnFailureListener {

        }
    }

    private fun uploadImage(userID: String, onUploadImageCallback: OnUploadImageCallback) {
        var counter = 0
        val img = Images()
        var imagelist: String
        for (image in images) {
            if (image.toString().isNotEmpty()) {
                imagelist = userID + "/" + UUID.randomUUID().toString()
                imagesLink.add("items_images/$imagelist")
                val ref = FirebaseConfig.firebaseStorage().reference
                        .child("items_images/").child(imagelist)

                val uploadTask = ref.putFile(image)
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

                        when (counter) {
                            0 -> img.imageOne = downloadUri
                            1 -> img.imageTwo = downloadUri
                            2 -> img.imageThree = downloadUri
                        }
                        onUploadImageCallback.onUpload(
                                Images(
                                        img.imageOne,
                                        img.imageTwo, img.imageThree
                                )
                        )
                        counter++
                    } else {
                        // Handle failures
                    }
                }.addOnFailureListener {

                }
            } else {
                Toast.makeText(context, context.getString(R.string.please_upload_photo), Toast.LENGTH_SHORT).show()
            }
        }
    }
}