package a.com.fpolyshop.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    @SerializedName("_id")
    val product_id: String,
    @SerializedName("product_name")
    val product_name: String,
    @SerializedName("over_view")
    val over_view: String,
    @SerializedName("vote_average")
    val vote_average: String,
    @SerializedName("quantity")
    val quantity: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("release_date")
    val release_date: String,
    @SerializedName("genre")
    val genre: String,
    @SerializedName("producer")
    val producer: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("image_path")
    val image_path: String,
    @SerializedName("backdrop_path")
    val backdrop_path: String
) : Parcelable
