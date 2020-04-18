package a.com.fpolyshop.screen.profile

import a.com.fpolyshop.data.models.User
import a.com.fpolyshop.screen.base.BasePresenter

interface ProfileContract {
    interface View {
        fun onStatusSuccess(status: String)
        fun onGetUserSuccess(user: User)
        fun onError(str: String?)
        fun onLoading(isLoad: Boolean)
    }

    interface Presenter : BasePresenter<View?> {
        fun getProfile(username: String, password: String)
    }

}