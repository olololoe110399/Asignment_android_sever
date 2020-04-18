package a.com.fpolyshop.utils

interface OnConvertImageListener {
    fun onImageLoaded(base64: String?)
    fun onImageError(e: Exception?)
}