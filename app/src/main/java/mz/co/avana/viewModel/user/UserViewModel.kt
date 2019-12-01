package mz.co.avana.viewModel.user

import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mz.co.avana.database.FirebaseConfig
import mz.co.avana.model.User
import mz.co.avana.repository.user.UserRepository
import mz.co.avana.utils.Constants

class UserViewModel : ViewModel() {
    val userLiveData: MutableLiveData<User> = MutableLiveData()

    fun userData() {

        if (UserRepository.isLogged()) {
            val uid = FirebaseConfig.instanceOfAuth().currentUser!!.uid
            FirebaseConfig.firebaseFirestore().collection(Constants.USER).document(uid)
                .get().addOnSuccessListener { documentSnapshot ->
                    val userData: User = if (documentSnapshot.getString("image").equals(null)) {
                        User(
                            name = documentSnapshot.get("name").toString(),
                            surname = documentSnapshot.get("surname").toString(),
                            posts = documentSnapshot.getLong("posts")!!,
                            image = "--".toUri(),
                            email = FirebaseConfig.instanceOfAuth().currentUser!!.email!!
                        )
                    } else {
                        User(
                            name = documentSnapshot.get("name").toString(),
                            surname = documentSnapshot.get("surname").toString(),
                            posts = documentSnapshot.getLong("posts")!!,
                            image = documentSnapshot.getString("image")!!.toUri()
                        )
                    }

                    userLiveData.value = userData
                }.addOnFailureListener {
                }
        }
    }

    fun specificUser(userID: String) {
        FirebaseConfig.firebaseFirestore().collection(Constants.USER).document(userID)
            .get().addOnSuccessListener { documentSnapshot ->
                val userData = User(
                    name = documentSnapshot.get("name").toString(),
                    surname = documentSnapshot.get("surname").toString()
                )
                userLiveData.value = userData
            }.addOnFailureListener {
            }
    }
}