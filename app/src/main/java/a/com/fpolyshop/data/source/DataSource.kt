package a.com.fpolyshop.data.source

import a.com.fpolyshop.data.models.User
import a.com.fpolyshop.data.source.remote.response.StatusResponse
import a.com.fpolyshop.data.source.remote.response.UserResponse
import io.reactivex.Observable

interface DataSource {
    /**
     * Local
     */
    interface Local {

    }

    /**
     * Remote
     */
    interface Remote {
        fun signIn(
            username: String,
            password: String
        ): Observable<StatusResponse>

        fun postSignUp(user: User): Observable<StatusResponse>

        fun postProfile(user: User): Observable<StatusResponse>

        fun getProfile(
            username: String, password: String
        ): Observable<UserResponse>
    }
}
