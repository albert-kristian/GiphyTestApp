package com.example.natifetestapp.remote.interceptors

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ApiTokenInterceptor(
    private val apiKey: String
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return chain.proceed(request.addApiKey())
    }

    private fun Request.addApiKey(): Request {
        val newUrl = url()
            .newBuilder()
            .addQueryParameter("api_key", apiKey)
            .build()
        return newBuilder()
            .url(newUrl)
            .build()
    }
}