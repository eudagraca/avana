package mz.co.avana.utils

import com.google.android.material.textfield.TextInputLayout

class Validator {

    companion object {

        private const val EMAIL_REGEX = "^[A-Za-z](.*)([@])(.+)(\\.)(.+)"
        fun validateEmail(email: String): Boolean {
            return EMAIL_REGEX.toRegex().matches(email)
        }
        fun validate(input: TextInputLayout) = input.editText!!.text.isEmpty()
        fun validateLength(input: TextInputLayout) = input.editText!!.text.toString().length < 3
        fun validatePrice(input: TextInputLayout) = input.editText!!.text.toString().toDouble() < 0
        fun  validatePassword(input: TextInputLayout) = input.editText!!.text.toString().length < 6
    }
}