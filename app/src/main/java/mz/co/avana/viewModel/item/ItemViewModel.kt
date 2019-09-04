package mz.co.avana.viewModel.item

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mz.co.avana.database.FirebaseConfig
import mz.co.avana.model.Category
import mz.co.avana.model.Item
import mz.co.avana.repository.user.UserRepository
import mz.co.avana.utils.Constants

@Suppress("UNCHECKED_CAST")
class ItemViewModel : ViewModel() {

    val itemLiveDataList: MutableLiveData<List<Item>> = MutableLiveData()
    val itemLiveData: MutableLiveData<Item> = MutableLiveData()

    fun itensLowers() {
        FirebaseConfig.firebaseFirestore().collection(Constants.ITEM)
            .get().addOnCompleteListener { querySnapShots ->
                if (querySnapShots.isSuccessful) {
                    val itemList: MutableList<Item> = mutableListOf()
                    for (result in querySnapShots.result!!) {

                        val category = result["category"] as Map<Any, String>
                        val item = Item(
                            name = result["name"] as String,
                            location = result["location"] as String,
                            description = result["description"] as String,
                            currentPrice = result["currentPrice"] as Double,
                            normalPrice = result["normalPrice"] as Double,

                            category = Category(
                                image = category["image"] as String,
                                name = category["name"] as String
                            ),
                            images = result["images"] as ArrayList<String>,
                            date = result["date"] as Long,
                            userID = result["userID"] as String,
                            itemId = result.id,
                            store = result["store"] as String
                        )
                        itemList.add(item)
                    }
                    itemLiveDataList.value = itemList
                }
            }
    }

    fun getSpecificItem(itemID: String) {
        FirebaseConfig.firebaseFirestore().collection(Constants.ITEM).document(itemID)
            .get().addOnCompleteListener { documentSnapshot ->
                val item = Item(
                    name = documentSnapshot.result!!["name"] as String,
                    location = documentSnapshot.result!!["location"] as String,
                    description = documentSnapshot.result!!["description"] as String,
                    currentPrice = documentSnapshot.result!!["currentPrice"] as Double,
                    normalPrice = documentSnapshot.result!!["normalPrice"] as Double,
                    category = Category(image = "hello", name = documentSnapshot.result!!["category"] as String),
                    images = documentSnapshot.result!!["images"] as ArrayList<String>,
                    date = documentSnapshot.result!!["date"] as Long,
                    userID = documentSnapshot.result!!["userID"] as String,
                    itemId = documentSnapshot.result!!.id,
                    store = documentSnapshot.result!!["store"] as String
                )
                itemLiveData.value = item
            }

    }

    fun itemsUser() {
        FirebaseConfig.firebaseFirestore().collection(Constants.ITEM)
            .whereEqualTo("userID", UserRepository.user())
            .get().addOnCompleteListener { querySnapShots ->
                if (querySnapShots.isSuccessful) {
                    val itemList: MutableList<Item> = mutableListOf()
                    for (result in querySnapShots.result!!) {
                        val category = result["category"] as Map<Any, String>
                        val item = Item(
                            name = result["name"] as String,
                            location = result["location"] as String,
                            description = result["description"] as String,
                            currentPrice = result["currentPrice"] as Double,
                            normalPrice = result["normalPrice"] as Double,

                            category = Category(
                                image = category["image"] as String,
                                name = category["name"] as String
                            ),
                            images = result["images"] as ArrayList<String>,
                            date = result["date"] as Long,
                            userID = result["userID"] as String,
                            itemId = result.id,
                            store = result["store"] as String
                        )
                        itemList.add(item)
                    }
                    itemLiveDataList.value = itemList
                }
            }
    }

    fun itemNear(location: String) {
        FirebaseConfig.firebaseFirestore().collection(Constants.ITEM)
            .whereEqualTo("location", location)
            .get().addOnCompleteListener { querySnapShots ->
                if (querySnapShots.isSuccessful) {
                    val itemList: MutableList<Item> = mutableListOf()
                    for (result in querySnapShots.result!!) {
                        val category = result["category"] as Map<Any, String>
                        val item = Item(
                            name = result["name"] as String,
                            location = result["location"] as String,
                            description = result["description"] as String,
                            currentPrice = result["currentPrice"] as Double,
                            normalPrice = result["normalPrice"] as Double,

                            category = Category(
                                image = category["image"] as String,
                                name = category["name"] as String
                            ),
                            images = result["images"] as ArrayList<String>,
                            date = result["date"] as Long,
                            userID = result["userID"] as String,
                            itemId = result.id,
                            store = result["store"] as String
                        )
                        itemList.add(item)
                    }
                    itemLiveDataList.value = itemList
                }
            }
    }
}