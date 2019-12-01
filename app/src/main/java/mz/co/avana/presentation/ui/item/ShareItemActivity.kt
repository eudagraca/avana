package mz.co.avana.presentation.ui.item

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import co.csadev.kwikpicker.KwikPicker
import com.asksira.bsimagepicker.BSImagePicker
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
import mz.co.avana.utils.Message
import mz.co.avana.utils.Utils
import mz.co.avana.utils.Validator.Companion.validate
import mz.co.avana.utils.Validator.Companion.validateLength
import mz.co.avana.utils.Validator.Companion.validatePrice
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class ShareItemActivity : AppCompatActivity(), BSImagePicker.OnMultiImageSelectedListener,
    BSImagePicker.ImageLoaderDelegate {

    private var selectCategory: TextView? = null
    private var category: String? = ""
    private val list: ArrayList<Images> = ArrayList()
    private val listOfImages: ArrayList<Uri> = ArrayList()
    private var imageOne: Uri = Uri.EMPTY
    private var imageTwo: Uri = Uri.EMPTY
    private var imageThree: Uri = Uri.EMPTY
    var images: ArrayList<String>? = null
    var load: CustomDialog? = null

    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_item)

        selectCategory = findViewById(R.id.selectCategory)

        selectCategory!!.setOnClickListener {
            getCategories()
        }

        load = CustomDialog(getString(R.string.sharing_item), getString(R.string.please_wait))
        load!!.isCancelable = false

        closeform.setOnClickListener {
            onBackPressed()
        }

        share.setOnClickListener {
            if (isValidInput()) {

                load!!.show(supportFragmentManager, "missiles")
                val item = Item(
                    itemName.editText!!.text.toString().toLowerCase(),
                    location.editText!!.text.toString().toLowerCase(),
                    description.editText!!.text.toString(),
                    itemPrice.editText!!.text.toString().toDouble(),
                    promotionPrice.editText!!.text.toString().toDouble(),
                    category!!,
                    Utils.dateToMills(end_promo.text.toString()),
                    store.editText!!.text.toString(),
                    UserRepository.user()
                )
                val itemRepository =
                    ItemRepository(baseContext, item, listOfImages, this@ShareItemActivity)
                itemRepository.shareItemDetails(object : MessageCallback {
                    override fun onSuccess(successMessage: String) {
                        load!!.dismissAllowingStateLoss()
                        if (successMessage.isNotEmpty() && successMessage.isNotBlank()) {
                            Message.snackbarMessage(baseContext, shareitem, successMessage)
                        }
                        handler()
                    }

                    override fun onError(errorMessage: String) {
                        load!!.dismissAllowingStateLoss()
                        if (errorMessage.isNotEmpty() && errorMessage.isNotBlank()) {
                            Message.snackbarMessage(baseContext, shareitem, errorMessage)
                        }
                    }
                })
            }
        }

        end_promo.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar[Calendar.YEAR]
            val month = calendar[Calendar.MONTH]
            val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]
            val datePicker = DatePickerDialog(
                this@ShareItemActivity,
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

        } else if (validate(itemName) || validateLength(itemName)) {
            itemName.error = getString(R.string.invalid_name)
            itemName.requestFocus()

        } else if (validate(promotionPrice!!) || validatePrice(promotionPrice!!)) {
            promotionPrice.error = getString(R.string.promotion_price_required)
            promotionPrice.requestFocus()
            itemName.clearFocus()
            itemName.isErrorEnabled = false

        } else if (validate(itemPrice!!) || validatePrice(itemPrice!!)) {
            itemPrice.error = getString(R.string.item_price_required)
            itemPrice.requestFocus()
            promotionPrice.clearFocus()
            promotionPrice.isErrorEnabled = false

        } else if (end_promo.text == null) {
            end_promo.error = getString(R.string.promotion_end_date_required)
            end_promo.requestFocus()
            itemPrice!!.clearFocus()
            itemPrice.isErrorEnabled = false

        } else if (validate(store)) {
            store.error = getString(R.string.store_that_sells_item)
            store.requestFocus()
            end_promo.clearFocus()

        } else if (validate(location)) {
            location.error = getString(R.string.where_the_item_is_located)
            location.requestFocus()
            store.clearFocus()
            store.isErrorEnabled = false

        } else if (validate(description)) {
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

    fun openAlbum(view: View) {
        selectImages().show()
    }

    private fun selectImages(): Dialog {
        var counter = 0
        val dialog = Dialog(this@ShareItemActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.open_gallery)
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.80).toInt(),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        dialog.findViewById<TextView>(R.id.openGalley).setOnClickListener {
            dialog.dismiss()

            if (ActivityCompat.checkSelfPermission(
                    this@ShareItemActivity,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@ShareItemActivity,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 100
                )

                return@setOnClickListener
            }

            val multiSelectionPicker = BSImagePicker.Builder("mz.co.avana.presentation.ui.item")
               .isMultiSelect //Set this if you want to use multi selection mode.
               .setMinimumMultiSelectCount(3) //Default: 1.
               .setMaximumMultiSelectCount(3) //Default: Integer.MAX_VALUE (i.e. User can select as many images as he/she wants)
               .setMultiSelectBarBgColor(android.R.color.white) //Default: #FFFFFF. You can also set it to a translucent color.
               .setMultiSelectTextColor(R.color.primary_text) //Default: #212121(Dark grey). This is the message in the multi-select bottom bar.
               .setMultiSelectDoneTextColor(R.color.colorAccent) //Default: #388e3c(Green). This is the color of the "Done" TextView.
               .setOverSelectTextColor(R.color.error_text) //Default: #b71c1c. This is the color of the message shown when user tries to select more than maximum select count.
               .disableOverSelectionMessage() //You can also decide not to show this over select message.
               .build()

            multiSelectionPicker.show(supportFragmentManager, "picker")

//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//            intent.type = "images/*"
//            startActivityForResult(intent, 1)

//            val kwikPicker = KwikPicker.Builder(
//                this@ShareItemActivity,
//                imageProvider = { imageView, uri ->
//                    if (uri != null) {
//                        Glide.with(this@ShareItemActivity)
//                            .load(uri)
//                            .into(imageView)
//                    }
//                },
//                onMultiImageSelectedListener = { list: ArrayList<Uri> ->
//                    if (list.size > 0){
//                        for (i in list) {
//                            when (counter) {
//                                0 -> imageOne = i
//                                1 -> imageTwo = i
//                                2 -> imageThree = i
//                            }
//                            counter++
//                            listOfImages.add(i)
//                        }
//                    }
//                    this.list.clear()
//                    this.list.add(Images(imageOne, imageTwo, imageThree))


//                },
//                peekHeight = 1600,
//                showTitle = false,
//                selectMaxCount = 3,
//                completeButtonText = getString(R.string.done),
//                emptySelectionText = getString(R.string.no_image_selected)
//            ).create(baseContext)
//            kwikPicker.show(supportFragmentManager)
        }



        return dialog
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

    override fun onMultiImageSelected(uriList: MutableList<Uri>?, tag: String?) {
        list.clear()
        var counter = 0
        if (uriList!!.size > 0) {
            for (i in uriList) {
                when (counter) {
                    0 -> imageOne = i
                    1 -> imageTwo = i
                    2 -> imageThree = i
                }
                counter++
                listOfImages.add(i)
            }

            this.list.add(Images(imageOne, imageTwo, imageThree))
            with(rvPhoto, {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = ImagesAdapter(context, this@ShareItemActivity.list) {
                    selectImages()
                }
                if (list.size > 0) {
                    formimages.visibility = View.GONE
                }
            })
        }
        uriList.clear()
    }

    override fun loadImage(imageFile: File?, ivImage: ImageView?) {
        Glide.with(this@ShareItemActivity).load(imageFile).into(ivImage!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(load!!.isVisible){
            load!!.dismissAllowingStateLoss()
        }
    }
}