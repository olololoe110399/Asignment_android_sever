package a.com.fpolyshop.screen

import a.com.fpolyshop.R
import a.com.fpolyshop.screen.container.ContainerFragment
import a.com.fpolyshop.screen.home.HomeFragment
import a.com.fpolyshop.screen.splash.SplashFragment
import android.app.AlertDialog
import android.graphics.Color
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
        val main = supportFragmentManager.findFragmentById(R.id.mainFrameLayout)
        val container =
            supportFragmentManager.findFragmentById(R.id.containerFrameLayout)
        if (main is ContainerFragment) {
            showAppExitDialog()
        } else if (container is HomeFragment) {
            ContainerFragment.startingPosition = 0
            supportFragmentManager.beginTransaction()
                .remove(container)
                .commit()
            super.onBackPressed()
        }else{
            super.onBackPressed()
        }
    }

    private fun showAppExitDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.AlertDialogStyle)
        val dialog: AlertDialog =
            builder.setIcon(R.drawable.ic_exit_to_app).setTitle(getString(R.string.title_exit_app))
                .setMessage(getString(R.string.notification_exit_app))

                .setPositiveButton(this.getString(R.string.title_yes)) { _, _ ->
                    finish()
                }
                .setNegativeButton(this.getString(R.string.title_no)) { _, _ ->
                }.create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(Color.RED)
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.TRANSPARENT)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(Color.RED)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(Color.TRANSPARENT)

    }
}
