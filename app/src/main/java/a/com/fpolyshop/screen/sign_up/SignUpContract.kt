package a.com.fpolyshop.screen.sign_up

import a.com.fpolyshop.data.models.User
import a.com.fpolyshop.screen.base.BasePresenter

interface SignUpContract {
    interface View {
        fun onSignUpUserSuccess(status: String)
        fun onError(str: String?)
        fun onLoading(isLoad: Boolean)
    }

    interface Presenter : BasePresenter<View?> {
        fun postSignUp(user: User)
    }

}