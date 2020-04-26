package mz.co.avana.repository.comment

import android.content.Context
import mz.co.avana.R
import mz.co.avana.callbacks.MessageCallback
import mz.co.avana.database.FirebaseConfig
import mz.co.avana.model.Comment
import mz.co.avana.utils.Constants

class CommentRepository(private val comment: Comment, val context: Context) {
    private val databaseReferenceLikes = FirebaseConfig.firebaseDatabase()
        .child(Constants.COMMENTS)

    fun commentAnItem(itemId: String, messageCallback: MessageCallback) {
        val ref = databaseReferenceLikes.child(itemId).push()
        ref.setValue(comment).addOnSuccessListener {
            messageCallback.onSuccess(context.resources.getString(R.string.comment_sent))
        }.addOnFailureListener {
            messageCallback.onError(it.message.toString())
        }
    }
}