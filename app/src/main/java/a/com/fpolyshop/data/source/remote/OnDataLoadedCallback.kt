package a.com.fpolyshop.data.source.remote

interface OnDataLoadedCallback<T> {
    fun onSuccess(data: T?)
    fun onError(e: Exception)
}
