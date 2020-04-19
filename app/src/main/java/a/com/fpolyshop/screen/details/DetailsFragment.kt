package a.com.fpolyshop.screen.details

import a.com.fpolyshop.R
import a.com.fpolyshop.data.models.Product
import a.com.fpolyshop.screen.MainActivity
import a.com.fpolyshop.utils.Constant
import a.com.fpolyshop.utils.GlideApp
import a.com.fpolyshop.utils.NetworkUtil
import a.com.fpolyshop.utils.removeFragmentContainer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_details.view.*
import kotlinx.android.synthetic.main.toolbar_base.*

class DetailsFragment private constructor() : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar()
        getProductSuccess()
        initRefresh()
    }

    private fun getProductSuccess() {
        val product = arguments?.getParcelable<Product>(Constant.BASE_VALUE)
        view?.run {
            product?.let {
                titleTextView.text = product.product_name
                voteTextView.text = product.vote_average.toFloat().toString()
                releaseDateTexView.text = product.release_date
                overViewTextView.text = product.over_view
                voteRatingBar.rating = product.vote_average.toFloat() / 2
                priceProduct.text =
                    "${(String.format("%,d", product.price.toInt())).replace(',', ' ')} VND"
                GlideApp
                    .with(posterImageView)
                    .load(Constant.BASE_URL + Constant.BASE_URL_IMAGE + it.image_path)
                    .centerCrop()
                    .into(posterImageView)
                GlideApp
                    .with(backdropImageView)
                    .load(Constant.BASE_URL + Constant.BASE_URL_IMAGE + it.backdrop_path)
                    .centerCrop()
                    .into(backdropImageView)
                posterImageView.animation =
                    AnimationUtils.loadAnimation(activity, R.anim.scale_animation)
                backdropImageView.animation =
                    AnimationUtils.loadAnimation(activity, R.anim.scale_animation)
            }
            detailsSwipeRefresh.isRefreshing = false
        }
    }

    private fun initToolBar() {
        toolbar_base?.let {
            (activity as? MainActivity)?.run {
                setSupportActionBar(it)
                supportActionBar?.run {
                    setDisplayShowTitleEnabled(true)
                    title = arguments?.getString(Constant.BASE_TITLE)
                }
            }
            it.setNavigationOnClickListener {
                activity?.run {
                    removeFragmentContainer()
                    supportFragmentManager.popBackStack()
                }
            }
        }
    }

    private fun initRefresh() {
        view?.detailsSwipeRefresh?.setOnRefreshListener {
            activity?.let { activity ->
                if (NetworkUtil.isConnectedToNetwork(activity)) {
                    arguments?.let {
                        getProductSuccess()
                    }
                } else {
                    val message = getString(R.string.check_internet_fail)
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    object HOLDER {
        val INSTANCE = DetailsFragment()
    }

    companion object {
        fun instance(product: Product, title: String) = HOLDER.INSTANCE.apply {
            arguments = bundleOf(Constant.BASE_VALUE to product, Constant.BASE_TITLE to title)
        }
    }
}