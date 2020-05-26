package a.com.fpolyshop.screen.profile

import a.com.fpolyshop.data.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ProfilePresenter(private val repository: Repository) : ProfileContract.Presenter {
    private var view: ProfileContract.View? = null
    private val compositeDisposable = CompositeDisposable()

    override fun getProfile(username: String, password: String) {
        view?.onLoading(true)
        val disposable: Disposable = repository.getProfile(username, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                view?.onLoading(false)
            }
            .subscribe({ data ->
                view?.onGetUserSuccess(data.user)
                view?.onStatusSuccess(data.status)
            },
                { throwable ->
                    view?.onError(throwable.message.toString())
                })
        compositeDisposable.add(disposable)
    }

    override fun onStart() {
        TODO("Not yet implemented")
    }

    override fun onStop() {
        TODO("Not yet implemented")
    }

    override fun setView(view: ProfileContract.View?) {
        this.view = view
    }

}