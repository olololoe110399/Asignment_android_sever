package a.com.fpolyshop.data.source.local

import a.com.fpolyshop.R
import a.com.fpolyshop.data.models.Category
import a.com.fpolyshop.data.source.DataSource
import a.com.fpolyshop.utils.EnumCategory
import io.reactivex.Flowable

class LocalDataSource private constructor() : DataSource.Local {
    override fun getCategories(): Flowable<List<Category>> {
        return Flowable.just(
            listOf(
                Category(EnumCategory.SELLING.type, R.drawable.selling_category),
                Category(EnumCategory.PROMOTION.type, R.drawable.promotion_category),
                Category(EnumCategory.TOP_RATED.type, R.drawable.top_rated_category),
                Category(EnumCategory.GOOD_PRICE.type, R.drawable.good_price_category),
                Category(EnumCategory.HOT.type, R.drawable.hot_category)
            )
        )
    }

    private object HOLDER {
        val INSTANCE = LocalDataSource()
    }

    companion object {
        val instance: LocalDataSource by lazy {
            HOLDER.INSTANCE
        }
    }
}
