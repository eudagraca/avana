package mz.co.avana.presentation.ui.categorie

import android.content.Context
import mz.co.avana.R
import mz.co.avana.model.Categories

object CategoriesList {

        fun categories(context: Context) = listOf(
            Categories(R.drawable.food, context.resources.getString(R.string.alimentos), "Alimentos"),
            Categories(R.drawable.car, context.resources.getString(R.string.automóveis), "Automóveis"),
            Categories(R.drawable.home, context.resources.getString(R.string.casa), "Casa"),
            Categories(R.drawable.phone, context.resources.getString(R.string.Celulares), "Celulares"),
            Categories(R.drawable.computer, context.resources.getString(R.string.computadores), "Computadores"),
            Categories(R.drawable.baby, context.resources.getString(R.string.criançasebebés), "Crianças e bebés"),
            Categories(R.drawable.ball, context.resources.getString(R.string.desporto), "Desporto"),
            Categories(R.drawable.education, context.resources.getString(R.string.educação), "Educação"),
            Categories(R.drawable.microwave, context.resources.getString(R.string.eletrodomésticos), "Eletrodomésticos"),
            Categories(R.drawable.more, context.resources.getString(R.string.outros), "Outros"),
            Categories(R.drawable.cosmetics, context.resources.getString(R.string.saúdeebeleza), "Saúde e Beleza"),
            Categories(R.drawable.clothes, context.resources.getString(R.string.vestuário), "Vestuário")
        )


//    fun categorieSelected(context: Context, name: String, nameDatabase: String): Categories{
//        listOf(
//            Categories(R.drawable.ic_foood, context.resources.getString(R.string.alimentos), "Alimentos"),
//            Categories(R.drawable.ic_car, context.resources.getString(R.string.automóveis), "Automóveis"),
//            Categories(R.drawable.ic_house, context.resources.getString(R.string.casa), "Casa"),
//            Categories(R.drawable.ic_mobilephone, context.resources.getString(R.string.Celulares), "Celulares"),
//            Categories(R.drawable.ic_informatic, context.resources.getString(R.string.computadores), "Computadores"),
//            Categories(R.drawable.ic_baby, context.resources.getString(R.string.criançasebebés), "Crianças e bebés"),
//            Categories(R.drawable.ic_sport, context.resources.getString(R.string.desporto), "Desporto"),
//            Categories(R.drawable.ic_education, context.resources.getString(R.string.educação), "Educação"),
//            Categories(R.drawable.ic_eletrodomestics, context.resources.getString(R.string.eletrodomésticos), "Eletrodomésticos"),
//            Categories(R.drawable.ic_more, context.resources.getString(R.string.outros), "Outros"),
//            Categories(R.drawable.ic_cosmetics, context.resources.getString(R.string.saúdeebeleza), "Saúde e Beleza"),
//            Categories(R.drawable.ic_clothes, context.resources.getString(R.string.vestuário), "Vestuário")
//        )
//
//        return Categories()
//    }
//
//    private fun getStringResource(context: Context, resName: String, c: Class<*>): String {
//        return try {
//            val idField = c.getDeclaredField(resName)
//            context.resources.getString(idField.getInt(idField))
//        } catch (e: Exception) {
//            e.printStackTrace()
//            ""
//        }
//
//    }
}