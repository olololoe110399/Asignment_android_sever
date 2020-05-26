package a.com.fpolyshop.screen.home

import a.com.fpolyshop.data.models.Genre
import a.com.fpolyshop.data.models.Producer
import a.com.fpolyshop.data.models.Product
import a.com.fpolyshop.screen.base.BasePresenter
import a.com.fpolyshop.utils.Constant
import a.com.fpolyshop.utils.EnumCategory

interface HomeContract {
    interface View {
        fun onShowGenresSuccess(genres: List<Genre>)
        fun onShowProductsSuccess(type: String, products: List<Product>)
        fun onError(str: String?)
        fun onLoading(type: String, isLoad: Boolean)
    }

    interface Presenter : BasePresenter<View?> {
        fun getGenres()
        fun getProducts(
            type: String,
            query: String = Constant.BASE_QUERY_DEFAULT
        )
    }
}
