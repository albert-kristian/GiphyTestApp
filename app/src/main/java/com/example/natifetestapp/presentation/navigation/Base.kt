package com.example.natifetestapp.presentation.navigation

import android.net.Uri

interface BaseRoute {

    companion object {
        const val SCHEME = "notify_test"
    }

    val authority: String

    fun route(): String = buildRouteInternal()
}

fun BaseRoute.buildRoute(
    path: String? = null,
    arguments: Map<String, Any?>? = null
): String {
    val builder = Uri.Builder()
        .scheme(BaseRoute.SCHEME)
        .authority(authority)

    if (!path.isNullOrEmpty()) {
        builder.appendPath(path)
    }

    arguments?.forEach { item ->
        item.value?.let { value ->
            builder.appendQueryParameter(item.key, value.toString())
        }
    }
    return builder.build().toString()
}

fun BaseRoute.buildRouteInternal(
    path: String? = null,
    vararg arguments: String? = emptyArray()
): String {
    val builder = Uri.Builder()
        .scheme(BaseRoute.SCHEME)
        .encodedAuthority(authority)

    if (!path.isNullOrEmpty()) {
        builder.appendEncodedPath(path)
    }

    arguments.forEach { item ->
        builder.appendQueryParameter(item, "{$item}")
    }
    return Uri.decode(builder.build().toString())
}