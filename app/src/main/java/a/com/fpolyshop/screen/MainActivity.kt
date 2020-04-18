package a.com.fpolyshop.screen

import a.com.fpolyshop.R
import a.com.fpolyshop.screen.container.ContainerFragment
import a.com.fpolyshop.screen.splash.SplashFragment
import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(
                R.id.mainFrameLayout,
                SplashFragment()
            )
            .commit()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.mainFrameLayout)
        if (fragment is ContainerFragment) {
            showAppExitDialog()
        } else {
            super.onBackPressed()
        }
    }

    private fun showAppExitDialog() = with(AlertDialog.Builder(this)) {
        setIcon(R.drawable.ic_exit_to_app)
        setTitle(getString(R.string.title_exit_app))
        setMessage(getString(R.string.notification_exit_app))

        setPositiveButton(context.getString(R.string.title_yes)) { _, _ ->
            finish()
        }
        setNegativeButton(context.getString(R.string.title_no)) { _, _ ->
        }
        show()
    }

}
