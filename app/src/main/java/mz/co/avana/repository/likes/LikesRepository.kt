package mz.co.avana.repository.likes

import android.content.Context
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import mz.co.avana.callbacks.LikedCallback
import mz.co.avana.database.FirebaseConfig
import mz.co.avana.model.Likes
import mz.co.avana.repository.user.UserRepository
import mz.co.avana.utils.Constants

class LikesRepository() {
    private lateinit var likes: Likes
    var setLike = true
    lateinit var context: Context
    private val referenceItem = FirebaseConfig.firebaseFirestore()
        .collection(Constants.ITEM)
    private val databaseReferenceLikes = FirebaseConfig.firebaseDatabase()
        .child(Constants.LIKES)

    constructor(likes: Likes, context: Context) : this() {
        this.likes = likes
        this.context = context
    }

    fun setLike(itemID: String, likedCallback: LikedCallback) {
        databaseReferenceLikes.keepSynced(true)
        databaseReferenceLikes.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (setLike) {
                    setLike = if (snapshot.child(itemID).hasChild(UserRepository.user())) {
                        databaseReferenceLikes.child(itemID).child(UserRepository.user()).removeValue()
                        likedCallback.likes(false)
                        referenceItem.document(itemID).update("likes", snapshot.childrenCount-1).addOnSuccessListener {
                        }
                        false
                    } else {
                        databaseReferenceLikes.child(itemID).child(UserRepository.user()).setValue(true)
                            .addOnSuccessListener {
                                referenceItem.document(itemID).update("likes", snapshot.childrenCount+1).addOnSuccessListener {
                                }
                                likedCallback.likes(true)
                            }
                        false
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    fun likedOrNot(itemID: String, likedCallback: LikedCallback) {
        databaseReferenceLikes.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (UserRepository.isLogged()) {
                    if (snapshot.child(itemID).hasChild(UserRepository.user())) {
                        likedCallback.likes(true)
                    } else {
                        likedCallback.likes(false)
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

}