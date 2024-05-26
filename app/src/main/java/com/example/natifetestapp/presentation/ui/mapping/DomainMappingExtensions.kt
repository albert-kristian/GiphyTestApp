package com.example.natifetestapp.presentation.ui.mapping

import com.example.natifetestapp.domain.models.GifDomain
import com.example.natifetestapp.presentation.ui.models.GifUIModel

fun GifDomain.toUIModel(): GifUIModel = GifUIModel(
    id = id,
    title = title,
    thumbnailAspectRatio = thumbnailWidth.toFloat() / thumbnailHeight.toFloat(),
    thumbnailGifUrl = thumbnailGifUrl,
    originalGifUrl = originalGifUrl
)