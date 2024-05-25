package com.example.natifetestapp.remote.responses

import com.google.gson.annotations.SerializedName

data class GiphyResponse<T>(
    val data: T?,
    val pagination: Pagination?,
    val meta: Meta
) {
    data class Pagination(
        @SerializedName("total_count")
        val totalCount: Int?,
        val count: Int?,
        val offset: Int?
    )

    data class Meta(
        val status: Int,
        @SerializedName("msg")
        val message: String
    )
}



