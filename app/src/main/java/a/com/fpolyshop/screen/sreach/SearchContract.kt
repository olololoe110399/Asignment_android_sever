package a.com.fpolyshop.screen.sreach

import a.com.fpolyshop.data.models.Category
import a.com.fpolyshop.data.models.Genre
import a.com.fpolyshop.data.models.Producer
import a.com.fpolyshop.screen.base.BasePresenter

interface SearchContract {
    interface View {
        fun onShowGenresSuccess(genres: List<Genre>)
        fun onShowCategorySuccess(category: List<Category>)
        fun onShowProducersSuccess(producers: List<Producer>)
        fun onError(str: String?)
        fun onLoading(type: String, isLoad: Boolean)
    }

    interface Presenter : BasePresenter<View?> {
        fun getCategory()
        fun getProducer()
        fun getGenres()
    }
}
