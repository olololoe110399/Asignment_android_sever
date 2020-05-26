package a.com.fpolyshop.screen.list.adapter

import a.com.fpolyshop.R
import a.com.fpolyshop.data.models.Product
import a.com.fpolyshop.screen.base.BaseAdapter
import a.com.fpolyshop.screen.base.BaseViewHolder
import a.com.fpolyshop.utils.Constant
import a.com.fpolyshop.utils.GlideApp
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_product_vertical.view.*

class ProductVerticalAdapter : BaseAdapter<Product, ProductVerticalAdapter.ViewHolder>() {
    var onItemClick: (Product, Int) -> Unit = { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_vertical, parent, false)
        return ViewHolder(
            itemView,
            onItemClick
        )
    }

    class ViewHolder(
        itemView: View,
        onItemClick: (Product, Int) -> Unit
    ) : BaseViewHolder<Product>(itemView, onItemClick) {

        override fun onBindData(itemData: Product) {
            super.onBindData(itemData)
            itemView.voteRatingBar.rating = itemData.vote_average.toFloat() / 2
            itemView.titleTextView.text = itemData.product_name
            itemView.overviewTextView.text = itemData.over_view
            itemView.releaseDateTexView.text = itemData.release_date
            itemView.priceTextView.text=   "${(String.format("%,d", itemData.price.toInt())).replace(',', ' ')} VND"
            GlideApp
                .with(itemView.posterImageView)
                .load(Constant.BASE_URL + Constant.BASE_URL_IMAGE + itemData.image_path)
                .centerInside()
                .into(itemView.posterImageView)
        }
    }

}
