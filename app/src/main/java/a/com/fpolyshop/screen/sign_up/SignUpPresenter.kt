package a.com.fpolyshop.screen.sign_up

import a.com.fpolyshop.data.models.User
import a.com.fpolyshop.data.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SignUpPresenter(private val repository: Repository) : SignUpContract.Presenter {
    private var view: SignUpContract.View? = null
    private val compositeDisposable = CompositeDisposable()

    override fun postSignUp(user: User) {
        view?.onLoading(true)
        val disposable: Disposable = repository.postSignUp(user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                view?.onLoading(false)
            }
            .subscribe({ response ->
                view?.onSignUpUserSuccess(response.status)
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

    override fun setView(view: SignUpContract.View?) {
        this.view = view
    }

}