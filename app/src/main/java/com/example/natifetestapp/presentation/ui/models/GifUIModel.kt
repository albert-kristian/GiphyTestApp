package com.example.natifetestapp.presentation.ui.models

data class GifUIModel(
    val id: String,
    val title: String,
    val thumbnailAspectRatio: Float,
    val thumbnailGifUrl: String,
    val originalGifUrl: String
)