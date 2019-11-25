package mz.co.avana.repository.item

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask
import mz.co.avana.R
import mz.co.avana.callbacks.MessageCallback
import mz.co.avana.callbacks.OnUploadImageCallback
import mz.co.avana.database.FirebaseConfig
import mz.co.avana.model.Images
import mz.co.avana.model.Item
import mz.co.avana.repository.user.UserRepository
import mz.co.avana.utils.Constants
import mz.co.avana.utils.Utils
import java.util.*

class ItemRepository(val context: Context?, val item: Item?, val images: ArrayList<Uri>?, val activity: Activity?) {

    constructor(context: Context) : this(context = null, item = null, images = null, activity = null)

    var imagesLink = arrayListOf<String>()
    val reference = FirebaseConfig.firebaseFirestore().collection(Constants.ITEM)

    fun shareItemDetails(messageCallback: MessageCallback) {
        val map = HashMap<String, Any>()
        val imgLink = HashMap<String, Any>()
        var messageError = String()
        var messageSuccess = String()

        val posts = Utils.readPreference(
            Constants.POSTS,
            Constants.POSTS,
            activity!!
        )
        reference.add(item!!).addOnSuccessListener { documentReference ->
            FirebaseConfig.firebaseFirestore().collection(Constants.USER)
                .document(UserRepository.user()).update("posts", posts.toLong() + 1).addOnSuccessListener {

                    Utils.writeSharedPreferences(
                        Constants.POSTS,
                        (posts.toLong() + 1).toString(), Constants.POSTS,
                        activity
                    )

                    uploadImage(UserRepository.user(), object : OnUploadImageCallback {
                        override fun onUpload(images: Images) {
                            val array = arrayOf(
                                images.imageOne.toString(),
                                images.imageTwo.toString(),
                                images.imageThree.toString()
                            )
                            map["images"] = listOf(*array)
                            val imgBackup = arrayOf(
                                imagesLink[0],
                                imagesLink[1],
                                imagesLink[2]
                            )
                            imgLink["imagesLink"] = listOf(*imgBackup)
                            reference.document(documentReference.id).update(map).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    reference.document(documentReference.id).update(imgLink).addOnCompleteListener {
                                        messageSuccess = "Item shared successful"
                                    }
                                } else {
                                    reference.document(documentReference.id).delete()
                                    messageError = "Failed to share"
                                }
                            }.addOnFailureListener {
                                reference.document(documentReference.id).delete()
                            }
                            if (messageSuccess.isNotEmpty() && messageSuccess != "" && messageSuccess.isNotBlank()) {
                                messageCallback.onSuccess(messageSuccess)
                            } else if (messageError.isNotEmpty() && messageError != "" && messageError.isNotBlank()) {
                                messageCallback.onError(messageError)
                            }
                        }
                    })
                }
        }.addOnFailureListener {
        }
    }

    fun updateItemDetails(messageCallback: MessageCallback) {
        val map = HashMap<String, Any>()
        val imgLink = HashMap<String, Any>()
        var messageError = String()
        var messageSuccess = String()

        val posts = Utils.readPreference(
            Constants.POSTS,
            Constants.POSTS,
            activity!!
        )
        val itemData = mapOf(
            "name" to item!!.name,
            "location" to item.location,
            "description" to item.description,
            "normalPrice" to item.normalPrice,
            "currentPrice" to item.currentPrice,
            "categories" to item.categories,
            "date" to item.date,
            "store" to item.store
        )

        item.itemId?.let { itemId->
            reference.document(itemId).update(itemData).addOnSuccessListener {
                FirebaseConfig.firebaseFirestore().collection(Constants.USER)
                    .document(UserRepository.user()).update("posts", posts.toLong() + 1).addOnSuccessListener {

                        Utils.writeSharedPreferences(
                            Constants.POSTS,
                            (posts.toLong() + 1).toString(), Constants.POSTS,
                            activity )

                        updateItemImage(object : OnUploadImageCallback {
                            override fun onUpload(images: Images) {
                                val array = arrayOf(
                                    images.imageOne.toString(),
                                    images.imageTwo.toString(),
                                    images.imageThree.toString()
                                )
                                map["images"] = listOf(*array)
                                val imgBackup = arrayOf(
                                    imagesLink[0],
                                    imagesLink[1],
                                    imagesLink[2]
                                )
                                imgLink["imagesLink"] = listOf(*imgBackup)
                                reference.document(itemId).update(map).addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        reference.document(itemId).update(imgLink).addOnCompleteListener {
                                            messageSuccess = "Item shared successful"
                                        }
                                    } else {
                                        reference.document(itemId).delete()
                                        messageError = "Failed to share"
                                    }
                                }.addOnFailureListener {
                                    reference.document(itemId).delete()
                                }
                                if (messageSuccess.isNotEmpty() && messageSuccess != "" && messageSuccess.isNotBlank()) {
                                    messageCallback.onSuccess(messageSuccess)
                                } else if (messageError.isNotEmpty() && messageError != "" && messageError.isNotBlank()) {
                                    messageCallback.onError(messageError)
                                }
                            }
                        })
                    }
            }.addOnFailureListener {
            }
        }
    }

    private fun uploadImage(userID: String, onUploadImageCallback: OnUploadImageCallback) {
        var counter = 0
        val img = Images()
        var singleImage: String
        for (image in images!!) {
            if (image.toString().isNotEmpty()) {
                singleImage = userID + "/" + UUID.randomUUID().toString()
                imagesLink.add("items_images/$singleImage")
                val ref = FirebaseConfig.firebaseStorage().reference
                    .child("items_images/").child(singleImage)

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
                    }
                }.addOnFailureListener {
                }
            } else {
                Toast.makeText(context, context!!.getString(R.string.please_upload_photo), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateItemImage(onUploadImageCallback: OnUploadImageCallback) {
        var counter = 0
        val img = Images()
        var singleImage: String
        reference.document(item!!.itemId!!).get().addOnSuccessListener {
            val oldImages = it.get("imagesLink") as ArrayList<String>
            var i = 0
            for (oldImage in oldImages) {
                val ref = FirebaseConfig.firebaseStorage().reference.child(oldImage)

                ref.delete().addOnSuccessListener {
                    i++
                    if (i==3) {
                        for (image in images!!) {
                            if (image.toString().isNotEmpty()) {
                                singleImage = item.userID + "/" + UUID.randomUUID().toString()
                                imagesLink.add("items_images/$singleImage")
                                val refer = FirebaseConfig.firebaseStorage().reference
                                    .child("items_images/").child(singleImage)

                                val uploadTask = refer.putFile(image)
                                uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                                    if (!task.isSuccessful) {
                                        task.exception?.let {ex->
                                            throw ex
                                        }
                                    }
                                    return@Continuation refer.downloadUrl
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
                                    }
                                }.addOnFailureListener {
                                }
                            } else {
                                Toast.makeText(context, context!!.getString(R.string.please_upload_photo), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }

    }


    fun deleteItem(itemId: String, messageCallback: MessageCallback) {
        reference.document(itemId).get().addOnSuccessListener {
            val oldImages = it.get("imagesLink") as ArrayList<String>
            var i = 0
            for (oldImage in oldImages) {
                val ref = FirebaseConfig.firebaseStorage().reference.child(oldImage)
                val refDataBaseLikes = FirebaseConfig.firebaseDatabase()
                    .child(Constants.LIKES)
                val refDataBaseComment = FirebaseConfig.firebaseDatabase()
                    .child(Constants.COMMENTS)
                ref.delete().addOnSuccessListener {
                    i++
                    if (i==3) {
                        reference.document(itemId).delete().addOnSuccessListener {
                            refDataBaseLikes.child(itemId).removeValue()
                            refDataBaseComment.child(itemId).removeValue()
                            messageCallback.onSuccess("Item was deleted successful")
                        }.addOnFailureListener {
                            messageCallback.onError("Error while delete item")
                        }
                    }
                }
            }
        }

    }
}