package mz.co.avana.viewModel.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mz.co.avana.database.FirebaseConfig
import mz.co.avana.model.User
import mz.co.avana.utils.Constants

class UserViewModel : ViewModel() {
    val userLiveData: MutableLiveData<User> = MutableLiveData()
    private val uid = FirebaseConfig.instanceOfAuth().currentUser!!.uid
    fun userData() {
        FirebaseConfig.firebaseFirestore().collection(Constants.USER).document(uid)
                .get().addOnSuccessListener { documentSnapshot ->
                    val userData = User(
                            name = documentSnapshot.get("name").toString(),
                            surname = documentSnapshot.get("surname").toString(),
                            location = documentSnapshot["location"].toString()
                    )
                    userLiveData.value = userData
                }.addOnFailureListener {

                }
    }

    fun specificUserData(userID: String) {
        FirebaseConfig.firebaseFirestore().collection(Constants.USER).document(userID)
                .get().addOnSuccessListener { documentSnapshot ->
                    val userData = User(
                            name = documentSnapshot.get("name").toString(),
                            surname = documentSnapshot.get("surname").toString(),
                            location = documentSnapshot["location"].toString()
                    )
                    userLiveData.value = userData
                }.addOnFailureListener {

                }
    }


}