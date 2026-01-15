package com.boostcamp.and03.data.datasource.remote.character

import android.util.Log
import com.boostcamp.and03.data.model.request.CharacterRequest
import com.boostcamp.and03.data.model.response.CharacterResponse
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class CharacterDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
): CharacterDataSource {
    override suspend fun getCharacters(
        userId: String,
        bookId: String
    ): List<CharacterResponse> {
        return try {
            val snapshot = db
                .collection("user")
                .document(userId)
                .collection("book")
                .document(bookId)
                .collection("character")
                .get()
                .await()

            snapshot.documents.mapNotNull { document ->
                document.data?.let { data ->
                    Log.d("CharacterDataSourceImpl", "data: $data")
                    CharacterResponse(
                        id = document.id,
                        role = data["role"] as? String ?: "",
                        description = data["description"] as? String ?: "",
                        name = data["name"] as? String ?: "",
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("CharacterDataSourceImpl", "Error: ${e.message}")
            emptyList()
        }
    }

    override suspend fun addCharacter(
        userId: String,
        bookId: String,
        character: CharacterRequest
    ) {
        try {
            val collectionRef = db
                .collection("user")
                .document(userId)
                .collection("book")
                .document(bookId)
                .collection("character")

            val newDocRef = collectionRef.document()
            newDocRef.set(character).await()

            Log.d("CharacterDataSourceImpl", "Character added: ${newDocRef.id}")
        } catch (e: Exception) {
            Log.e("CharacterDataSourceImpl", "Failed to add character: ${e.message}")
            throw e
        }
    }

    override suspend fun deleteCharacter(
        userId: String,
        bookId: String,
        characterId: String
    ) {
        try {
            db.collection("user")
                .document(userId)
                .collection("book")
                .document(bookId)
                .collection("character")
                .document(characterId)
                .delete()
                .await()

            Log.d("CharacterDataSourceImpl", "Character deleted: $characterId")
        } catch (e: Exception) {
            Log.e("CharacterDataSourceImpl", "Failed to delete character: ${e.message}")
            throw e
        }
    }
}
