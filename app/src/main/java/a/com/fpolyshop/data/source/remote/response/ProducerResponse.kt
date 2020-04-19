package a.com.fpolyshop.data.source.remote.response

import a.com.fpolyshop.data.models.Producer
import com.google.gson.annotations.SerializedName

data class ProducerResponse(@SerializedName("producer") var producers: List<Producer>)
