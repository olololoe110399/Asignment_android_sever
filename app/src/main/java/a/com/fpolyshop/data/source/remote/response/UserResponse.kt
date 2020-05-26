package a.com.fpolyshop.data.source.remote.response

import a.com.fpolyshop.data.models.User
import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("customer") var user: User,
    @SerializedName("status") var status: String
)
