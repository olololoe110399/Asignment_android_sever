package a.com.fpolyshop.utils

import android.content.Context
import android.net.ConnectivityManager

object NetworkUtil {
    fun isConnectedToNetwork(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return connectivityManager?.activeNetworkInfo?.isConnectedOrConnecting == true
    }
}
