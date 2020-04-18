package a.com.fpolyshop.utils

import a.com.fpolyshop.R
import android.content.Context
import android.content.SharedPreferences
import android.text.Editable
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.transition.TransitionInflater
import com.google.android.material.snackbar.Snackbar

fun FragmentActivity.showSnackBar(msgId: Int) {
    val view = this.findViewById(android.R.id.content) as View
    val snackBar = Snackbar.make(
        view
        , this.getString(msgId),
        Snackbar.LENGTH_SHORT
    )
    val snackBarView = snackBar.view
    snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorSliver))
    val textView =
        snackBarView.findViewById(R.id.snackbar_text) as TextView
    textView.setTextColor(ContextCompat.getColor(this, android.R.color.white))
    textView.textSize = 16f
    snackBar.show()
}

fun FragmentActivity.showSnackBarMsg(msg: String) {
    val view = this.findViewById(android.R.id.content) as View
    val snackBar = Snackbar.make(
        view
        , msg,
        Snackbar.LENGTH_SHORT
    )
    val snackBarView = snackBar.view
    snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorSliver))
    val textView =
        snackBarView.findViewById(R.id.snackbar_text) as TextView
    textView.setTextColor(ContextCompat.getColor(this, android.R.color.white))
    textView.textSize = 16f
    snackBar.show()
}

fun FragmentActivity.loadTransition(fragment: Fragment, view: View) {
    fragment.sharedElementReturnTransition =
        TransitionInflater.from(this).inflateTransition(android.R.transition.move)
    fragment.sharedElementEnterTransition =
        TransitionInflater.from(this).inflateTransition(android.R.transition.move)
    fragment.exitTransition =
        TransitionInflater.from(this).inflateTransition(android.R.transition.explode)
    fragment.enterTransition =
        TransitionInflater.from(this).inflateTransition(android.R.transition.explode)
    if (NetworkUtil.isConnectedToNetwork(this)) {
        this.supportFragmentManager.beginTransaction()
            .replace(R.id.mainFrameLayout, fragment)
            .addSharedElement(view, "logo")
            .commit()
    } else {
        val message = getString(R.string.check_internet_fail)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

fun FragmentActivity.load(fragment: Fragment) {
    if (NetworkUtil.isConnectedToNetwork(this)) {
        this.supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.mainFrameLayout, fragment)
            .addToBackStack(null)
            .commit()
    } else {
        val message = getString(R.string.check_internet_fail)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

fun FragmentActivity.loadNoBackStack(fragment: Fragment) {
    if (NetworkUtil.isConnectedToNetwork(this)) {
        this.supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.mainFrameLayout, fragment)
            .commit()
    } else {
        val message = getString(R.string.check_internet_fail)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

fun FragmentActivity.loadNoBackStackContainer(fragment: Fragment) {
    if (NetworkUtil.isConnectedToNetwork(this)) {
        this.supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.containerFrameLayout, fragment)
            .commit()
    } else {
        val message = getString(R.string.check_internet_fail)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

fun FragmentActivity.rememberUser(
    u: String?,
    p: String?,
    status: Boolean
) {
    val pref: SharedPreferences =
        getSharedPreferences("USER_FILE", Context.MODE_PRIVATE)
    val edit = pref.edit()
    if (!status) {
        edit.clear()
        edit.putString("USERNAME", u)
        edit.putString("PASSWORD", p)
    } else {
        edit.putString("USERNAME", u)
        edit.putString("PASSWORD", p)
        edit.putBoolean("REMEMBER", status)
    }
    edit.apply()
}
