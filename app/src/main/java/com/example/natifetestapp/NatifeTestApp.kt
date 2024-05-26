package com.example.natifetestapp

import android.app.Application
import android.os.Build.VERSION.SDK_INT
import coil.EventListener
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.natifetestapp.data.repository.GifsRepository
import com.example.natifetestapp.di.coroutines.ApplicationScope
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class NatifeTestApp: Application(), ImageLoaderFactory {

    @Inject
    lateinit var gifsRepository: GifsRepository

    @Inject
    @ApplicationScope
    lateinit var scope: CoroutineScope

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(this.cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.02)
                    .build()
            }
            .eventListener(
                object : EventListener {
                    override fun onSuccess(request: ImageRequest, result: SuccessResult) {
                        super.onSuccess(request, result)
                        result.diskCacheKey?.let { key ->
                            scope.launch {
                                gifsRepository.setGifIsCached(key)
                            }
                        }
                    }
                }
            )
            .build()
    }
}