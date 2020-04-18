package a.com.fpolyshop.data.source.local.base

import a.com.fpolyshop.R
import a.com.fpolyshop.data.source.remote.OnDataLoadedCallback
import android.content.res.Resources
import android.os.AsyncTask

class LocalAsyncTask<P, T>(
    private val handler: LocalDataHandler<P, T>,
    private val callback: OnDataLoadedCallback<T>
) : AsyncTask<P, Void, T?>() {

    private var exception: Exception? = null

    override fun doInBackground(vararg params: P): T? {
        return try {
            handler.execute(params[0]) ?: throw Exception()
        } catch (e: Exception) {
            exception = e
            null
        }
    }

    override fun onPostExecute(result: T?) {
        result?.let {
            callback.onSuccess(result)
        } ?: callback.onError(
            exception ?: Exception(
                Resources.getSystem().getString(R.string.message_null_result)
            )
        )
    }
}
