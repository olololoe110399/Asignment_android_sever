package a.com.fpolyshop.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Producer(
    @SerializedName("producer_id")
    val producer_id: String,
    @SerializedName("producer_name")
    val producer_name: String,
    @SerializedName("image_path")
    val image_path: String
) : Parcelable
