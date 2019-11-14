package mz.co.avana.viewModel.comment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mz.co.avana.database.FirebaseConfig
import mz.co.avana.model.Comment
import mz.co.avana.model.Item
import mz.co.avana.utils.Constants

class ComentViewModel: ViewModel(){

    val commentLiveDataList: MutableLiveData<List<Comment>> = MutableLiveData()
    
    fun comments(itemId: String){
        FirebaseConfig.firebaseFirestore().collection(Constants.ITEM)
                .document(itemId).collection(Constants.COMMENTS)
                .get().addOnCompleteListener{documents->
                    val commentList: MutableList<Comment> = mutableListOf()
                    if (documents.isSuccessful){

                        for (document in documents.result!!){
                            val comment = Comment(
                                    userID = document["userID"] as String,
                                    text = document["text"] as String,
                                    date = document["date"] as String
                            )

                            commentList.add(comment)
                        }
                        commentLiveDataList.value = commentList
                    }
                }
    }
}