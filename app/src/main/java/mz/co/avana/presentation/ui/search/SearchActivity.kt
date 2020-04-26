package mz.co.avana.presentation.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.search_bar.*
import mz.co.avana.R

class SearchActivity : AppCompatActivity() {
    var oldSearch: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        search.isFocusable = true
        search.isEnabled = true
        search.isFocusableInTouchMode = true
        search.isClickable = true
        search.isCursorVisible = true
        search.hint = getString(R.string.what_are_you_look)

        materialButtonBack.setBackgroundColor(ContextCompat.getColor(this, R.color.md_white_1000))
        materialButtonBack.background = getDrawable(R.drawable.ic_left_arrow)
        materialButtonBack.backgroundTintList =
            ContextCompat.getColorStateList(baseContext, R.color.md_black_1000)
        toolbar.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.md_white_1000))

        oldSearch = intent.getStringExtra("search")
        if (oldSearch.toString().isNotEmpty()) {
            search.setText(oldSearch)
        }

        search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val intent = Intent(this@SearchActivity, ItemSearchedActivity::class.java)
                intent.putExtra("item", search.text.toString())
                startActivity(intent)
                true
            } else false
        }

        materialButtonBack.setOnClickListener {
            onBackPressed()
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
