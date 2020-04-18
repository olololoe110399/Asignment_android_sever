package a.com.fpolyshop.screen.login

import a.com.fpolyshop.R
import a.com.fpolyshop.screen.login.adapter.ViewPageAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_login.view.*

class LoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ViewPageAdapter(childFragmentManager)
        view.view_pager.adapter = adapter
        view.tabs.setupWithViewPager(view.view_pager)
        view.view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(view.tabs))
        view.tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(view.view_pager))
    }
}