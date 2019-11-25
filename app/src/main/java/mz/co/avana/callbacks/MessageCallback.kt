package mz.co.avana.callbacks

interface MessageCallback {

    fun onSuccess(successMessage: String)
    fun onError(errorMessage: String)
}