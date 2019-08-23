package mz.co.avana.presentation.ui.item


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import co.csadev.kwikpicker.KwikPicker
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bootom_sheet_categories.*
import kotlinx.android.synthetic.main.form_item.*
import kotlinx.android.synthetic.main.list_pictures.*
import mz.co.avana.R
import mz.co.avana.model.Category
import mz.co.avana.model.Images
import mz.co.avana.model.Item
import mz.co.avana.presentation.ui.category.CategoryAdapter
import mz.co.avana.presentation.ui.image.ImagesAdapter
import mz.co.avana.presentation.ui.main.HomeActivity
import mz.co.avana.repository.item.ItemRepository
import mz.co.avana.repository.user.UserRepository
import mz.co.avana.utils.Message
import mz.co.avana.utils.MessageCallback
import mz.co.avana.utils.Utils
import mz.co.avana.utils.Validator.Companion.validate
import mz.co.avana.utils.Validator.Companion.validateLength
import mz.co.avana.utils.Validator.Companion.validatePrice
import java.util.*
import kotlin.collections.ArrayList

class ShareItemActivity : AppCompatActivity() {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private val lista: ArrayList<Images> = ArrayList()
    private val listOfImages: ArrayList<Uri> = ArrayList()
    private var imageOne: Uri = Uri.EMPTY
    private var imageTwo: Uri = Uri.EMPTY
    private var imageThree: Uri = Uri.EMPTY
    var images: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_item)

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        selectCategory.setOnClickListener {
            bottomSheetBehavior.peekHeight = 600
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        closeform.setOnClickListener {
            startActivity(Intent(baseContext, HomeActivity::class.java))
            finish()
        }
        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            @SuppressLint("SwitchIntDef")
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {

                        with(rv_Categories) {
                            layoutManager = GridLayoutManager(applicationContext, 3)
                            setHasFixedSize(true)
                            adapter = CategoryAdapter(getCat(), applicationContext) { categories ->

                                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                                selectCategory.text = categories.name
                            }
                        }
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }
        })


        formimages.setOnClickListener {
            selectImages()
        }

        share.setOnClickListener {
            if (isValidInput()) {
                val item = Item(
                        itemName.editText!!.text.toString(),
                        location.editText!!.text.toString(),
                        description.editText!!.text.toString(),
                        itemPrice.editText!!.text.toString().toDouble(),
                        promotionPrice.editText!!.text.toString().toDouble()
                        , Category("head", selectCategory.text.toString()),
                        Utils.dateToMills(end_promo.text.toString()),
                        images!!,
                        store.editText.toString(),
                        UserRepository.user()
                )

                Toast.makeText(baseContext, Utils.dateToMills(end_promo.text.toString()).toString(), Toast.LENGTH_SHORT).show()
                val itemRepository = ItemRepository(baseContext, item, listOfImages)
                itemRepository.shareItemDetails(object : MessageCallback {
                    override fun onSuccess(successMessage: String) {
                        Message.message(baseContext, successMessage)
                    }

                    override fun onError(errorMessage: String) {
                        Message.message(baseContext, errorMessage)
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
                        val text = String.format(resources.getString(R.string.show_date), day, m, y)
                        end_promo.setText(text.trim())
                    }, year, month, dayOfMonth
            )
            datePicker.datePicker.minDate = System.currentTimeMillis()
            datePicker.show()
        }
    }

    private fun isValidInput(): Boolean {
        var invalid = false

        if (validate(itemName) || validateLength(itemName)) {
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
            description.error = getString(R.string.item_escription_required)
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

    fun getCat(): List<Category> {
        return listOf(
                Category("", "ESCOLA"),
                Category("", "ELETRONICO"),
                Category("", "ESCRITORIO"),
                Category("", "CARRO"),
                Category("", "CASA"),
                Category("", "MATERIA")
        )
    }

    private fun selectImages() {

        var contador = 0

        let { it1 ->
            AlertDialog.Builder(it1)
                    .setTitle(getString(R.string.select_images))
                    .setPositiveButton(getString(R.string.gallery)) { dialog, _ ->
                        dialog.dismiss()

                        val kwikPicker = KwikPicker.Builder(
                                this@ShareItemActivity,
                                imageProvider = { imageView, uri ->
                                    Glide.with(this@ShareItemActivity)
                                            .load(uri)
                                            .into(imageView)
                                },
                                onMultiImageSelectedListener = { list: ArrayList<Uri> ->
                                    for (i in list) {
                                        when (contador) {
                                            0 -> imageOne = i
                                            1 -> imageTwo = i
                                            2 -> imageThree = i
                                        }
                                        contador++

                                        listOfImages.add(i)
                                    }
                                    lista.clear()
                                    lista.add(Images(imageOne, imageTwo, imageThree))

                                    with(rvPhoto, {
                                        layoutManager = LinearLayoutManager(context)
                                        setHasFixedSize(true)
                                        adapter = ImagesAdapter(context, lista) {
                                            selectImages()
                                        }
                                        formimages.visibility = View.GONE
                                    })

                                },
                                peekHeight = 1600,
                                showTitle = false,
                                selectMaxCount = 3,
                                completeButtonText = "Done",
                                emptySelectionText = "No Selection"
                        ).create(baseContext)

                        kwikPicker.show(supportFragmentManager)
                    }
                    .show()
        }
    }
}