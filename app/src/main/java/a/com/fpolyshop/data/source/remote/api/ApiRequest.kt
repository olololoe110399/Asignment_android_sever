package a.com.fpolyshop.data.source.remote.api

import a.com.fpolyshop.data.models.User
import a.com.fpolyshop.data.source.remote.response.StatusResponse
import a.com.fpolyshop.data.source.remote.response.UserResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiRequest {

    @POST("API/api_sign_in")
    fun postSignIn(
        @Query("username") username: String,
        @Query("password") password: String
    ): Observable<StatusResponse>

    @POST("API/api_sign_up")
    fun postSignUp(@Body user: User): Observable<StatusResponse>

    @POST("API/api_update_profile")
    fun postProfile(@Body user: User): Observable<StatusResponse>

    @POST("API/api_profile")
    fun getProfile(
        @Query("username") username: String,
        @Query("password") password: String
    ): Observable<UserResponse>
}
