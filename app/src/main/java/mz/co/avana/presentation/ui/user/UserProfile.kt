package mz.co.avana.presentation.ui.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.fragment_user_profile.view.*
import mz.co.avana.R
import mz.co.avana.presentation.ui.item.ItemAdapter
import mz.co.avana.presentation.ui.item.ItemDetailsActivity
import mz.co.avana.utils.Constants
import mz.co.avana.utils.Message
import mz.co.avana.utils.MessageCallback
import mz.co.avana.utils.Utils
import mz.co.avana.viewModel.item.ItemViewModel
import mz.co.avana.viewModel.user.UserViewModel


class UserProfile : Fragment(), MessageCallback {
    lateinit var mView: View
    lateinit var snackbar: Snackbar

    override fun onSuccess(successMessage: String) {
        userData()
        Message.snackbarMessage(context!!, mView, successMessage)
    }

    override fun onError(errorMessage: String) {
        Message.snackbarMessage(context!!, mView, errorMessage)
    }

    lateinit var name: String
    lateinit var surname: String
    lateinit var location: String

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        mView = inflater.inflate(R.layout.fragment_user_profile, container, false)

        userData()

        val itemViewModel = ViewModelProviders.of(this).get(ItemViewModel::class.java)
        itemViewModel.itemLiveDataList.observe(this, Observer { items ->
            nr_promotions.text = resources.getString(R.string.show_nr_promotions, items.size.toString(),
                    resources.getString(R.string.promotions))
            with(mView.rv_my_itens, {
                layoutManager = GridLayoutManager(context, 2)
                setHasFixedSize(true)
                adapter = ItemAdapter(items, context) { item ->
                    val intent = Intent(activity, ItemDetailsActivity::class.java)
                    intent.putExtra(Constants.ITEM, item)
                    startActivity(intent)
                }
            })
        })
        itemViewModel.itemsUser()

        mView.edit_name.setOnClickListener {
            openNameEditDialog()
        }

        mView.edit_location.setOnClickListener {
            openLocationEditDialog()
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

    private fun openLocationEditDialog() {
        val bundle = Bundle()
        val newFragment = EditLocationDialog(this)
        bundle.putString(Constants.LOCATION, location)
        newFragment.arguments = bundle
        newFragment.show(childFragmentManager, "missiles")
    }

    private fun userData() {
        val userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        userViewModel.userLiveData.observe(this, Observer {
            it?.let { user ->
                username.text = resources.getString(R.string.show_username, user.name, user.surname)
                city_location.text = user.location
                name = user.name
                surname = user.surname!!
                location = user.location!!
            }
        })
        userViewModel.userData()
    }

}