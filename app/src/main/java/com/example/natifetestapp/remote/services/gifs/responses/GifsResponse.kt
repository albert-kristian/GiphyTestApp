package com.example.natifetestapp.remote.services.gifs.responses

import com.google.gson.annotations.SerializedName

typealias GifsResponse = List<GifResponse>

data class GifResponse(
    val type: String,
    val id: String,
    val title: String,
    val url: String,
    val images: ImagesResponse
)

data class ImagesResponse(
    val original: ImageResponse,
    @SerializedName("fixed_width_small")
    val fixedWidthSmall: ImageResponse,
    @SerializedName("fixed_width_downsampled")
    val fixedWidthDownsampled: ImageResponse
)

data class ImageResponse(
    val height: Int,
    val width: Int,
    val url: String
)