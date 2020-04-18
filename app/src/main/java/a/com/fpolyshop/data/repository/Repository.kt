package a.com.fpolyshop.data.repository

import a.com.fpolyshop.data.models.User
import a.com.fpolyshop.data.source.DataSource
import a.com.fpolyshop.data.source.remote.response.StatusResponse
import a.com.fpolyshop.data.source.remote.response.UserResponse
import io.reactivex.Observable

class Repository private constructor(
    private val remote: DataSource.Remote,
    private val local: DataSource.Local
) {
    fun signIn(username: String, password: String): Observable<StatusResponse> {
        return remote.signIn(username, password)
    }

    fun postSignUp(user: User): Observable<StatusResponse> {
        return remote.postSignUp(user)
    }

    fun postProfile(user: User): Observable<StatusResponse> {
        return remote.postProfile(user)
    }

    fun getProfile(
        username: String,
        password: String
    ): Observable<UserResponse> {
        return remote.getProfile(username, password)
    }

    companion object {
        private var instance: Repository? = null
        fun getInstance(
            RemoteDataSource: DataSource.Remote,
            LocalDataSource: DataSource.Local
        ) = instance ?: Repository(
            RemoteDataSource,
            LocalDataSource
        ).also { instance = it }
    }
}
