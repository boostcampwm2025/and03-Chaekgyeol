package com.boostcamp.and03.data.datasource.remote.memo

import com.boostcamp.and03.data.model.request.TextMemoRequest
import com.boostcamp.and03.data.model.response.memo.MemoResponse

interface MemoDataSource {

    suspend fun getMemos(
        userId: String,
        bookId: String
    ): List<MemoResponse>

    suspend fun addTextMemo(
        userId: String,
        bookId: String,
        memo: TextMemoRequest
    )

}