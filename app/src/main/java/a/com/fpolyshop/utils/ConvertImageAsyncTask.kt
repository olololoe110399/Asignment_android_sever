package a.com.fpolyshop.utils

import android.graphics.Bitmap
import android.os.AsyncTask
import android.util.Base64
import java.io.ByteArrayOutputStream

class ConvertImageAsyncTask(private var listener: OnConvertImageListener) :
    AsyncTask<Bitmap?, Void?, String?>() {

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        try {
            listener.onImageLoaded(result)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun convertImage(bitmap: Bitmap): String? {
        return try {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)

            val byteArray = byteArrayOutputStream.toByteArray()
            return Base64.encodeToString(byteArray, Base64.DEFAULT)
        } catch (e: Exception) {
            listener.onImageError(e)
            null
        }
    }

    override fun doInBackground(vararg params: Bitmap?): String? {
        return params[0]?.let { convertImage(it) }
    }
}
