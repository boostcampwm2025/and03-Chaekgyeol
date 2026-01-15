package com.boostcamp.and03.data.datasource.remote.book_storage

import com.boostcamp.and03.data.model.request.BookStorageRequest
import com.boostcamp.and03.data.model.response.BookStorageResponse

interface BookStorageDataSource {

    suspend fun getBooks(userId: String): List<BookStorageResponse>

    suspend fun saveBook(
        userId: String,
        book: BookStorageRequest
    )
}