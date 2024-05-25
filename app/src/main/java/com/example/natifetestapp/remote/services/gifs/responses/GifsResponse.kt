package com.example.natifetestapp.remote.services.gifs.responses

typealias GifsResponse = List<GifResponse>

data class GifResponse(
    val type: String,
    val id: String,
    val title: String
)