package a.com.fpolyshop.data.source

import a.com.fpolyshop.data.models.Category
import a.com.fpolyshop.data.models.User
import a.com.fpolyshop.data.source.remote.response.*
import io.reactivex.Flowable
import io.reactivex.Observable

interface DataSource {
    /**
     * Local
     */
    interface Local {
        fun getCategories(): Flowable<List<Category>>

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

        fun getGenres(): Observable<GenreResponse>

        fun getProducers(): Observable<ProducerResponse>

        fun getProducts(type: String, query: String): Observable<ProductResponse>
    }
}
