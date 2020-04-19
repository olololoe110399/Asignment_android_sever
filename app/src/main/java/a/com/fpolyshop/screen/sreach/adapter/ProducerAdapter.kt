package a.com.fpolyshop.screen.sreach.adapter

import a.com.fpolyshop.R
import a.com.fpolyshop.data.models.Producer
import a.com.fpolyshop.screen.base.BaseAdapter
import a.com.fpolyshop.screen.base.BaseViewHolder
import a.com.fpolyshop.utils.Constant
import a.com.fpolyshop.utils.GlideApp
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_producer.view.*

class ProducerAdapter : BaseAdapter<Producer, ProducerAdapter.ViewHolder>() {

    var onItemClick: (Producer, Int) -> Unit = { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_producer, parent, false), onItemClick
        )

    class ViewHolder(
        itemView: View,
        onItemClick: (Producer, Int) -> Unit
    ) : BaseViewHolder<Producer>(itemView, onItemClick) {

        override fun onBindData(itemData: Producer) {
            super.onBindData(itemData)
            itemView.produceNameTextView.text = itemData.producer_name
            GlideApp
                .with(itemView.produceProfileImageView)
                .load(Constant.BASE_URL + Constant.BASE_URL_IMAGE + itemData.image_path)
                .centerInside()
                .into(itemView.produceProfileImageView)
        }
    }
}
