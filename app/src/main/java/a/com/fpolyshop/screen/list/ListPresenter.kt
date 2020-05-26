package a.com.fpolyshop.screen.list

import a.com.fpolyshop.data.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ListPresenter(private val repository: Repository) : ListContract.Presenter {
    private var view: ListContract.View? = null
    override fun getProducts(type: String, query: String) {
        view?.onLoading(true)
        val disposable: Disposable = repository.getProducts(type, query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                view?.onLoading(false)
            }
            .subscribe({ data ->
                view?.onShowProductsSuccess(data.products)
            },
                { throwable ->
                    view?.onError(throwable.message.toString())
                })
        CompositeDisposable().add(disposable)
    }


    override fun onStart() {
        TODO("Not yet implemented")
    }

    override fun onStop() {
        TODO("Not yet implemented")
    }

    override fun setView(view: ListContract.View?) {
        this.view = view
    }
}