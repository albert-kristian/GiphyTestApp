package com.example.natifetestapp.domain.models

data class GifDomain(
    val id: String,
    val title: String,
    val thumbnailHeight: Int,
    val thumbnailWidth: Int,
    val thumbnailGifUrl: String,
    val originalGifUrl: String
)
