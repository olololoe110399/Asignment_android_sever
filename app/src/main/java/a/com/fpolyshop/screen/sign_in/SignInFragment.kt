package a.com.fpolyshop.screen.sign_in

import a.com.fpolyshop.R
import a.com.fpolyshop.data.repository.Repository
import a.com.fpolyshop.data.source.local.LocalDataSource
import a.com.fpolyshop.data.source.remote.RemoteDataSource
import a.com.fpolyshop.screen.container.ContainerFragment
import a.com.fpolyshop.utils.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_sign_in.view.*

class SignInFragment : Fragment(), SignInContract.View {
    private lateinit var presenter: SignInContract.Presenter
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
            presenter = SignInPresenter(movieRepository)
        }
        presenter.setView(this)
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
    }

    private fun onClick() {
        view?.loginBtn?.setOnClickListener {
            checkLogin()
        }
    }

    private object HOLDER {
        val INSTANCE = SignInFragment()
    }

    companion object {
        val instance: SignInFragment by lazy {
            HOLDER.INSTANCE
        }
    }

    override fun onSignInUserSuccess(status: String) {
        if (status == "Success") {
            view?.run {
                activity?.rememberUser(
                    usernameEdt.text.toString(),
                    passwordEdt.text.toString(),
                    checkPass.isChecked
                )
            }
            activity?.run {
                removeFragmentContainer()
                ContainerFragment.startingPosition = 0
                activity?.loadNoBackStack(ContainerFragment.instance)
            }
        } else {
            activity?.showSnackBarMsg(status)
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

    private fun checkLogin() {
        view?.run {
            activity?.let {
                if (usernameEdt.text.toString() == "" || passwordEdt.text.toString() == ""
                ) {
                    it.showSnackBar(R.string.valid_null)
                } else if (!validateEmail(usernameEdt.text.toString())) {
                    it.showSnackBar(R.string.valid_email)
                } else if (passwordEdt.text.toString().length < 8) {
                    it.showSnackBar(R.string.valid_pass)
                } else {
                    if (NetworkUtil.isConnectedToNetwork(it)) {
                        presenter.signIn(
                            usernameEdt.text.toString().trim(),
                            passwordEdt.text.toString().trim()
                        )
                    } else {
                        val message = getString(R.string.check_internet_fail)
                        Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }

    private fun validateEmail(email: String): Boolean {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true
        }
        return false
    }


}