package a.com.fpolyshop.data.source.remote.response

import a.com.fpolyshop.data.models.Genre
import com.google.gson.annotations.SerializedName

data class GenreResponse(@SerializedName("genres") var genres: List<Genre>)
