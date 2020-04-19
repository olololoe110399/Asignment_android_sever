package a.com.fpolyshop.screen.list

import a.com.fpolyshop.data.models.Product
import a.com.fpolyshop.screen.base.BasePresenter
import a.com.fpolyshop.utils.Constant

interface ListContract {
    /**
     * View
     */
    interface View {
        fun onShowProductsSuccess(products: List<Product>)
        fun onError(str: String?)
        fun onLoading(isLoad: Boolean)
    }

    /**
     * Presenter
     */
    interface Presenter : BasePresenter<View?> {
        fun getProducts(
            type: String,
            query: String = Constant.BASE_QUERY_DEFAULT
        )
    }
}