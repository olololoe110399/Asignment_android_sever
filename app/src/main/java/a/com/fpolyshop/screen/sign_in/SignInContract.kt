package a.com.fpolyshop.screen.sign_in

import a.com.fpolyshop.screen.base.BasePresenter

interface SignInContract {
    interface View {
        fun onSignInUserSuccess(status: String)
        fun onError(str: String?)
        fun onLoading(isLoad: Boolean)
    }

    interface Presenter : BasePresenter<View?> {
        fun signIn(username: String, password: String)
    }
}