package a.com.fpolyshop.screen.home.adapter

import a.com.fpolyshop.R
import a.com.fpolyshop.data.models.Product
import a.com.fpolyshop.utils.Constant
import a.com.fpolyshop.utils.GlideApp
import a.com.fpolyshop.utils.OnClickListener
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import kotlinx.android.synthetic.main.item_selling.view.*

class SliderViewPagerAdapter() : PagerAdapter() {
    private val list = arrayListOf<Product>()
    private var slideItemClickListener: OnClickListener<Product>? = null

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater =
            container.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val slideLayout = inflater.inflate(R.layout.item_selling, null)
        slideLayout.setOnClickListener {
            slideItemClickListener?.click(
                list[position]
            )
        }
        slideLayout.productNameTextView.text = list[position].product_name
        GlideApp
            .with(slideLayout.slideImageView)
            .load(Constant.BASE_URL + Constant.BASE_URL_IMAGE + list[position].backdrop_path)
            .centerCrop()
            .into(slideLayout.slideImageView)
        container.addView(slideLayout)
        return slideLayout
    }

    override fun getCount(): Int = list.size

    override fun isViewFromObject(view: View, o: Any): Boolean = view === o

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        obj: Any
    ) {
        container.removeView(obj as View)
    }

    fun setSlideItemClickListener(
        movieItemClickListener: OnClickListener<Product>?
    ) {
        this.slideItemClickListener = movieItemClickListener
    }

    fun updateData(newItems: List<Product>) {
        list.apply {
            if (isNotEmpty()) clear()
            addAll(newItems)
        }
        notifyDataSetChanged()
    }
}
