package a.com.fpolyshop.data.source.remote.api

import a.com.fpolyshop.data.models.User
import a.com.fpolyshop.data.source.remote.response.*
import io.reactivex.Observable
import retrofit2.http.*

interface ApiRequest {

    @POST("api/sign_in")
    fun postSignIn(
        @Query("username") username: String,
        @Query("password") password: String
    ): Observable<StatusResponse>

    @POST("api/sign_up")
    fun postSignUp(@Body user: User): Observable<StatusResponse>

    @POST("api/update_profile")
    fun postProfile(@Body user: User): Observable<StatusResponse>

    @POST("api/profile")
    fun getProfile(
        @Query("username") username: String,
        @Query("password") password: String
    ): Observable<UserResponse>

    @GET("api/genre/list")
    fun getGenres(): Observable<GenreResponse>

    @GET("api/producer/list")
    fun getProducers(): Observable<ProducerResponse>

    @GET("api/{category}")
    fun getProductsByCategory(@Path("category") category: String): Observable<ProductResponse>

    @GET("api/discover/product")
    fun getProductsByGenre(@Query("with_genre") query: String): Observable<ProductResponse>

    @GET("api/discover/product")
    fun getProductsByProducer(@Query("with_producer") query: String): Observable<ProductResponse>

    @GET("api/search/product")
    fun getProductsByQuery(@Query("query") query: String): Observable<ProductResponse>
}
