package a.com.fpolyshop.screen.profile

import a.com.fpolyshop.R
import a.com.fpolyshop.data.models.User
import a.com.fpolyshop.data.repository.Repository
import a.com.fpolyshop.data.source.local.LocalDataSource
import a.com.fpolyshop.data.source.remote.RemoteDataSource
import a.com.fpolyshop.utils.*
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment private constructor() : Fragment(), ProfileContract.View {
    private lateinit var presenter: ProfileContract.Presenter
    var profile: User? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        context?.let {
            val movieRepository: Repository =
                Repository.getInstance(
                    RemoteDataSource.instance,
                    LocalDataSource.instance
                )
            presenter = ProfilePresenter(movieRepository)
        }
        presenter.setView(this)
        return inflater.inflate(R.layout.fragment_profile, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            val userNameSp = it.getSharedPreferences(
                "USER_FILE",
                Context.MODE_PRIVATE
            ).getString("USERNAME", "")
            val passwordSp = it.getSharedPreferences(
                "USER_FILE",
                Context.MODE_PRIVATE
            ).getString("PASSWORD", "")
            if (NetworkUtil.isConnectedToNetwork(it)) {
                if (userNameSp != null && passwordSp != null) {
                    Handler().postDelayed({ presenter.getProfile(userNameSp, passwordSp) }, 500)
                }
            } else {
                onLoading(false)
                val message = getString(R.string.check_internet_fail)
                Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
            }
        }
        initRefresh()
    }

    override fun onStatusSuccess(status: String) {
        if (status != "") {
            activity?.showSnackBarMsg(status)
        }
    }

    override fun onGetUserSuccess(user: User) {
        profile = user
        view?.run {
            usernameEdt.text = user.username.toEditable()
            full_nameEdt.text = user.full_name.toEditable()
            birthEdt.text = user.date.toEditable()
            phoneEdt.text = user.phone.toEditable()
            addressEdt.text = user.address.toEditable()
            GlideApp
                .with(avatar)
                .load(Constant.BASE_URL + Constant.BASE_URL_IMAGE + user.image_path)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .centerCrop()
                .into(avatar)
        }
    }

    override fun onError(str: String?) {
        str?.let {
            Toast.makeText(activity, it, Toast.LENGTH_LONG)
                .show()
        }
    }

    override fun onLoading(isLoad: Boolean) {
        view?.run {
            if (isLoad) {
                frameProgressBar.visibility = View.VISIBLE
            } else {
                frameProgressBar.visibility = View.GONE
            }
        }
    }

    private fun initRefresh() {
        view?.homeSwipeRefresh?.setOnRefreshListener {
            activity?.let {
                val userNameSp = it.getSharedPreferences(
                    "USER_FILE",
                    Context.MODE_PRIVATE
                ).getString("USERNAME", "")
                val passwordSp = it.getSharedPreferences(
                    "USER_FILE",
                    Context.MODE_PRIVATE
                ).getString("PASSWORD", "")
                if (NetworkUtil.isConnectedToNetwork(it)) {
                    if (userNameSp != null && passwordSp != null) {
                        homeSwipeRefresh.isRefreshing = false
                        presenter.getProfile(userNameSp, passwordSp)
                    }
                } else {
                    homeSwipeRefresh.isRefreshing = false
                    onLoading(false)
                    val message = getString(R.string.check_internet_fail)
                    Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun getUserProfile(): User? {
        return profile
    }

    private object HOLDER {
        val INSTANCE = ProfileFragment()
    }

    companion object {
        val instance: ProfileFragment by lazy {
            HOLDER.INSTANCE
        }
    }
}