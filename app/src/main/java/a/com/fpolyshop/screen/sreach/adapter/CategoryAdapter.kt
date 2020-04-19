package a.com.fpolyshop.screen.sreach.adapter

import a.com.fpolyshop.R
import a.com.fpolyshop.data.models.Category
import a.com.fpolyshop.screen.base.BaseAdapter
import a.com.fpolyshop.screen.base.BaseViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_category.view.*

class CategoryAdapter : BaseAdapter<Category, CategoryAdapter.ViewHolder>() {
    var onItemClick: (Category, Int) -> Unit = { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_category, parent, false), onItemClick
        )

    class ViewHolder(
        itemView: View,
        onItemClick: (Category, Int) -> Unit
    ) : BaseViewHolder<Category>(itemView, onItemClick) {

        override fun onBindData(itemData: Category) {
            super.onBindData(itemData)
            itemView.categoryNameTextView.text = itemData.categoryName
            itemView.categoryImageView.setImageResource(itemData.categoryImage)
        }
    }
}
