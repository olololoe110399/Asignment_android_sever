package a.com.fpolyshop.screen.home.adapter

import a.com.fpolyshop.R
import a.com.fpolyshop.data.models.Product
import a.com.fpolyshop.screen.base.BaseAdapter
import a.com.fpolyshop.screen.base.BaseViewHolder
import a.com.fpolyshop.utils.Constant
import a.com.fpolyshop.utils.GlideApp
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_product_hotizontal.view.*

class ProductAdapter : BaseAdapter<Product, ProductAdapter.ViewHolder>() {

    var onItemClick: (Product, Int) -> Unit = { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product_hotizontal, parent, false), onItemClick
        )

    class ViewHolder(
        itemView: View,
        onItemClick: (Product, Int) -> Unit
    ) : BaseViewHolder<Product>(itemView, onItemClick) {

        override fun onBindData(itemData: Product) {
            super.onBindData(itemData)
            GlideApp
                .with(itemView.itemMovieImageView.context)
                .load(Constant.BASE_URL + Constant.BASE_URL_IMAGE + itemData.image_path)
                .centerCrop()
                .into(itemView.itemMovieImageView)

            itemView.textImdb.text = itemData.vote_average.toFloat().toString()
            itemView.progressVote.progress=itemData.vote_average.toInt()
            itemView.produceNameTextView.text=itemData.product_name
        }
    }
}
