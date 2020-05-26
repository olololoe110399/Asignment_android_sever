package a.com.fpolyshop.screen.home

import a.com.fpolyshop.data.repository.Repository
import a.com.fpolyshop.utils.Constant
import a.com.fpolyshop.utils.EnumCategory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class HomePresenter(private val repository: Repository) : HomeContract.Presenter {
    private var view: HomeContract.View? = null
    private val compositeDisposable = CompositeDisposable()

    override fun getGenres() {
        val disposable: Disposable = repository.getGenres()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
            }
            .subscribe({ data ->
                view?.onShowGenresSuccess(data.genres)
            },
                { throwable ->
                    view?.onError(throwable.message.toString())
                })
        compositeDisposable.add(disposable)
    }

    override fun getProducts(type: String, query: String) {
        view?.onLoading(type,true)
        val disposable: Disposable = repository.getProducts(type,query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                view?.onLoading(type,false)
            }
            .subscribe({ data ->
                view?.onShowProductsSuccess(type ,data.products)
            },
                { throwable ->
                    view?.onError(throwable.message.toString())
                })
        compositeDisposable.add(disposable)
    }

    override fun onStart() {
        getGenres()
        getProducts(EnumCategory.HOT.type)
        getProducts(EnumCategory.SELLING.type)
        getProducts(EnumCategory.GOOD_PRICE.type)
        getProducts(EnumCategory.PROMOTION.type)
        getProducts(EnumCategory.TOP_RATED.type)
    }

    override fun onStop() {
        TODO("Not yet implemented")
    }

    override fun setView(view: HomeContract.View?) {
        this.view = view
    }
}