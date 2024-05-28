package com.example.natifetestapp.utils.extensions

import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi

@OptIn(ExperimentalCoilApi::class)
fun ImageLoader.isImageCached(key: String): Boolean {
    return try {
        diskCache?.openSnapshot(key)?.use {
            it.data.toFile().exists()
        } ?: false
    } catch (t: Throwable) {
        false
    }
}