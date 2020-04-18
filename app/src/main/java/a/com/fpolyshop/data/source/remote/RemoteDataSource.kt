package a.com.fpolyshop.data.source.remote

import a.com.fpolyshop.data.models.User
import a.com.fpolyshop.data.source.DataSource
import a.com.fpolyshop.data.source.remote.api.ApiRequest
import a.com.fpolyshop.data.source.remote.api.NetworkService
import a.com.fpolyshop.data.source.remote.response.StatusResponse
import a.com.fpolyshop.data.source.remote.response.UserResponse
import io.reactivex.Observable


class RemoteDataSource private constructor(private val apiRequest: ApiRequest) : DataSource.Remote {

    companion object {
        private var instance: RemoteDataSource? = null
        fun getInstance() =
            instance ?: RemoteDataSource(NetworkService.getInstance()).also { instance = it }
    }

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
}
