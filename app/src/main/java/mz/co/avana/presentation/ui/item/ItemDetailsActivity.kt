package mz.co.avana.presentation.ui.item

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ShareActionProvider
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.activity_item_details.*
import mz.co.avana.R
import mz.co.avana.databinding.ActivityItemDetailsBinding
import mz.co.avana.model.Comment
import mz.co.avana.model.Item
import mz.co.avana.presentation.ui.comment.CommentAdapter
import mz.co.avana.presentation.ui.image.SliderAdapter
import mz.co.avana.presentation.ui.main.HomeActivity
import mz.co.avana.repository.comment.CommentRepository
import mz.co.avana.repository.user.UserRepository
import mz.co.avana.utils.Constants
import mz.co.avana.utils.MessageCallback
import mz.co.avana.utils.Utils
import mz.co.avana.viewModel.comment.ComentViewModel
import mz.co.avana.viewModel.user.UserViewModel


class ItemDetailsActivity : AppCompatActivity() {

    private var shareActionProvider: ShareActionProvider? = null

    lateinit var item: Item
    lateinit var binding: ActivityItemDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
                this, mz.co.avana.R.layout.activity_item_details
        )

        btnBack.setOnClickListener {
            val intent = Intent(this@ItemDetailsActivity, HomeActivity::class.java)
            intent.putExtra("fragment", "home")
            startActivity(intent)
        }

        intent!!.let {
            item = intent.extras!!.getParcelable(Constants.ITEM) as Item

            end_promo_details.text = Utils.toNormalDate(item.date)
            //Put style of textview
            normalPriceDetails.paintFlags = normalPriceDetails.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG

            val adapter = SliderAdapter(this, item.images)
            imageSlider.sliderAdapter = adapter
            imageSlider.setIndicatorAnimation(IndicatorAnimations.WORM) //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
            imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
            imageSlider.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
            imageSlider.indicatorSelectedColor = Color.WHITE
            imageSlider.indicatorUnselectedColor = Color.GRAY
            imageSlider.scrollTimeInSec = 10 //set scroll delay in seconds :
            imageSlider.startAutoCycle()

            val userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
            userViewModel.userLiveData.observe(this, Observer { user ->
                userPubliched.text = user.name
            })
            userViewModel.specificUserData(item.userID)
            binding.item = item

            loadComments()
        }

        share_item_external.setOnClickListener {
            val shareIntent = ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain").setText("vvvvvvvv").intent

            setShareIntent(shareIntent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.share_menu, menu)
        menu.findItem(R.id.menu_item_share).also { menuItem ->
            shareActionProvider = menuItem.actionProvider as? ShareActionProvider
        }
        return true
    }

    private fun setShareIntent(shareIntent: Intent) {
        shareActionProvider?.setShareIntent(shareIntent)
    }

    fun commentPost(view: View) {
        val comment = Comment(UserRepository.user(),
                til_comment.editText!!.text.toString(),
                Utils.toNormalDate(System.currentTimeMillis()))
        val repository = CommentRepository(comment, baseContext)
        item.itemId?.let {
            repository.commentAnItem(it, object : MessageCallback {
                override fun onSuccess(successMessage: String) {
                    Utils.showMessage(baseContext, binding.root, successMessage)
                    loadComments()
                }

                override fun onError(errorMessage: String) {
                    Utils.showMessage(baseContext, binding.root, errorMessage)
                }

            })
        }

    }

    fun loadComments(){
        val commentViewModel = ViewModelProviders.of(this).get(ComentViewModel::class.java)
        commentViewModel.commentLiveDataList.observe(this, Observer {comments->
            with(rv_comments, {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                setHasFixedSize(true)
                adapter = CommentAdapter(comments, context)
            })

        })

        commentViewModel.comments(item.itemId!!)
    }

    fun openAlbum(view: View) {
        val transaction = supportFragmentManager.beginTransaction()
        val fragment = ImageDialogFragment()
        val bundle = Bundle()

        val imagePosition = when (view.id) {
            R.id.iv_img_thumb_01 -> 0
            R.id.iv_img_thumb_02 -> 1
            R.id.iv_img_thumb_03 -> 2
            else -> 0
        }

        bundle.putParcelable(ImageDialogFragment.KEY, item)
        bundle.putInt(Item.KEY_IMAGE, imagePosition)
        fragment.arguments = bundle
        fragment.show(transaction, ImageDialogFragment.KEY)
    }

}