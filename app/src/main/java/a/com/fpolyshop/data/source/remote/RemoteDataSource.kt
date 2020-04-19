package a.com.fpolyshop.data.source.remote

import a.com.fpolyshop.data.models.User
import a.com.fpolyshop.data.source.DataSource
import a.com.fpolyshop.data.source.remote.api.ApiRequest
import a.com.fpolyshop.data.source.remote.api.NetworkService
import a.com.fpolyshop.data.source.remote.response.*
import a.com.fpolyshop.utils.Constant
import io.reactivex.Observable


class RemoteDataSource private constructor(private val apiRequest: ApiRequest) : DataSource.Remote {

    override fun signIn(
        username: String,
        password: String
    ): Observable<StatusResponse> {
        return apiRequest.postSignIn(username, password)
    }

    override fun postSignUp(user: User): Observable<StatusResponse> {
        return apiRequest.postSignUp(user)
    }

    override fun postProfile(user: User): Observable<StatusResponse> {
        return apiRequest.postProfile(user)
    }

    override fun getProfile(
        username: String,
        password: String
    ): Observable<UserResponse> {
        return apiRequest.getProfile(username, password)
    }

    override fun getGenres(): Observable<GenreResponse> {
        return apiRequest.getGenres()
    }

    override fun getProducers(): Observable<ProducerResponse> {
        return apiRequest.getProducers()
    }

    override fun getProducts(type: String, query: String): Observable<ProductResponse> {
        return when (type) {
            Constant.BASE_DISCOVER_GENRE -> apiRequest.getProductsByGenre(query)
            Constant.BASE_DISCOVER_PRODUCER -> apiRequest.getProductsByProducer(query)
            Constant.BASE_SEARCH -> apiRequest.getProductsByQuery(query)
            else -> apiRequest.getProductsByCategory(type)
        }
    }

    private object HOLDER {
        val INSTANCE = RemoteDataSource(NetworkService.instance)
    }

    companion object {
        val instance: RemoteDataSource by lazy {
            HOLDER.INSTANCE
        }
    }
}
