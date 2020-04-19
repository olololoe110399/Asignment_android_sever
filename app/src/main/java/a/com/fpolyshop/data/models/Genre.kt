package a.com.fpolyshop.data.models

import com.google.gson.annotations.SerializedName

data class Genre(
    @SerializedName("genre_id")
    val genre_id: String,
    @SerializedName("genre_name")
    val genre_name: String
)
