package a.com.fpolyshop.screen.edit_profile

import a.com.fpolyshop.data.models.User
import a.com.fpolyshop.data.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class EditProfilePresenter(private val repository: Repository) : EditProfileContract.Presenter {
    private var view: EditProfileContract.View? = null
    private val compositeDisposable = CompositeDisposable()

    override fun postProfile(user: User) {
        view?.onLoading(true)
        val disposable: Disposable = repository.postProfile(user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                view?.onLoading(false)
            }
            .subscribe({ data ->
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

    override fun setView(view: EditProfileContract.View?) {
        this.view = view
    }

}