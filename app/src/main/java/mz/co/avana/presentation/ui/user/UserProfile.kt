package mz.co.avana.presentation.ui.user

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.developer.kalert.KAlertDialog
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.is_blank.view.*
import kotlinx.android.synthetic.main.loading.view.*
import mz.co.avana.R
import mz.co.avana.callbacks.MessageCallback
import mz.co.avana.model.User
import mz.co.avana.presentation.ui.item.ItemDetailsActivity
import mz.co.avana.presentation.ui.item.UserItemAdapter
import mz.co.avana.repository.user.UserRepository
import mz.co.avana.utils.Constants
import mz.co.avana.utils.Message
import mz.co.avana.utils.Utils
import mz.co.avana.viewModel.item.ItemViewModel
import mz.co.avana.viewModel.user.UserViewModel

class UserProfile : Fragment(), MessageCallback {

    private val PICK_IMAGE_REQUEST = 101
    private lateinit var mView: View

    override fun onSuccess(successMessage: String) {
        if (UserRepository.isLogged()) {
            userData()
        }
        Message.snackbarMessage(context!!, mView, successMessage)
    }

    override fun onError(errorMessage: String) {
        if (UserRepository.isLogged()) {
            Message.snackbarMessage(context!!, mView, errorMessage)
        }
    }

    var name: String? = ""
    var surname: String? = ""
    val user: User = User()

    private var dialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_profile, container, false)
        mView.edit_name.visibility = View.GONE
        mView.lLisBlank.visibility = View.GONE
        mView.loading.visibility = View.GONE

        dialog = Dialog(context!!)

        if (UserRepository.isLogged()) {
            mView.edit_name.visibility = View.VISIBLE
            mView.loading.visibility = View.VISIBLE

            val user = Utils.readPreference(
                Constants.USER, Constants.USER, activity!!
            )

            val posts = Utils.readPreference(
                Constants.POSTS, Constants.POSTS, activity!!
            )

            val email = Utils.readPreference(
                Constants.EMAIL, Constants.EMAIL, activity!!
            )

            val promotions = Utils.readPreference(
                Constants.PROMOTION, Constants.PROMOTION, activity!!
            )

            mView.logOut.setOnClickListener {

                dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog!!.setContentView(R.layout.open_gallery)
                dialog!!.window?.setLayout(
                    (resources.displayMetrics.widthPixels * 0.80).toInt(),
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

                val ok = dialog!!.findViewById<TextView>(R.id.openGalley)
                val text = dialog!!.findViewById<TextView>(R.id.tv_text)
                text.text = getString(R.string.log_out)
                ok.text = context!!.getString(R.string.yes)
                ok.setOnClickListener {
                    logOut()
                }
                dialog!!.show()
            }

            if (loadImage() == "--") {
                Glide.with(context!!).load(context!!.getDrawable(R.drawable.user))
                    .transform(CenterCrop(), RoundedCorners(50))
                    .into(mView.profile_image)
            } else {
                Glide.with(context!!).load(loadImage())
                    .transform(CenterCrop(), RoundedCorners(50))
                    .into(mView.profile_image)
            }

            mView.profile_image.setOnClickListener {
                selectImage(mView).show()
            }

            mView.nr_posts.text = posts
            mView.username.text = user
            mView.nr_promotions.text = promotions
            mView.myEmail.text = email

            val itemViewModel = ViewModelProviders.of(this).get(ItemViewModel::class.java)
            itemViewModel.itemLiveDataList.observe(this, Observer { items ->
                mView.loading.visibility = View.VISIBLE
                Utils.writeSharedPreferences(
                    Constants.PROMOTION, items.size.toString(),
                    Constants.PROMOTION,
                    activity!!
                )

                with(mView.rv_my_items, {
                    layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                    setHasFixedSize(true)
                    adapter = UserItemAdapter(items, context) { item ->
                        val intent = Intent(activity, ItemDetailsActivity::class.java)
                        intent.putExtra(Constants.ITEM, item)
                        intent.putExtra("fragment", "profile")
                        startActivity(intent)
                    }

                    if (items.isEmpty()) {
                        mView.lLisBlank.visibility = View.VISIBLE
                        mView.tv_isBlank.text = context.getString(R.string.havent_share_item)
                    } else {
                        mView.lLisBlank.visibility = View.GONE
                    }
                    mView.loading.visibility = View.GONE
                })
            })
            itemViewModel.itemsUser()
            mView.edit_name.setOnClickListener {
                openNameEditDialog()
            }
        }

        return mView
    }

    private fun openNameEditDialog() {
        val bundle = Bundle()
        val newFragment = EditUserNameDialog(this)
        bundle.putString(Constants.NAMES_OF_USER, name)
        bundle.putString(Constants.SURNAME, surname)
        newFragment.arguments = bundle
        newFragment.show(childFragmentManager, "missiles")
    }

    private fun userData() {
        mView.loading.visibility = View.VISIBLE
        val userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        userViewModel.userLiveData.observe(this, Observer {
            it?.let { user ->
                username.text = resources.getString(R.string.show_username, user.name, user.surname)
                name = user.name
                surname = user.surname!!
                Utils.writeSharedPreferences(
                    Constants.USER,
                    "${user.name} ${user.surname}", Constants.USER,
                    activity!!
                )
            }
            mView.loading.visibility = View.GONE
        })
        userViewModel.userData()
    }

    private fun selectImage(view: View): Dialog {
        let {
            val dialog = Dialog(context!!)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.open_gallery)
            dialog.window?.setLayout(
                (resources.displayMetrics.widthPixels * 0.80).toInt(),
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            dialog.findViewById<TextView>(R.id.openGalley).setOnClickListener {
                dialog.dismiss()
                pickImage()
            }
            return dialog
        }
    }

    private fun loadImage() = Utils.readPreference(
        Constants.IMAGE_PROF,
        Constants.IMAGE_PROF,
        activity!!
    )

    private fun logOut() {
        if (UserRepository.isLogged()) {
            UserRepository.logOut()
        }
        context!!.startActivity(Intent(context!!, UserLoginActivity::class.java))
        activity!!.finish()
    }

    override fun onPause() {
        super.onPause()
        dialog!!.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        dialog!!.dismiss()
    }

    private fun pickImage() {
      val intent = Intent(Intent.ACTION_GET_CONTENT)
      // Sets the type as image/*. This ensures only components of type image are selected
        intent.type = "image/*"
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        intent.action = Intent.ACTION_GET_CONTENT
        // Launching the Intent
       startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

          if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                user.image = data!!.data
                val image = data.data
                KAlertDialog(context, KAlertDialog.WARNING_TYPE)
                    .setTitleText(context!!.getString(R.string.are_you_sure))
                    .setContentText(context!!.getString(R.string.wont_be_able_to_recover_old_image))
                    .confirmButtonColor(R.drawable.alert_button_positive)
                    .setConfirmText(context!!.getString(R.string.yes_change_it))
                    .cancelButtonColor(R.drawable.alert_button_cancel)
                    .setConfirmText(context!!.getString(R.string.yes_change_it))
                    .setCancelText(context!!.getString(R.string.no_cancel))
                    .setConfirmClickListener { sDialog ->
                        mView.loading.visibility = View.VISIBLE
                        sDialog.dismissWithAnimation()
                        val repository = UserRepository(context!!, user, activity!!)
                        repository.updateImage(object : MessageCallback {
                            override fun onSuccess(successMessage: String) {
                                mView.loading.visibility = View.GONE
                                Message.snackbarMessage(context!!, mView, successMessage)
                                Utils.writeSharedPreferences(
                                    Constants.IMAGE_PROF,
                                    image.toString(), Constants.IMAGE_PROF,
                                    activity!!
                                )
                                Glide.with(context!!).load(loadImage())
                                    .apply(RequestOptions.circleCropTransform())
                                    .into(view!!.profile_image)
                            }

                            override fun onError(errorMessage: String) {
                                mView.loading.visibility = View.GONE
                                Message.snackbarMessage(context!!, mView, errorMessage)
                            }

                        })
                    }
                    .setCancelClickListener { sDialog ->
                        sDialog.dismissWithAnimation()
                    }
                    .show()
            }

        }
    }

}