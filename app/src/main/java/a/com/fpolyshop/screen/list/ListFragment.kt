package a.com.fpolyshop.screen.list

import a.com.fpolyshop.R
import a.com.fpolyshop.data.models.Product
import a.com.fpolyshop.data.repository.Repository
import a.com.fpolyshop.data.source.local.LocalDataSource
import a.com.fpolyshop.data.source.remote.RemoteDataSource
import a.com.fpolyshop.screen.MainActivity
import a.com.fpolyshop.screen.details.DetailsFragment
import a.com.fpolyshop.screen.list.adapter.ProductVerticalAdapter
import a.com.fpolyshop.utils.Constant
import a.com.fpolyshop.utils.NetworkUtil
import a.com.fpolyshop.utils.load
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.android.synthetic.main.toolbar_base.*

class ListFragment : Fragment(), ListContract.View {
    private lateinit var presenter: ListContract.Presenter
    private val listAdapter: ProductVerticalAdapter by lazy { ProductVerticalAdapter() }
    private var type: String? = ""
    private var value: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            val movieRepository: Repository =
                Repository.getInstance(
                    RemoteDataSource.instance,
                    LocalDataSource.instance
                )
            presenter = ListPresenter(movieRepository)
        }
        presenter.setView(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar()
        initPresenter()
        initAdapter()
    }

    override fun onShowProductsSuccess(products: List<Product>) {
        listAdapter.updateData(products)
        view?.run {
            resultTextView.text =
                getString(R.string.results_found, listAdapter.itemCount)

        }
    }

    override fun onError(str: String?) {
        str?.let {
            Toast.makeText(activity, it, Toast.LENGTH_LONG)
                .show()
        }
    }


    override fun onLoading(isLoad: Boolean) {
        view?.run {
            if (isLoad) {
                frameProgressBar.visibility = View.VISIBLE
            } else {
                frameProgressBar.visibility = View.GONE
            }
        }
    }

    private fun initAdapter() {
        view?.listRecyclerView?.apply {
            setHasFixedSize(true)
            adapter = listAdapter.apply {
                onItemClick = { item, _ ->
                    activity?.load(DetailsFragment.instance(item, item.product_name))
                }
            }
        }
    }

    private fun initPresenter() {
        presenter.setView(this)
        arguments?.run {
            type = getString(Constant.BASE_TYPE)
            value = getString(Constant.BASE_VALUE)
            if (type != null && value != null) {
                activity?.let {
                    if (NetworkUtil.isConnectedToNetwork(it)) {
                        presenter.getProducts(type!!, value!!)
                    } else {
                        val message = getString(R.string.check_internet_fail)
                        Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
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
                activity?.run { supportFragmentManager.popBackStack() }
            }
        }
    }

    private object HOLDER {
        val INSTANCE = ListFragment()
    }

    companion object {
        fun instance(type: String, query: String, title: String) =
            HOLDER.INSTANCE.apply {
                arguments = bundleOf(
                    Constant.BASE_TYPE to type,
                    Constant.BASE_VALUE to query,
                    Constant.BASE_TITLE to title
                )
            }
    }
}