package a.com.fpolyshop.data.models

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class Status(
    @SerializedName("status")
    val status: String
) {
    object StatusEntry {
        const val STATUS = "status"
    }

    constructor(jsonObject: JSONObject) : this(
        status = jsonObject.optString(StatusEntry.STATUS)
    )
}