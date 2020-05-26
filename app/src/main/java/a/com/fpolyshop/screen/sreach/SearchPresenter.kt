package a.com.fpolyshop.screen.sreach

import a.com.fpolyshop.data.repository.Repository
import a.com.fpolyshop.utils.Constant
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SearchPresenter(private val repository: Repository) : SearchContract.Presenter {
    private var view: SearchContract.View? = null
    private val compositeDisposable = CompositeDisposable()
    override fun getCategory() {
        view?.onLoading(Constant.BASE_CATEGORY, true)
        val disposable: Disposable = repository.getCategories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                view?.onLoading(Constant.BASE_CATEGORY, false)
            }
            .subscribe({ data ->
                view?.onShowCategorySuccess(data)
            },
                { throwable ->
                    view?.onError(throwable.message.toString())
                })
        compositeDisposable.add(disposable)
    }

    override fun getProducer() {
        view?.onLoading(Constant.BASE_DISCOVER_PRODUCER, true)
        val disposable: Disposable = repository.getProducers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                view?.onLoading(Constant.BASE_DISCOVER_PRODUCER, false)
            }
            .subscribe({ data ->
                view?.onShowProducersSuccess(data.producers)
            },
                { throwable ->
                    view?.onError(throwable.message.toString())
                })
        compositeDisposable.add(disposable)
    }

    override fun getGenres() {
        view?.onLoading(Constant.BASE_DISCOVER_GENRE, true)
        val disposable: Disposable = repository.getGenres()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                view?.onLoading(Constant.BASE_DISCOVER_GENRE, false)
            }
            .subscribe({ data ->
                view?.onShowGenresSuccess(data.genres)
            },
                { throwable ->
                    view?.onError(throwable.message.toString())
                })
        compositeDisposable.add(disposable)
    }

    override fun onStart() {
        getGenres()
        getCategory()
        getProducer()
    }

    override fun onStop() {
        TODO("Not yet implemented")
    }

    override fun setView(view: SearchContract.View?) {
        this.view = view
    }

}