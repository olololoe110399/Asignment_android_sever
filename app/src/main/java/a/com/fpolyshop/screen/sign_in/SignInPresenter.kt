package a.com.fpolyshop.screen.sign_in

import a.com.fpolyshop.data.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SignInPresenter(private val repository: Repository) : SignInContract.Presenter {
    private var view: SignInContract.View? = null
    private val compositeDisposable = CompositeDisposable()

    override fun signIn(username: String, password: String) {
        view?.onLoading(true)
        val disposable: Disposable = repository.signIn(username, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                view?.onLoading(false)
            }
            .subscribe({ response ->
                view?.onSignInUserSuccess(response.status)
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

    override fun setView(view: SignInContract.View?) {
        this.view = view
    }

}