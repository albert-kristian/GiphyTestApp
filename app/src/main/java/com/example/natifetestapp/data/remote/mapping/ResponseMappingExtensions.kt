package com.example.natifetestapp.data.remote.mapping

import com.example.natifetestapp.data.remote.services.gifs.responses.GifResponse
import com.example.natifetestapp.data.remote.services.gifs.responses.GifsResponse
import com.example.natifetestapp.domain.models.GifDomain

fun GifsResponse.toDomain(): List<GifDomain> = map { gifResponse ->
    gifResponse.toDomain()
}

fun GifResponse.toDomain(): GifDomain = GifDomain(
    id = id,
    title = title,
    thumbnailHeight = images.fixedWidthDownsampled.height,
    thumbnailWidth = images.fixedWidthDownsampled.width,
    thumbnailGifUrl = images.fixedWidthDownsampled.url,
    originalGifUrl = images.original.url
)