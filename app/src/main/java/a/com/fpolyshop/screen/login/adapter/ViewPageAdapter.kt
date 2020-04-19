package a.com.fpolyshop.screen.login.adapter

import a.com.fpolyshop.screen.sign_in.SignInFragment
import a.com.fpolyshop.screen.sign_up.SignUpFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

@Suppress("DEPRECATION")
class ViewPageAdapter(fragmentManager: FragmentManager?) :
    FragmentPagerAdapter(fragmentManager!!) {

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = SignInFragment.instance
            1 -> fragment = SignUpFragment.instance
        }
        return fragment!!
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title = ""
        when (position) {
            0 -> title = "Sign in"
            1 -> title = "Sign up"
        }
        return title
    }
}