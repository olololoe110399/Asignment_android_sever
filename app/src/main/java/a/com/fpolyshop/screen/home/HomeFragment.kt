package a.com.fpolyshop.screen.home

import a.com.fpolyshop.R
import a.com.fpolyshop.data.models.Genre
import a.com.fpolyshop.data.models.Product
import a.com.fpolyshop.data.repository.Repository
import a.com.fpolyshop.data.source.local.LocalDataSource
import a.com.fpolyshop.data.source.remote.RemoteDataSource
import a.com.fpolyshop.screen.details.DetailsFragment
import a.com.fpolyshop.screen.home.adapter.ProductAdapter
import a.com.fpolyshop.screen.home.adapter.SliderViewPagerAdapter
import a.com.fpolyshop.utils.*
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.util.*

class HomeFragment : Fragment(), HomeContract.View, OnClickListener<Product> {
    private lateinit var presenter: HomeContract.Presenter
    private val sellingSlideAdapter: SliderViewPagerAdapter by lazy { SliderViewPagerAdapter() }
    private val hotAdapter: ProductAdapter by lazy { ProductAdapter() }
    private val byGenreAdapter: ProductAdapter by lazy { ProductAdapter() }
    private val goodPriceAdapter: ProductAdapter by lazy { ProductAdapter() }
    private val promotionAdapter: ProductAdapter by lazy { ProductAdapter() }
    private val topRatedAdapter: ProductAdapter by lazy { ProductAdapter() }
    private var genresSelected = 0
    private val swipeTimer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            val movieRepository: Repository =
                Repository.getInstance(
                    RemoteDataSource.instance,
                    LocalDataSource.instance
                )
            presenter = HomePresenter(movieRepository)
        }
        presenter.setView(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    override fun onShowGenresSuccess(genres: List<Genre>) {
        initTabGenres(genres)
    }

    override fun onShowProductsSuccess(type: String, products: List<Product>) {
        when (type) {
            EnumCategory.HOT.type -> hotAdapter.updateData(products)
            EnumCategory.GOOD_PRICE.type -> goodPriceAdapter.updateData(products)
            EnumCategory.SELLING.type -> sellingSlideAdapter.updateData(products)
            EnumCategory.PROMOTION.type -> promotionAdapter.updateData(products)
            EnumCategory.TOP_RATED.type -> topRatedAdapter.updateData(products)
            Constant.BASE_DISCOVER_GENRE -> byGenreAdapter.updateData(products)
        }
    }

    override fun onError(str: String?) {
        str?.let {
            Toast.makeText(activity, it, Toast.LENGTH_LONG)
                .show()
        }
    }

    override fun onLoading(type: String, isLoad: Boolean) {
        view?.run {
            homeSwipeRefresh.isRefreshing = false
            when (type) {
                EnumCategory.SELLING.type -> {
                    if (isLoad) {
                        sellingFrameProgressBar.visibility = View.VISIBLE
                    } else {
                        sellingFrameProgressBar.visibility = View.GONE
                    }
                }
                EnumCategory.HOT.type -> {
                    if (isLoad) {
                        hotFrameProgressBar.visibility = View.VISIBLE
                    } else {
                        hotFrameProgressBar.visibility = View.GONE
                    }
                }
                EnumCategory.GOOD_PRICE.type -> {
                    if (isLoad) {
                        goodPriceFrameProgressBar.visibility = View.VISIBLE
                    } else {
                        goodPriceFrameProgressBar.visibility = View.GONE
                    }
                }
                EnumCategory.TOP_RATED.type -> {
                    if (isLoad) {
                        topRatedFrameProgressBar.visibility = View.VISIBLE
                    } else {
                        topRatedFrameProgressBar.visibility = View.GONE
                    }
                }
                EnumCategory.PROMOTION.type -> {
                    if (isLoad) {
                        promotionFrameProgressBar.visibility = View.VISIBLE
                    } else {
                        promotionFrameProgressBar.visibility = View.GONE
                    }
                }
                Constant.BASE_DISCOVER_GENRE -> {
                    if (isLoad) {
                        productByGenreFrameProgressBar.visibility = View.VISIBLE
                    } else {
                        productByGenreFrameProgressBar.visibility = View.GONE
                    }
                }
            }
        }

    }

    override fun click(item: Product?) {
        item?.let {
            activity?.load(DetailsFragment.instance(it, it.product_name))
        }
    }

    private fun initComponents() {
        timerSlide()
        initAdapter()
        initPresenter()
        initRefresh()
    }

    private fun initPresenter() {
        activity?.let {
            if (NetworkUtil.isConnectedToNetwork(it)) {
                Handler().postDelayed({ presenter.onStart() }, 500)
            } else {
                val message = getString(R.string.check_internet_fail)
                Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initAdapter() {
        view?.run {
            productByGenreRecyclerView.setHasFixedSize(true)
            productByGenreRecyclerView.adapter = byGenreAdapter.apply {
                onItemClick = { item, _ ->
                    activity?.load(DetailsFragment.instance(item, item.product_name))
                }
            }
            promotionRecyclerView.setHasFixedSize(true)
            promotionRecyclerView.adapter = promotionAdapter.apply {
                onItemClick = { item, _ ->
                    activity?.load(DetailsFragment.instance(item, item.product_name))

                }
            }
            topRatedRecyclerView.setHasFixedSize(true)
            topRatedRecyclerView.adapter = topRatedAdapter.apply {
                onItemClick = { item, _ ->
                    activity?.load(DetailsFragment.instance(item, item.product_name))

                }
            }
            goodPriceRecyclerView.setHasFixedSize(true)
            goodPriceRecyclerView.adapter = goodPriceAdapter.apply {
                onItemClick = { item, _ ->
                    activity?.load(DetailsFragment.instance(item, item.product_name))

                }
            }
            hotRecyclerView.setHasFixedSize(true)
            hotRecyclerView.adapter = hotAdapter.apply {
                onItemClick = { item, _ ->
                    activity?.load(DetailsFragment.instance(item, item.product_name))
                }
            }
            sellingViewPager?.adapter = sellingSlideAdapter
            sellingTabLayout.setupWithViewPager(
                sellingViewPager,
                true
            )
        }
        sellingSlideAdapter.setSlideItemClickListener(this)
        timerSlide()
    }


    private fun timerSlide() {
        view?.run {
            // Auto start of viewpager
            val updateRunnable = Runnable {
                if (sellingViewPager.currentItem < sellingSlideAdapter.count - 1) {
                    sellingViewPager.currentItem =
                        sellingViewPager.currentItem + 1
                } else {
                    sellingViewPager.currentItem = 0
                }
            }
            swipeTimer.schedule(object : TimerTask() {
                override fun run() {
                    activity?.runOnUiThread { Handler().post(updateRunnable) }
                }
            }, Constant.BASE_DELAY_SLIDE, Constant.BASE_DELAY_SLIDE)
        }
    }


    private fun initTabGenres(genres: List<Genre>) {
        view?.run {
            if (genreTabLayout?.tabCount == 0) {
                activity?.let {
                    genreTabLayout?.setTabTextColors(
                        ContextCompat.getColor(it, R.color.colorSliver),
                        ContextCompat.getColor(it, R.color.colorOrange)
                    )
                }
                for (element in genres) {
                    genreTabLayout.addTab(
                        genreTabLayout.newTab().setText(element.genre_name)
                    )
                }
                genresSelected = genres[0].genre_id.toInt()
                presenter.getProducts(
                    Constant.BASE_DISCOVER_GENRE,
                    genresSelected.toString()
                )
                genreTabLayout?.addOnTabSelectedListener(
                    object : TabLayout.OnTabSelectedListener {
                        override fun onTabReselected(tab: TabLayout.Tab?) {
                        }

                        override fun onTabUnselected(tab: TabLayout.Tab?) {
                        }

                        override fun onTabSelected(tab: TabLayout.Tab?) {
                            activity?.let { activity ->
                                if (NetworkUtil.isConnectedToNetwork(activity)) {
                                    tab?.let {
                                        genresSelected = genres[it.position].genre_id.toInt()
                                    }
                                    presenter.getProducts(
                                        Constant.BASE_DISCOVER_GENRE,
                                        genresSelected.toString()
                                    )
                                } else {
                                    Toast.makeText(
                                        activity,
                                        getString(R.string.check_internet_fail),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    })
                genreTabLayout.getTabAt(0)?.select()
            }
        }
    }

    private fun initRefresh() {
        view?.homeSwipeRefresh?.setOnRefreshListener {
            activity?.let {
                if (NetworkUtil.isConnectedToNetwork(it)) {
                    presenter.onStart()
                    presenter.getProducts(
                        Constant.BASE_DISCOVER_GENRE,
                        genresSelected.toString()
                    )
                } else {
                    val message = getString(R.string.check_internet_fail)
                    Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private object HOLDER {
        val INSTANCE = HomeFragment()
    }

    companion object {
        val instance: HomeFragment by lazy {
            HOLDER.INSTANCE
        }
    }


}