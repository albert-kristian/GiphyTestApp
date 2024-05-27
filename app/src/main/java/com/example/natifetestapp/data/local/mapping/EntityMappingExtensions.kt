package com.example.natifetestapp.data.local.mapping

import com.example.natifetestapp.data.local.entities.GifEntity
import com.example.natifetestapp.domain.models.GifDomain

fun GifEntity.toDomain(): GifDomain = GifDomain(
    id = id,
    title = title,
    thumbnailHeight = thumbnailHeight,
    thumbnailWidth = thumbnailWidth,
    thumbnailGifUrl = thumbnailUrl,
    originalGifUrl = originalUrl
)

fun GifDomain.toEntity(
    isDeleted: Boolean = false
): GifEntity = GifEntity(
    id = id,
    title = title,
    isDeleted = isDeleted,
    thumbnailHeight = thumbnailHeight,
    thumbnailWidth = thumbnailWidth,
    thumbnailUrl = thumbnailGifUrl,
    originalUrl = originalGifUrl
)