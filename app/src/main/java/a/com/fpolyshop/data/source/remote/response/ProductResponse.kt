package a.com.fpolyshop.data.source.remote.response

import a.com.fpolyshop.data.models.Product
import com.google.gson.annotations.SerializedName

data class ProductResponse(@SerializedName("products") var products: List<Product>)
