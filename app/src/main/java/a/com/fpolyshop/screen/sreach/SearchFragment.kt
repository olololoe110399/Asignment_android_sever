package a.com.fpolyshop.screen.sreach

import a.com.fpolyshop.R
import a.com.fpolyshop.data.models.Category
import a.com.fpolyshop.data.models.Genre
import a.com.fpolyshop.data.models.Producer
import a.com.fpolyshop.data.repository.Repository
import a.com.fpolyshop.data.source.local.LocalDataSource
import a.com.fpolyshop.data.source.remote.RemoteDataSource
import a.com.fpolyshop.screen.MainActivity
import a.com.fpolyshop.screen.list.ListFragment
import a.com.fpolyshop.screen.sreach.adapter.CategoryAdapter
import a.com.fpolyshop.screen.sreach.adapter.ProducerAdapter
import a.com.fpolyshop.utils.Constant
import a.com.fpolyshop.utils.NetworkUtil
import a.com.fpolyshop.utils.load
import a.com.fpolyshop.utils.removeFragmentContainer
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.android.synthetic.main.toolbar_base.*

class SearchFragment private constructor() : Fragment(), SearchContract.View {
    private lateinit var presenter: SearchContract.Presenter
    private val categoryAdapter: CategoryAdapter by lazy { CategoryAdapter() }
    private val producerAdapter: ProducerAdapter by lazy { ProducerAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            val movieRepository: Repository =
                Repository.getInstance(
                    RemoteDataSource.instance,
                    LocalDataSource.instance
                )
            presenter = SearchPresenter(movieRepository)
        }
        presenter.setView(this)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initPresenter()
        initAdapter()
        initToolBar()
        initRefresh()
    }

    override fun onShowGenresSuccess(genres: List<Genre>) {
        view?.run {
            if (genresChipGroup.childCount == 0) {
                for (item in genres) {
                    val inflate = LayoutInflater.from(activity)
                    val genresChip = inflate.inflate(
                        R.layout.item_chip,
                        genresChipGroup,
                        false
                    ) as Chip
                    genresChip.run {
                        id = item.genre_id.toInt()
                        text = item.genre_name
                        setOnCheckedChangeListener { buttonView, isChecked ->
                            if (isChecked) {
                                val fragment = ListFragment.instance(
                                    Constant.BASE_DISCOVER_GENRE,
                                    buttonView.id.toString(),
                                    buttonView.text.toString()
                                )
                                activity?.load(fragment)
                            }
                        }
                    }
                    genresChipGroup.addView(genresChip)
                }
            }
        }
    }

    override fun onShowCategorySuccess(category: List<Category>) {
        categoryAdapter.updateData(category)
    }

    override fun onShowProducersSuccess(producers: List<Producer>) {
        producerAdapter.updateData(producers)
    }

    override fun onError(str: String?) {
        str?.let {
            Toast.makeText(activity, it, Toast.LENGTH_LONG)
                .show()
        }
    }

    override fun onLoading(type: String, isLoad: Boolean) {
        view?.run {
            when (type) {
                Constant.BASE_DISCOVER_GENRE -> {
                    if (isLoad) {
                        genresProgressBar.visibility = View.VISIBLE
                    } else {
                        genresProgressBar.visibility = View.GONE
                    }
                }
                Constant.BASE_DISCOVER_PRODUCER -> {
                    if (isLoad) {
                        productionCompaniesProgressBar.visibility = View.VISIBLE
                    } else {
                        productionCompaniesProgressBar.visibility = View.GONE
                    }
                }
                Constant.BASE_CATEGORY -> {
                    if (isLoad) {
                        categoriesProgressBar.visibility = View.VISIBLE
                    } else {
                        categoriesProgressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.search_view, menu)
        val searchView = menu.findItem(R.id.searchView).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                val fragment = ListFragment.instance(
                    Constant.BASE_SEARCH,
                    query,
                    query
                )
                activity?.load(fragment)
                return false
            }
        })
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
            categoriesRecyclerView.setHasFixedSize(true)
            categoriesRecyclerView.adapter = categoryAdapter.apply {
                onItemClick = { category, _ ->
                    val fragment = ListFragment.instance(
                        category.categoryName,
                        Constant.BASE_VALUE,
                        category.categoryName
                    )
                    activity?.load(fragment)
                }


            }
            productionCompaniesRecyclerView.setHasFixedSize(true)
            productionCompaniesRecyclerView.adapter = producerAdapter.apply {
                onItemClick = { producer, _ ->
                    val fragment = ListFragment.instance(
                       Constant.BASE_DISCOVER_PRODUCER,
                        producer.producer_id,
                        producer.producer_name
                    )
                    activity?.load(fragment)
                }
            }
        }
    }

    private fun initToolBar() {
        toolbar_base?.let {
            (activity as? MainActivity)?.run {
                setSupportActionBar(it)
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
        view?.searchSwipeRefresh?.setOnRefreshListener {
            activity?.let { activity ->
                if (NetworkUtil.isConnectedToNetwork(activity)) {
                    presenter.onStart()
                    searchSwipeRefresh.isRefreshing = false
                } else {
                    val message = getString(R.string.check_internet_fail)
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private object HOLDER {
        val INSTANCE = SearchFragment()
    }

    companion object {
        val instance: SearchFragment by lazy {
            HOLDER.INSTANCE
        }
    }
}