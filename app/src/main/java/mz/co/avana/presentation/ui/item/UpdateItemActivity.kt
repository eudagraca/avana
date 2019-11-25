package mz.co.avana.presentation.ui.item

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import co.csadev.kwikpicker.KwikPicker
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_share_item.*
import kotlinx.android.synthetic.main.form_item.*
import kotlinx.android.synthetic.main.list_pictures.*
import mz.co.avana.R
import mz.co.avana.callbacks.ItemPriceCallback
import mz.co.avana.callbacks.MessageCallback
import mz.co.avana.model.Images
import mz.co.avana.model.Item
import mz.co.avana.presentation.ui.dialog.CustomDialog
import mz.co.avana.presentation.ui.image.ImagesAdapter
import mz.co.avana.presentation.ui.main.HomeActivity
import mz.co.avana.presentation.ui.search.CategoriesBottomDialogFragment
import mz.co.avana.repository.item.ItemRepository
import mz.co.avana.repository.user.UserRepository
import mz.co.avana.utils.*
import java.util.*
import kotlin.collections.ArrayList

class UpdateItemActivity : AppCompatActivity() {
    private var selectCategory: TextView? = null
    private var category: String? = ""
    private val list: ArrayList<Images> = ArrayList()
    private val listOfImages: ArrayList<Uri> = ArrayList()
    private var imageOne: Uri = Uri.EMPTY
    private var imageTwo: Uri = Uri.EMPTY
    private var imageThree: Uri = Uri.EMPTY
    var images: ArrayList<String>? = null
    lateinit var item: Item
    var load: CustomDialog? = null

    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_item)
        selectCategory = findViewById(R.id.selectCategory)

        intent!!.let {
            item = intent.extras!!.getParcelable<Item>(Constants.ITEM) as Item

            Glide.with(baseContext).load(item.images!![0]).into(imgOne)
            Glide.with(baseContext).load(item.images!![1]).into(imgTwo)
            Glide.with(baseContext).load(item.images!![2]).into(imgThree)

            for (image in item.images!!){
                listOfImages.add(image.toUri())
            }

            itemName.editText!!.setText(item.name)
            promotionPrice.editText!!.setText(item.currentPrice.toString())
            itemPrice.editText!!.setText(item.normalPrice.toString())
            end_promo.setText(Utils.toNormalDate(item.date))
            store.editText!!.setText(item.store)
            location.editText!!.setText(item.location)
            description.editText!!.setText(item.description)
            category = item.categories
            selectCategory!!.text = getStringResource(category!!.toLowerCase(), R.string::class.java)
        }

        selectCategory!!.setOnClickListener {
            getCategories()
        }

        closeform.setOnClickListener {
            startActivity()
        }

        load = CustomDialog(getString(R.string.update_item), getString(R.string.please_wait))
        load!!.isCancelable = false

        share.setOnClickListener {
            if (isValidInput()) {
                load!!.show(supportFragmentManager, "missiles")
                val item = Item(
                    itemName.editText!!.text.toString(),
                    location.editText!!.text.toString().toLowerCase(),
                    description.editText!!.text.toString(),
                    itemPrice.editText!!.text.toString().toDouble(),
                    promotionPrice.editText!!.text.toString().toDouble(),
                    category!!,
                    Utils.dateToMills(end_promo.text.toString()),
                    store.editText!!.text.toString(),
                    UserRepository.user(),
                    item.itemId!! )
                val itemRepository = ItemRepository(baseContext, item, listOfImages, this@UpdateItemActivity)
                itemRepository.updateItemDetails(object : MessageCallback {
                    override fun onSuccess(successMessage: String) {
                        if (successMessage.isNotEmpty() && successMessage.isNotBlank()) {
                            Message.snackbarMessage(baseContext, shareitem, successMessage)
                        }
                        handler()
                    }

                    override fun onError(errorMessage: String) {
                        if (errorMessage.isNotEmpty() && errorMessage.isNotBlank()) {
                            Message.snackbarMessage(baseContext, shareitem, errorMessage)
                        }
                    }
                })
                load!!.dismissAllowingStateLoss()
            }
        }

        end_promo.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar[Calendar.YEAR]
            val month = calendar[Calendar.MONTH]
            val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]
            val datePicker = DatePickerDialog(
                this@UpdateItemActivity,
                DatePickerDialog.OnDateSetListener { _, y, m, day ->
                    val text = String.format(resources.getString(R.string.show_date), day, m + 1, y)
                    end_promo.setText(text.trim())
                }, year, month, dayOfMonth
            )
            datePicker.datePicker.minDate = System.currentTimeMillis()
            datePicker.show()
        }
    }

    private fun isValidInput(): Boolean {
        var invalid = false
        if (category!!.isEmpty() || category == "") {
            Message.messageToast(baseContext, getString(R.string.select_category))
        } else if (listOfImages.size < 3) {
            Message.messageToast(baseContext, getString(R.string.select_three_pictures))

        } else if (Validator.validate(itemName) || Validator.validateLength(itemName)) {
            itemName.error = getString(R.string.invalid_name)
            itemName.requestFocus()

        } else if (Validator.validate(promotionPrice!!) || Validator.validatePrice(promotionPrice!!)) {
            promotionPrice.error = getString(R.string.promotion_price_required)
            promotionPrice.requestFocus()
            itemName.clearFocus()
            itemName.isErrorEnabled = false

        } else if (Validator.validate(itemPrice!!) || Validator.validatePrice(itemPrice!!)) {
            itemPrice.error = getString(R.string.item_price_required)
            itemPrice.requestFocus()
            promotionPrice.clearFocus()
            promotionPrice.isErrorEnabled = false

        } else if (end_promo.text == null) {
            end_promo.error = getString(R.string.promotion_end_date_required)
            end_promo.requestFocus()
            itemPrice!!.clearFocus()
            itemPrice.isErrorEnabled = false

        } else if (Validator.validate(store)) {
            store.error = getString(R.string.store_that_sells_item)
            store.requestFocus()
            end_promo.clearFocus()

        } else if (Validator.validate(location)) {
            location.error = getString(R.string.where_the_item_is_located)
            location.requestFocus()
            store.clearFocus()
            store.isErrorEnabled = false

        } else if (Validator.validate(description)) {
            description.error = getString(R.string.item_description_required)
            description.requestFocus()
            location.clearFocus()
            location.isErrorEnabled = false
        } else {
            description.clearFocus()
            description.isErrorEnabled = false
            invalid = true
        }
        return invalid
    }

    private fun getCategories() {
        val addPhotoBottomDialogFragment =
            CategoriesBottomDialogFragment(object : ItemPriceCallback {
                override fun values(min: String, max: String, item: String) {

                }

                override fun categories(name: String, databaseName: String) {
                    selectCategory!!.text = name
                    category = databaseName
                }
            })
        addPhotoBottomDialogFragment.show(
            supportFragmentManager,
            "add_photo_dialog_fragment"
        )
    }

    fun openAlbum() {
        selectImages()
    }

    private fun selectImages() {
        var counter = 0
        let { it1 ->
            val dialog = Dialog(it1)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.open_gallery)
            dialog.window?.setLayout((resources.displayMetrics.widthPixels*0.80).toInt(), LinearLayout.LayoutParams.WRAP_CONTENT)
            dialog.findViewById<TextView>(R.id.openGalley).setOnClickListener {
                    dialog.dismiss()
                    val kwikPicker = KwikPicker.Builder(
                        this@UpdateItemActivity,
                        imageProvider = { imageView, uri ->
                            if (uri != null){
                                Glide.with(this@UpdateItemActivity)
                                    .load(uri)
                                    .into(imageView)
                            }
                        },
                        onMultiImageSelectedListener = { list: ArrayList<Uri> ->
                            if (list.size > 0){
                                for (i in list) {
                                    when (counter) {
                                        0 -> imageOne = i
                                        1 -> imageTwo = i
                                        2 -> imageThree = i
                                    }
                                    counter++
                                    listOfImages.add(i)
                                }
                                this.list.clear()
                                this.list.add(Images(imageOne, imageTwo, imageThree))
                            }

                            with(rvPhoto, {
                                layoutManager = LinearLayoutManager(context)
                                setHasFixedSize(true)
                                adapter = ImagesAdapter(context, this@UpdateItemActivity.list) {
                                    selectImages()
                                }
                                if(list.size > 0){
                                    formimages.visibility = View.GONE
                                }
                            })
                        },
                        peekHeight = 1600,
                        showTitle = false,
                        selectMaxCount = 3,
                        completeButtonText = getString(R.string.done),
                        emptySelectionText = "No Selection"
                    ).create(baseContext)
                    kwikPicker.show(supportFragmentManager)
                }
                dialog.show()
        }
    }

    private fun handler() {
        val handle = Handler()
        form_item.visibility = View.GONE
        spin_kit.visibility = View.VISIBLE
        handle.postDelayed(this::startActivity, 5000)
    }

    private fun startActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun getStringResource(resName: String, c: Class<*>): String {
        return try {
            val idField = c.getDeclaredField(resName)
            resources.getString(idField.getInt(idField))
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        load!!.dismissAllowingStateLoss()
    }
}
