package a.com.fpolyshop.screen.base

interface BasePresenter<T> {
    fun onStart()
    fun onStop()
    fun setView(view: T)
}
