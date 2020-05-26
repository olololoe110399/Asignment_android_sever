package a.com.fpolyshop.screen.edit_profile

import a.com.fpolyshop.data.models.User
import a.com.fpolyshop.screen.base.BasePresenter

interface EditProfileContract {
    interface View {
        fun onStatusSuccess(status: String)
        fun onError(str: String?)
        fun onLoading(isLoad: Boolean)
    }

    interface Presenter : BasePresenter<View?> {
        fun postProfile(user: User)
    }

}