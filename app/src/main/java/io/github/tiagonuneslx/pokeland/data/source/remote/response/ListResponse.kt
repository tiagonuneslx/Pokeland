package io.github.tiagonuneslx.pokeland.data.source.remote.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ListResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("results")
    val results: List<Result>,
) {
    @Keep
    data class Result(
        @SerializedName("name")
        val name: String,
        @SerializedName("url")
        val url: String,
    )
}