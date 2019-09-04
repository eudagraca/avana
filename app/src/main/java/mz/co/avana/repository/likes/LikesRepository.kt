package mz.co.avana.repository.likes

import android.content.Context
import com.google.firebase.firestore.FieldValue
import mz.co.avana.R
import mz.co.avana.database.FirebaseConfig
import mz.co.avana.model.Likes
import mz.co.avana.repository.user.UserRepository
import mz.co.avana.utils.Constants
import mz.co.avana.utils.MessageCallback

class LikesRepository(){
    private val currentUser = UserRepository.user()
    lateinit var likes:Likes
    lateinit var context: Context
    private val referenceItem = FirebaseConfig.firebaseFirestore()
        .collection(Constants.ITEM)
    private val referenceLikes = FirebaseConfig.firebaseFirestore()
        .collection(Constants.LIKES)

    constructor(likes: Likes, context: Context) : this() {
        this.likes = likes
        this.context = context
    }

    fun setLike(messageCallback: MessageCallback){
        val userId = hashMapOf(
            currentUser to true
        )
        val updates = hashMapOf<String, Any>(
            currentUser to FieldValue.delete()
        )
        val updateLike = hashMapOf<Int, Any>(
            likes.like to +1
        )


        referenceItem.document(likes.itemID).get().addOnSuccessListener{ dc->
                if (dc[currentUser]!! == true){
                    referenceItem.document(likes.itemID).set(likes.like to -1).addOnSuccessListener {
                        referenceLikes.document(likes.itemID).update(updates).addOnSuccessListener {
                            messageCallback.onSuccess(context.getString(R.string.like_remove))
                        }.addOnFailureListener {
                            messageCallback.onError(context.getString(R.string.failed))
                        }
                    }
                }else{
                    referenceItem.document(likes.itemID).update(updates).onSuccessTask {
                        referenceLikes.document(likes.itemID).set(userId).addOnSuccessListener {
                            messageCallback.onSuccess(context.getString(R.string.one_more_like))
                        }.addOnFailureListener {
                            messageCallback.onError(context.getString(R.string.failed))
                        }
                    }
                }
            }
    }

    fun likedOrNot(itemID: String, likedCallback: LikedCallback){
        var liked = false
        referenceItem.document(itemID).get().addOnSuccessListener { docSnap->
            liked = docSnap.contains(currentUser)
            }
        likedCallback.likes(liked)
    }
}