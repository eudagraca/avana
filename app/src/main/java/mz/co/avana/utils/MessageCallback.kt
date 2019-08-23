package mz.co.avana.utils

interface MessageCallback {

    fun onSuccess(successMessage: String)
    fun onError(errorMessage: String)
}