package com.boostcamp.and03.data.datasource.remote.search.naver

import com.boostcamp.and03.data.api.BookSearchApiService
import com.boostcamp.and03.data.model.response.BookSearchResponse
import javax.inject.Inject

class BookSearchRemoteDataSourceImpl @Inject constructor(
    private val bookSearchApiService: BookSearchApiService
) : BookSearchRemoteDataSource {

    override suspend fun loadBooks(query: String, display: Int, start: Int): BookSearchResponse =
        bookSearchApiService.loadBooks(
            query = query,
            display = display,
            start = start
        )
}