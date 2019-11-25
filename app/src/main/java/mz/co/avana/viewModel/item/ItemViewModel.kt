package mz.co.avana.viewModel.item

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import mz.co.avana.database.FirebaseConfig
import mz.co.avana.model.Item
import mz.co.avana.repository.user.UserRepository
import mz.co.avana.utils.Constants
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


@Suppress("UNCHECKED_CAST")
class ItemViewModel : ViewModel() {

    val itemLiveDataList: MutableLiveData<List<Item>> = MutableLiveData()
    val itemLiveData: MutableLiveData<Item> = MutableLiveData()
    private var isLoading: Boolean = false

    /**
     * Retrieving all *Items*.
     * @return list of Items with minimum 20% discount off at a particular location
     */
    fun itemsLowers() {
        isLoading = true
        FirebaseConfig.firebaseFirestore().collection(Constants.ITEM)
            .whereLessThanOrEqualTo("percent", -2)
            .orderBy("percent", Query.Direction.DESCENDING)
            .get().addOnCompleteListener { querySnapShots ->
                if (querySnapShots.isSuccessful) {
                    val itemList: MutableList<Item> = mutableListOf()
                    for (result in querySnapShots.result!!) {
                        val item = Item(
                            name = result["name"] as String,
                            location = result["location"] as String,
                            description = result["description"] as String,
                            currentPrice = result["currentPrice"] as Double,
                            normalPrice = result["normalPrice"] as Double,
                            categories = result["categories"] as String,
                            images = result["images"] as ArrayList<String>,
                            date = result["date"] as Long,
                            userID = result["userID"] as String,
                            itemId = result.id,
                            store = result["store"] as String,
                            likes = result["likes"] as Long,
                            percent = result["percent"] as Long,
                            createdAt = result["createdAt"] as Long
                        )
                        itemList.add(item)
                    }
                    isLoading = false
                    Log.i("SSS", itemList.size.toString())
                    itemLiveDataList.value = itemList
                }
            }.addOnFailureListener { e ->
                Log.i("ERRO", e.toString())
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
                    categories = documentSnapshot.result!!["categories"] as String,
                    images = documentSnapshot.result!!["images"] as ArrayList<String>,
                    date = documentSnapshot.result!!["date"] as Long,
                    userID = documentSnapshot.result!!["userID"] as String,
                    itemId = documentSnapshot.result!!.id,
                    store = documentSnapshot.result!!["store"] as String,
                    likes = documentSnapshot.result!!["likes"] as Long,
                    percent = documentSnapshot.result!!["percent"] as Long,
                    createdAt = documentSnapshot.result!!["createdAt"] as Long
                )
                itemLiveData.value = item
            }
    }

    /**
     * Retrieving all *items*.
     *
     * @return Authenticated User Item List
     */
    fun itemsUser() {
        FirebaseConfig.firebaseFirestore().collection(Constants.ITEM)
            .whereEqualTo("userID", UserRepository.user())
            .get().addOnCompleteListener { querySnapShots ->
                if (querySnapShots.isSuccessful) {
                    val itemList: MutableList<Item> = mutableListOf()
                    for (result in querySnapShots.result!!) {
                        val item = Item(
                            name = result["name"] as String,
                            location = result["location"] as String,
                            description = result["description"] as String,
                            currentPrice = result["currentPrice"] as Double,
                            normalPrice = result["normalPrice"] as Double,
                            categories = result!!["categories"] as String,
                            images = result["images"] as ArrayList<String>,
                            date = result["date"] as Long,
                            userID = result["userID"] as String,
                            itemId = result.id,
                            store = result["store"] as String,
                            likes = result["likes"] as Long,
                            percent = result["percent"] as Long,
                            createdAt = result["createdAt"] as Long
                        )
                        itemList.add(item)
                    }
                    itemLiveDataList.value = itemList
                }
            }
    }

    /**
     * Retrieving all *itens*.
     *
     * @param location the category of item.
     * @return List of similarly located items that a location equal to the parameter.
     */
    fun itemNear(location: String) {
        FirebaseConfig.firebaseFirestore().collection(Constants.ITEM).orderBy("location")
            .startAt(location).endAt(location + "\uf8ff")
            .get().addOnCompleteListener { querySnapShots ->
                if (querySnapShots.isSuccessful) {
                    val itemList: MutableList<Item> = mutableListOf()
                    for (result in querySnapShots.result!!) {
                        val item = Item(
                            name = result["name"] as String,
                            location = result["location"] as String,
                            description = result["description"] as String,
                            currentPrice = result["currentPrice"] as Double,
                            normalPrice = result["normalPrice"] as Double,
                            categories = result!!["categories"] as String,
                            images = result["images"] as ArrayList<String>,
                            date = result["date"] as Long,
                            userID = result["userID"] as String,
                            itemId = result.id,
                            store = result["store"] as String,
                            likes = result["likes"] as Long,
                            percent = result["percent"] as Long,
                            createdAt = result["createdAt"] as Long
                        )
                        itemList.add(item)
                    }
                    itemLiveDataList.value = itemList
                }
            }
    }

    /**
     * Retrieving all *itens*.
     *
     * @param name the name of item.
     * @return List of items with similar name to the parameter
     */
    fun searchItemByName(name: String) {
        FirebaseConfig.firebaseFirestore().collection(Constants.ITEM).orderBy("name")
            .startAt(name).endAt(name + "\uf8ff")
            .get().addOnCompleteListener { querySnapShots ->
                if (querySnapShots.isSuccessful) {
                    val itemList: MutableList<Item> = mutableListOf()
                    for (result in querySnapShots.result!!) {
                        val item = Item(
                            name = result["name"] as String,
                            location = result["location"] as String,
                            description = result["description"] as String,
                            currentPrice = result["currentPrice"] as Double,
                            normalPrice = result["normalPrice"] as Double,
                            categories = result!!["categories"] as String,
                            images = result["images"] as ArrayList<String>,
                            date = result["date"] as Long,
                            userID = result["userID"] as String,
                            itemId = result.id,
                            store = result["store"] as String,
                            likes = result["likes"] as Long,
                            percent = result["percent"] as Long,
                            createdAt = result["createdAt"] as Long
                        )
                        itemList.add(item)
                    }
                    itemLiveDataList.value = itemList
                }
            }
    }

    /**
     * Retrieving all *itens*.
     *
     * @param min the minimal price of item.
     * @param max the maximal price of item.
     * @return list of items with certain minimum and maximum price
     */
    fun searchItemByPrice(min: String, max: String) {
        FirebaseConfig.firebaseFirestore().collection(Constants.ITEM)
            .whereGreaterThanOrEqualTo("currentPrice", min.toDouble())
            .whereLessThanOrEqualTo("currentPrice", max.toDouble()).get()
            .addOnCompleteListener { querySnapShots ->
                if (querySnapShots.isSuccessful) {
                    val itemList: MutableList<Item> = mutableListOf()
                    for (result in querySnapShots.result!!) {
                        val item = Item(
                            name = result["name"] as String,
                            location = result["location"] as String,
                            description = result["description"] as String,
                            currentPrice = result["currentPrice"] as Double,
                            normalPrice = result["normalPrice"] as Double,
                            categories = result!!["categories"] as String,
                            images = result["images"] as ArrayList<String>,
                            date = result["date"] as Long,
                            userID = result["userID"] as String,
                            itemId = result.id,
                            store = result["store"] as String,
                            likes = result["likes"] as Long,
                            percent = result["percent"] as Long,
                            createdAt = result["createdAt"] as Long
                        )
                        itemList.add(item)
                    }
                    itemLiveDataList.value = itemList
                }
            }
    }

    /**
     * Retrieving all *itens*.
     *
     * @param min the minimal price of item.
     * @param max the maximal price of item.
     * @param name the maximal price of item.
     * @param category the maximal price of item.
     * @return list of items with certain minimum, maximum, name and category.
     */
    fun searchItemByAllParams(name: String, category: String, min: String, max: String) {
        val reference = FirebaseConfig.firebaseFirestore().collection(Constants.ITEM)
        val task1 = reference.whereEqualTo("categories", category).get()
        val task2 = reference.whereGreaterThanOrEqualTo("currentPrice", min.toDouble()).get()
        val task3 = reference.whereLessThanOrEqualTo("currentPrice", max.toDouble()).get()
        val task4 = reference.orderBy("name").startAt(name).endAt(name + "\uf8ff").get()
        val allTasks = Tasks.whenAllSuccess<QuerySnapshot>(task1, task2, task3, task4)

        allTasks.addOnSuccessListener { querySnapShots ->
            for (documentSnapShots in querySnapShots) {
                val itemList: MutableList<Item> = mutableListOf()
                for (result in documentSnapShots) {
                    if (result["name"].toString().contains(name) && result["categories"].toString().contains(
                            category
                        )
                    ) {
                        val item = Item(
                            name = result["name"] as String,
                            location = result["location"] as String,
                            description = result["description"] as String,
                            currentPrice = result["currentPrice"] as Double,
                            normalPrice = result["normalPrice"] as Double,
                            categories = result!!["categories"] as String,
                            images = result["images"] as ArrayList<String>,
                            date = result["date"] as Long,
                            userID = result["userID"] as String,
                            itemId = result.id,
                            store = result["store"] as String,
                            likes = result["likes"] as Long,
                            percent = result["percent"] as Long,
                            createdAt = result["createdAt"] as Long
                        )
                        itemList.add(item)
                    }
                }
                Log.i("CCC", itemList.size.toString())

                itemLiveDataList.value = itemList
            }
        }.addOnFailureListener {
            Log.i("ERRO", it.toString())
        }
    }

    /**
     * Retrieving all *itens*.
     *
     * @param name the name of item.
     * @param category the category of item.
     * @param min the minimal price of item.
     * @return List of similarly named items that have a minimum price and a category equal to the parameter.
     */
    fun searchItemByCategoryAndNameMin(name: String, category: String, min: String) {
        val reference = FirebaseConfig.firebaseFirestore().collection(Constants.ITEM)
        val task1 = reference.orderBy("name").startAt(name).endAt(name + "\uf8ff").get()
        val task2 = reference.whereEqualTo("categories", category).get()
        val task3 = reference.whereGreaterThanOrEqualTo("currentPrice", min.toDouble()).get()
        val allTasks = Tasks.whenAllSuccess<QuerySnapshot>(task1, task2, task3)

        allTasks.addOnSuccessListener { documentSnapShots ->
            for (querySnapShots in documentSnapShots) {
                val itemList: MutableList<Item> = mutableListOf()
                for (result in querySnapShots) {
                    if (result["name"].toString().contains(name)) {
                        val item = Item(
                            name = result["name"] as String,
                            location = result["location"] as String,
                            description = result["description"] as String,
                            currentPrice = result["currentPrice"] as Double,
                            normalPrice = result["normalPrice"] as Double,
                            categories = result!!["categories"] as String,
                            images = result["images"] as ArrayList<String>,
                            date = result["date"] as Long,
                            userID = result["userID"] as String,
                            itemId = result.id,
                            store = result["store"] as String,
                            likes = result["likes"] as Long,
                            percent = result["percent"] as Long,
                            createdAt = result["createdAt"] as Long
                        )
                        itemList.add(item)
                    }
                }
                itemLiveDataList.value = itemList
            }
        }.addOnFailureListener {
            Log.i("ERRO", it.toString())
        }
    }

    /**
     * Retrieving all *itens*.
     *
     * @param name the name of item.
     * @param category the category of item.
     * @return List of similarly named items that have a category equal to the parameter.
     */
    fun searchItemByNameAndCategory(name: String, category: String) {
        val reference = FirebaseConfig.firebaseFirestore().collection(Constants.ITEM)
        val task1 = reference.orderBy("name").startAt(name).endAt(name + "\uf8ff").get()
        val task2 = reference.whereEqualTo("categories", category).get()
        val allTasks = Tasks.whenAllSuccess<QuerySnapshot>(task1, task2)

        allTasks.addOnSuccessListener { querySnapShots ->
            for (documentSnapShots in querySnapShots) {
                val itemList: MutableList<Item> = mutableListOf()
                for (result in documentSnapShots) {
                    if (result["name"].toString().contains(name)) {
                        val item = Item(
                            name = result["name"] as String,
                            location = result["location"] as String,
                            description = result["description"] as String,
                            currentPrice = result["currentPrice"] as Double,
                            normalPrice = result["normalPrice"] as Double,
                            categories = result!!["categories"] as String,
                            images = result["images"] as ArrayList<String>,
                            date = result["date"] as Long,
                            userID = result["userID"] as String,
                            itemId = result.id,
                            store = result["store"] as String,
                            likes = result["likes"] as Long,
                            percent = result["percent"] as Long,
                            createdAt = result["createdAt"] as Long
                        )
                        itemList.add(item)
                    }
                }
                itemLiveDataList.value = itemList
            }
        }.addOnFailureListener {
            Log.i("ERRO", it.toString())
        }
    }

    fun allItens() {
        FirebaseConfig.firebaseFirestore().collection(Constants.ITEM).orderBy("name")
            .get()
            .addOnCompleteListener { querySnapShots ->
                if (querySnapShots.isSuccessful) {
                    val itemList: MutableList<Item> = mutableListOf()
                    for (result in querySnapShots.result!!) {
                        val item = Item(
                            name = result["name"] as String,
                            location = result["location"] as String,
                            description = result["description"] as String,
                            currentPrice = result["currentPrice"] as Double,
                            normalPrice = result["normalPrice"] as Double,
                            categories = result!!["categories"] as String,
                            images = result["images"] as ArrayList<String>,
                            date = result["date"] as Long,
                            userID = result["userID"] as String,
                            itemId = result.id,
                            store = result["store"] as String,
                            likes = result["likes"] as Long,
                            percent = result["percent"] as Long,
                            createdAt = result["createdAt"] as Long
                        )
                        itemList.add(item)
                    }
                    itemLiveDataList.value = itemList
                }
            }.addOnFailureListener {
                Log.i("ERRO", it.toString())
            }
    }

    /**
     * Retrieving all *itens*.
     *
     * @param category the category of item.
     * @return List of item that have a category equal to the parameter.
     */
    fun searchItemByCategory(category: String) {
        FirebaseConfig.firebaseFirestore().collection(Constants.ITEM)
            .whereEqualTo("categories", category).get()
            .addOnCompleteListener { querySnapShots ->
                if (querySnapShots.isSuccessful) {
                    val itemList: MutableList<Item> = mutableListOf()
                    for (result in querySnapShots.result!!) {
                        val item = Item(
                            name = result["name"] as String,
                            location = result["location"] as String,
                            description = result["description"] as String,
                            currentPrice = result["currentPrice"] as Double,
                            normalPrice = result["normalPrice"] as Double,
                            categories = result!!["categories"] as String,
                            images = result["images"] as ArrayList<String>,
                            date = result["date"] as Long,
                            userID = result["userID"] as String,
                            itemId = result.id,
                            store = result["store"] as String,
                            likes = result["likes"] as Long,
                            percent = result["percent"] as Long,
                            createdAt = result["createdAt"] as Long
                        )
                        itemList.add(item)
                    }
                    itemLiveDataList.value = itemList
                }
            }
    }

    fun searchItemByNameAndPrice(name: String, min: String, max: String) {
        val reference = FirebaseConfig.firebaseFirestore().collection(Constants.ITEM)
        val task1 = reference.orderBy("name").startAt(name).endAt(name + "\uf8ff").get()
        val task2 = reference.whereGreaterThanOrEqualTo("currentPrice", min.toDouble()).get()
        val task3 = reference.whereLessThanOrEqualTo("normalPrice", max.toDouble()).get()
        val allTasks = Tasks.whenAllSuccess<QuerySnapshot>(task1, task2, task3)
        allTasks.addOnSuccessListener { querySnapShots ->
            for (documentSnapShots in querySnapShots) {
                val itemList: MutableList<Item> = mutableListOf()
                for (result in documentSnapShots) {
                    if (result["name"].toString().contains(name)) {
                        val item = Item(
                            name = result["name"] as String,
                            location = result["location"] as String,
                            description = result["description"] as String,
                            currentPrice = result["currentPrice"] as Double,
                            normalPrice = result["normalPrice"] as Double,
                            categories = result!!["categories"] as String,
                            images = result["images"] as ArrayList<String>,
                            date = result["date"] as Long,
                            userID = result["userID"] as String,
                            itemId = result.id,
                            store = result["store"] as String,
                            likes = result["likes"] as Long,
                            percent = result["percent"] as Long,
                            createdAt = result["createdAt"] as Long
                        )
                        itemList.add(item)
                    }
                }
                itemLiveDataList.value = itemList
            }
        }.addOnFailureListener {
            Log.i("ERRO", it.toString())
        }
    }

    /**
     * Retrieving all *items*.
     *
     * @param name the name of item.
     * @param category the category of item.
     * @param max the minimal price of item.
     * @return List of similarly named items that have a minimum price and a category equal to the parameter.
     */
    fun searchItemByCategoryAndNameMax(name: String, category: String, max: String) {
        val reference = FirebaseConfig.firebaseFirestore().collection(Constants.ITEM)
        val task1 = reference.orderBy("name").startAt(name).endAt(name + "\uf8ff").get()
        val task3 = reference.whereLessThanOrEqualTo("currentPrice", max.toDouble()).get()
        val allTasks: Task<List<QuerySnapshot>>?
        allTasks = if (category != "All" || category != "") {
            val task2 = reference.whereEqualTo("categories", category).get()
            Tasks.whenAllSuccess<QuerySnapshot>(task1, task2, task3)
        } else {
            Tasks.whenAllSuccess<QuerySnapshot>(task1, task3)
        }

        allTasks!!.addOnSuccessListener { documentSnapShots ->
            for (querySnapShots in documentSnapShots) {
                val itemList: MutableList<Item> = mutableListOf()
                for (result in querySnapShots) {
                    if (result["name"].toString().contains(name)) {
                        val item = Item(
                            name = result["name"] as String,
                            location = result["location"] as String,
                            description = result["description"] as String,
                            currentPrice = result["currentPrice"] as Double,
                            normalPrice = result["normalPrice"] as Double,
                            categories = result!!["categories"] as String,
                            images = result["images"] as ArrayList<String>,
                            date = result["date"] as Long,
                            userID = result["userID"] as String,
                            itemId = result.id,
                            store = result["store"] as String,
                            likes = result["likes"] as Long,
                            percent = result["percent"] as Long,
                            createdAt = result["createdAt"] as Long
                        )
                        itemList.add(item)
                    }
                }
                itemLiveDataList.value = itemList
            }
        }.addOnFailureListener {
            Log.i("ERRO", it.toString())
        }
    }

    fun hotDeals(/*location: String*/) {
        FirebaseConfig.firebaseFirestore().collection(Constants.ITEM)
            .whereGreaterThanOrEqualTo("likes", 2)
            .orderBy("likes", Query.Direction.DESCENDING)
            .get().addOnCompleteListener { querySnapShots ->
                if (querySnapShots.isSuccessful) {
                    val itemList: MutableList<Item> = mutableListOf()
                    for (result in querySnapShots.result!!) {
                        val item = Item(
                            name = result["name"] as String,
                            location = result["location"] as String,
                            description = result["description"] as String,
                            currentPrice = result["currentPrice"] as Double,
                            normalPrice = result["normalPrice"] as Double,
                            categories = result["categories"] as String,
                            images = result["images"] as ArrayList<String>,
                            date = result["date"] as Long,
                            userID = result["userID"] as String,
                            itemId = result.id,
                            store = result["store"] as String,
                            likes = result["likes"] as Long,
                            percent = result["percent"] as Long,
                            createdAt = result["createdAt"] as Long
                        )
                        itemList.add(item)
                    }
                    itemLiveDataList.value = itemList
                }
            }.addOnFailureListener {
                Log.i("ERRO", it.toString())
            }
    }
}