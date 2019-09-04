package mz.co.avana.presentation.ui.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColor
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_image_dialog.*
import mz.co.avana.R
import mz.co.avana.model.Item

class ImageDialogFragment : DialogFragment(), View.OnClickListener {

    companion object {
        const val KEY = "item_image"
        const val COUNT_PLUS = 1
        const val COUNT_LESS = -1
    }

    lateinit var item: Item
    var imagePosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(
            STYLE_NORMAL,
            android.R.style.Theme_Black_NoTitleBar_Fullscreen
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(
            R.layout.fragment_image_dialog,
            container, false
        )
    }
    /*
     * Os acessos as Views estão no método onResume(), pois somente
     * depois de onCreateView() é que podemos utilizar a sintaxe
     * do plugin kotlin-android-extensions, sem necessidade de uso
     * do método findViewById().
     * */
    override fun onResume() {
        super.onResume()

        /*
         * Acessando dados enviados de CarAdapter depois do
         * acionamento de algum item em lista.
         * */
        item = arguments!!.getParcelable<Item>(KEY) as Item
        imagePosition = arguments!!.getInt(Item.KEY_IMAGE)

        iv_close.setOnClickListener(this)
        iv_arrow_left.setOnClickListener(this)
        iv_arrow_right.setOnClickListener(this)

        setImage(false)

        /*
         * Ampliando a capacidade de zoom máximo na imagem.
         * */
        iv_image.maximumScale = 5.0F
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_arrow_left -> prevImage()
            R.id.iv_arrow_right -> nextImage()
            R.id.iv_close -> close()
        }
    }

    private fun prevImage() {
        setImage(true, COUNT_LESS)
    }

    private fun nextImage() {
        setImage(true, COUNT_PLUS)
    }

    private fun setImage(applyCount: Boolean, typeCount: Int = COUNT_PLUS) {

        /*
         * Verificação se deve ou não ser aplicado o contador a
         * propriedade imagePosition, pois logo na abertura do
         * DialogFragment não há necessidade de invocar o algoritmo
         * contador, posteriormente, na mudança de imagem em tela, o
         * contador deve ser invocado para o correto cálculo de qual
         * botão passador de imagem poderá ou não ficar em tela.
         * */
        if (applyCount) {
            imagePosition += when (typeCount) {
                COUNT_PLUS -> 1
                else -> -1
            }
        }

        Glide
            .with(context!!)
            .load(item.images!![imagePosition])
            .into(iv_image)

        verifyButtons()
    }

    /*
     * Verificação se os botões passadores de imagem podem ou não
     * permanecer em tela de acordo com a posição atual da imagem
     * em exibição.
     * */
    private fun verifyButtons() {
        iv_arrow_left.visibility =
            if (imagePosition == 0)
                View.GONE
            else
                View.VISIBLE

        iv_arrow_right.visibility =
            if (imagePosition == item.images!!.size - 1)
                View.GONE
            else
                View.VISIBLE
    }

    private fun close() {
        dismiss()
    }
}