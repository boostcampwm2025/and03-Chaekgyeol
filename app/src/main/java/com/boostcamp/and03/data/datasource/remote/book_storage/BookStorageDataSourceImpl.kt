package com.boostcamp.and03.data.datasource.remote.book_storage

import android.util.Log
import com.boostcamp.and03.data.model.request.BookStorageRequest
import com.boostcamp.and03.data.model.response.BookStorageResponse
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.jvm.java

class BookStorageDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
): BookStorageDataSource {
    override suspend fun getBooks(userId: String): List<BookStorageResponse> {
        return try {
            val books = db.collection("user")
                .document(userId)
                .collection("book")
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(BookStorageResponse::class.java) }

            books
        } catch (e: Exception) {
            Log.e("BookStorage", "Error: ${e.message}")
            emptyList()
        }
    }

    override suspend fun saveBook(userId: String, book: BookStorageRequest) {
        db.collection("user")
            .document(userId)
            .collection("book")
            .document()
            .set(book)
            .await()
    }
}
