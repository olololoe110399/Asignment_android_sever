package a.com.fpolyshop.screen.splash

import a.com.fpolyshop.R
import a.com.fpolyshop.screen.container.ContainerFragment
import a.com.fpolyshop.screen.login.LoginFragment
import a.com.fpolyshop.utils.Constant
import a.com.fpolyshop.utils.loadNoBackStack
import a.com.fpolyshop.utils.loadTransition
import a.com.fpolyshop.utils.showSnackBarMsg
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_splash.view.*

class SplashFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler().postDelayed({
            if (checkLogin()) {
                activity?.loadNoBackStack(ContainerFragment.instance)
            } else {
                activity?.loadTransition(LoginFragment.instance, view.logo_linear)
            }
        }, Constant.BASE_TIME_DELAY.toLong())
    }

    private fun checkLogin(): Boolean {
        activity?.run {
            val pref =
                getSharedPreferences("USER_FILE", Context.MODE_PRIVATE)
            val chk = pref.getBoolean("REMEMBER", false)
            if (chk) {
                return true
            }
        }
        return false
    }
}