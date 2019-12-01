package mz.co.avana.viewModel.comment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import mz.co.avana.database.FirebaseConfig
import mz.co.avana.model.Comment
import mz.co.avana.utils.Constants

class CommentViewModel : ViewModel() {
    val commentLiveDataList: MutableLiveData<List<Comment>> = MutableLiveData()

    fun comments(itemId: String) {
        val commentList: MutableList<Comment> = mutableListOf()
        FirebaseConfig.firebaseDatabase().child(Constants.COMMENTS).child(itemId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(commentSnapshot: DataSnapshot) {
                    if (commentSnapshot.exists()) {
                        for (snap in commentSnapshot.children) {
                            val comment = Comment(
                                userID = snap.child("userID").value as String,
                                date = snap.child("date").value as Long,
                                text = snap.child("text").value as String,
                                user = snap.child("user").value as String
                            )
                            commentList.add(comment)
                        }
                        commentLiveDataList.value = commentList
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                }
            })
    }
}