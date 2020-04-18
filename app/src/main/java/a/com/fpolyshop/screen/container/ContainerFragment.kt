package a.com.fpolyshop.screen.container

import a.com.fpolyshop.R
import a.com.fpolyshop.screen.MainActivity
import a.com.fpolyshop.screen.edit_profile.EditProfileFragment
import a.com.fpolyshop.screen.home.HomeFragment
import a.com.fpolyshop.screen.login.LoginFragment
import a.com.fpolyshop.screen.profile.ProfileFragment
import a.com.fpolyshop.utils.loadNoBackStack
import a.com.fpolyshop.utils.showSnackBar
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_container.view.*
import kotlinx.android.synthetic.main.toolbar_main.view.*

class ContainerFragment : Fragment() {
    private var startingPosition = 0
    private var changeMenu = 1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        view.navigationView.setOnNavigationItemSelectedListener(selectedListener)
        initToolBar()
        loadFragment(HomeFragment(), 0)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        when (changeMenu) {
            1 -> {
                inflater.inflate(R.menu.home, menu)
            }
            2 -> {
                inflater.inflate(R.menu.profile, menu)
            }
            else -> {
                inflater.inflate(R.menu.save_profile, menu)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        activity?.run {
            val manager = supportFragmentManager
            val fragment =
                manager.findFragmentById(R.id.containerFrameLayout)
            when (item.itemId) {
                R.id.search_home -> {
                }
                R.id.aboutUs -> {
                }
                R.id.logout -> {
                    val edit = getSharedPreferences("USER_FILE", Context.MODE_PRIVATE).edit()
                    edit.clear()
                    edit.apply()
                    activity?.loadNoBackStack(LoginFragment())
                }
                R.id.edit -> {
                    if (fragment is ProfileFragment) {
                        fragment.getUserProfile()?.let {
                            loadFragment(EditProfileFragment.getInstance(it), 2)
                        }

                    } else {
                        showSnackBar(R.string.pls_slow)
                    }
                }
                R.id.save -> {
                    if (fragment is EditProfileFragment) {
                        fragment.onClick()
                    } else {
                        showSnackBar(R.string.pls_slow)
                    }
                }
                R.id.cancel -> {
                    if (fragment is EditProfileFragment) {
                        loadFragment(ProfileFragment.getInstance(), 2)
                    } else {
                        showSnackBar(R.string.pls_slow)
                    }
                }
                else -> null
            }
        }
        return false
    }

    fun invalidateOptionsMenuOnSuccessEdit() {
        activity?.run {
            changeMenu = 2
            invalidateOptionsMenu()
        }

    }

    private fun loadFragment(fragment: Fragment, position: Int): Boolean {
        activity?.run {
            when {
                startingPosition > position -> {
                    supportFragmentManager.beginTransaction().setCustomAnimations(
                            R.anim.left_to_right,
                            R.anim.exit_left_to_right,
                            R.anim.right_to_left,
                            R.anim.exit_right_to_left
                        ).replace(R.id.containerFrameLayout, fragment)
                        .commit()
                }
                startingPosition < position -> {
                    supportFragmentManager.beginTransaction().setCustomAnimations(
                            R.anim.right_to_left,
                            R.anim.exit_right_to_left,
                            R.anim.left_to_right,
                            R.anim.exit_left_to_right
                        ).replace(R.id.containerFrameLayout, fragment)
                        .commit()
                }
                else -> {
                    changeMenu =
                        when (fragment) {
                            is ProfileFragment -> {
                                2
                            }
                            is EditProfileFragment -> {
                                0
                            }
                            else -> {
                                1
                            }
                        }
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.containerFrameLayout, fragment)
                        .commit()
                }
            }
            invalidateOptionsMenu()
        }
        startingPosition = position
        return true
    }

    private fun initToolBar() {
        view?.toolbar?.let {
            (activity as? MainActivity)?.run { setSupportActionBar(it) }
        }
    }

    private val selectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment(), 1)
                    changeMenu = 1
                    return@OnNavigationItemSelectedListener true
                }
                R.id.profile -> {
                    loadFragment(ProfileFragment.getInstance(), 2)
                    changeMenu = 2
                    return@OnNavigationItemSelectedListener true
                }
            }
            activity?.invalidateOptionsMenu()
            return@OnNavigationItemSelectedListener false
        }
}