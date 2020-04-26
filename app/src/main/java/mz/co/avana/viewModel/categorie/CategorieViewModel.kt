package mz.co.avana.viewModel.categorie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mz.co.avana.model.Categories

class CategorieViewModel : ViewModel() {
    val categoriesLiveData: MutableLiveData<List<Categories>> = MutableLiveData()
//
//    fun getCategories(){
//        FirebaseConfig.firebaseFirestore().collection("categories").orderBy("title")
//            .get().addOnCompleteListener {querySnap->
//                val categoriestList: MutableList<Categories> = mutableListOf()
//                    for (doc in querySnap.result!!){
//                        val categories = Categories(
//                            image = "ree" as String,
//                            name = doc["title"] as String
//                        )
//                        categoriestList.add(categories)
//                    }
//                categoriesLiveData.value = categoriestList
//            }
//    }

}