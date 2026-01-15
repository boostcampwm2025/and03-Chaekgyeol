package com.boostcamp.and03.data.datasource.remote.memo

import android.util.Log
import com.boostcamp.and03.data.model.request.TextMemoRequest
import com.boostcamp.and03.data.model.response.memo.CanvasMemoResponse
import com.boostcamp.and03.data.model.response.memo.EdgeResponse
import com.boostcamp.and03.data.model.response.memo.GraphResponse
import com.boostcamp.and03.data.model.response.memo.MemoResponse
import com.boostcamp.and03.data.model.response.memo.NodeResponse
import com.boostcamp.and03.data.model.response.memo.TextMemoResponse
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class MemoDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
): MemoDataSource {
    override suspend fun getMemos(
        userId: String,
        bookId: String
    ): List<MemoResponse> {
        return try {
            val snapshot = db
                .collection("user")
                .document(userId)
                .collection("book")
                .document(bookId)
                .collection("memo")
                .get()
                .await()

            snapshot.documents.mapNotNull { document ->
                val data = document.data ?: return@mapNotNull null
                parseMemo(document.id, data)
            }

        } catch (e: Exception) {
            Log.e("MemoDataSourceImpl", "Error: ${e.message}")
            emptyList()
        }
    }

    private fun parseMemo(
        documentId: String,
        data: Map<String, Any>
    ): MemoResponse? {
        val type = data["type"] as? String ?: return null

        return when (type) {
            "TEXT" -> parseTextMemo(documentId, data)
            "CANVAS" -> parseCanvasMemo(documentId, data)
            else -> null
        }
    }

    private fun parseTextMemo(
        id: String,
        data: Map<String, Any>
    ): TextMemoResponse =
        TextMemoResponse(
            id = id,
            title = data["title"] as? String ?: "",
            content = data["content"] as? String ?: "",
            createdAt = data["createdAt"] as? String ?: "",
            type = "TEXT",
            startPage = (data["startPage"] as? Long)?.toInt() ?: 0,
            endPage = (data["endPage"] as? Long)?.toInt() ?: 0,
        )

    private fun parseCanvasMemo(
        id: String,
        data: Map<String, Any>
    ): CanvasMemoResponse =
        @Suppress("UNCHECKED_CAST")
        CanvasMemoResponse(
            id = id,
            title = data["title"] as? String ?: "",
            createdAt = data["createdAt"] as? String ?: "",
            type = "CANVAS",
            startPage = (data["startPage"] as? Long)?.toInt() ?: 0,
            endPage = (data["endPage"] as? Long)?.toInt() ?: 0,
            graph = parseGraph(data["graph"] as? Map<String, Any>)
        )

    private fun parseGraph(data: Map<String, Any>?): GraphResponse {
        if (data == null) {
            return GraphResponse(emptyList(), emptyList())
        }

        @Suppress("UNCHECKED_CAST")
        val nodes = (data["nodes"] as? List<Map<String, Any>>).orEmpty().map {
            NodeResponse(
                id = it["id"] as? String ?: "",
                title = it["title"] as? String ?: "",
                content = it["content"] as? String ?: "",
                nodeType = it["nodeType"] as? String ?: "",
                x = (it["x"] as? Double)?.toFloat() ?: 0f,
                y = (it["y"] as? Double)?.toFloat() ?: 0f,
            )
        }

        @Suppress("UNCHECKED_CAST")
        val edges = (data["edges"] as? List<Map<String, Any>>).orEmpty().map {
            EdgeResponse(
                id = it["id"] as? String ?: "",
                fromNodeId = it["fromNodeId"] as? String ?: "",
                toNodeId = it["toNodeId"] as? String ?: "",
                relationText = it["relationText"] as? String ?: ""
            )
        }

        return GraphResponse(nodes, edges)
    }

    override suspend fun addTextMemo(userId: String, bookId: String, memo: TextMemoRequest) {
        try {
            val data = hashMapOf(
                "title" to memo.title,
                "content" to memo.content,
                "type" to memo.type,
                "startPage" to memo.startPage,
                "endPage" to memo.endPage,
                "createdAt" to FieldValue.serverTimestamp()
            )

            val collectionRef = db
                .collection("user")
                .document(userId)
                .collection("book")
                .document(bookId)
                .collection("memo")

            val newDocRef = collectionRef.document()
            newDocRef.set(data).await()

            Log.d("MemoDataSourceImpl", "Memo added: ${newDocRef.id}")
        } catch (e: Exception) {
            Log.e("MemoDataSourceImpl", "Failed to add memo: ${e.message}")
            throw e
        }
    }

}