package mz.co.avana.repository.comment

import android.content.Context
import mz.co.avana.R
import mz.co.avana.database.FirebaseConfig
import mz.co.avana.model.Comment
import mz.co.avana.utils.Constants
import mz.co.avana.utils.MessageCallback

class CommentRepository (val comment: Comment, val context: Context){

    fun commentAnItem(itemId: String, messageCallback: MessageCallback){
        FirebaseConfig.firebaseFirestore().collection(Constants.ITEM)
                .document(itemId).collection(Constants.COMMENTS)
                .add(comment).addOnSuccessListener {
                    messageCallback.onSuccess(context.resources.getString(R.string.comment_sended))
                }.addOnFailureListener {
                    messageCallback.onError(context.resources.getString(R.string.unable_to_comment))
                }
    }

}