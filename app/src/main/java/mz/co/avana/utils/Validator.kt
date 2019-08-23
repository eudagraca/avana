package mz.co.avana.utils

import com.google.android.material.textfield.TextInputLayout

class Validator {

    companion object {

        fun validate(input: TextInputLayout) = input.editText!!.text.isEmpty()
        fun validateLength(input: TextInputLayout) = input.editText!!.text.toString().length < 5
        fun validatePrice(input: TextInputLayout) = input.editText!!.text.toString().toDouble() < 0
    }
}