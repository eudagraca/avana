package mz.co.avana.callbacks

interface ItemPriceCallback {
    fun values(min: String, max: String, item: String)
    fun categories(name: String, databaseName: String)
}