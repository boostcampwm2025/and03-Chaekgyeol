package com.boostcamp.and03.data.repository.book_storage

import com.boostcamp.and03.data.model.request.BookStorageRequest
import com.boostcamp.and03.data.model.request.CharacterRequest
import com.boostcamp.and03.data.model.request.QuoteRequest
import com.boostcamp.and03.data.model.request.TextMemoRequest
import com.boostcamp.and03.data.model.response.BookStorageResponse
import com.boostcamp.and03.data.model.response.CharacterResponse
import com.boostcamp.and03.data.model.response.QuoteResponse
import com.boostcamp.and03.data.model.response.memo.MemoResponse
import com.boostcamp.and03.ui.screen.booklist.model.BookUiModel

interface BookStorageRepository {
    suspend fun getBooks(userId: String): List<BookStorageResponse>

    suspend fun saveBook(userId: String, book: BookStorageRequest)

    suspend fun getCharacters(userId: String, bookId: String): List<CharacterResponse>

    suspend fun addCharacter(userId: String, bookId: String, character: CharacterRequest)

    suspend fun deleteCharacter(userId: String, bookId: String, characterId: String)

    suspend fun getQuotes(userId: String, bookId: String): List<QuoteResponse>

    suspend fun addQuote(userId: String, bookId: String, quote: QuoteRequest)

    suspend fun deleteQuote(userId: String, bookId: String, quoteId: String)

    suspend fun getMemos(userId: String, bookId: String): List<MemoResponse>

    suspend fun addTextMemo(userId: String, bookId: String, memo: TextMemoRequest)

}