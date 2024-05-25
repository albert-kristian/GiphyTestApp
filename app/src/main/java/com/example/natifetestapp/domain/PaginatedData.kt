package com.example.natifetestapp.domain

import com.example.natifetestapp.remote.responses.GiphyResponse

data class PaginatedData<T>( // TODO: Use later
    val data: T,
    val pagination: GiphyResponse.Pagination
)