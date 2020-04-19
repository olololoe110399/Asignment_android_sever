package a.com.fpolyshop.data.source.remote.api

import a.com.fpolyshop.utils.Constant
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkService {
    private object HOLDER {
        val INSTANCE: ApiRequest = retrofitBuilder().create(ApiRequest::class.java)
    }

    companion object {
        val instance: ApiRequest by lazy {
            HOLDER.INSTANCE
        }
        private val client = OkHttpClient.Builder()
            .readTimeout(Constant.BASE_TIME_OUT, TimeUnit.MILLISECONDS)
            .writeTimeout(Constant.BASE_TIME_OUT, TimeUnit.MILLISECONDS)
            .connectTimeout(Constant.BASE_TIME_OUT, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(Interceptor { chain ->
                chain.proceed(buildRequest(chain))
            })
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        fun retrofitBuilder(): Retrofit = Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()


        private fun buildRequest(chain: Interceptor.Chain) = chain.request()
            .newBuilder()
            .url(addApiKey(chain))
            .build()

        private fun addApiKey(chain: Interceptor.Chain) = chain.request().url
            .newBuilder()
            .addQueryParameter("", "")
            .build()
    }
}
