package com.boostcamp.and03.ui.screen.bookdetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.and03.data.model.request.TextMemoRequest
import com.boostcamp.and03.data.repository.book_storage.BookStorageRepository
import com.boostcamp.and03.ui.screen.bookdetail.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val bookRepository: BookStorageRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(BookDetailUiState())
    val uiState: StateFlow<BookDetailUiState> = _uiState.asStateFlow()

    init {
        initPreviewData()
        loadAllData()
    }

    private fun initPreviewData() {
        _uiState.value = BookDetailUiState(
            thumbnail = "https://i.pinimg.com/736x/8f/20/41/8f2041520696507bc2bfd2f5648c8da3.jpg",
            title = "Harry Potter and the Philosopher's Stone",
            author = "J.K. Rowling",
            publisher = "Bloomsbury Publishing",
//            memos = persistentListOf(
//                MemoUiModel(
//                    id = "memo_1",
//                    memoType = MemoType.CANVAS,
//                    startPage = 1,
//                    endPage = 26,
//                    date = "2025.12.24",
//                    title = "인물 관계도 정리",
//                    content = null
//                ),
//                MemoUiModel(
//                    id = "memo_2",
//                    memoType = MemoType.TEXT,
//                    startPage = 5,
//                    endPage = 8,
//                    date = "2025.12.24",
//                    title = "마법 세계관 메모",
//                    content = "해리 포터 세계관에서 마법은 타고난 재능과 훈련이 결합된 능력으로 묘사된다. 머글과 마법사의 대비가 인상적이었다."
//                ),
//                MemoUiModel(
//                    id = "memo_3",
//                    memoType = MemoType.TEXT,
//                    startPage = 20,
//                    endPage = 22,
//                    date = "2025.12.25",
//                    title = "인상 깊은 문장",
//                    content = "선택이야말로 우리가 누구인지를 보여준다. 능력보다 더 중요한 것은 선택이라는 메시지가 강하게 남았다."
//                ),
//                MemoUiModel(
//                    id = "memo_4",
//                    memoType = MemoType.CANVAS,
//                    startPage = 1,
//                    endPage = 50,
//                    date = "2025.12.26",
//                    title = "주요 인물 관계도",
//                    content = null
//                )
//            )
        )
    }

    fun loadAllData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                loadCharacters()
                loadQuotes()
                loadMemos()
                addTextMemo()
                _uiState.update { it.copy(isLoading = false) }
//                throw Exception("테스트용")
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "데이터를 불러오는데 실패했습니다.")
                }
                Log.d("BookDetailViewModel", "loadAllData: ${e.message}")
            }
        }
    }

    private suspend fun loadCharacters(
        userId: String = "O12OmGoVY8FPYFElNjKN",
        bookId: String = "YkFyRg6G0v2Us6b3V5Tm"
    ) {
        val result = bookRepository.getCharacters(userId, bookId)
        _uiState.update {
            it.copy(
                characters = result.map { character -> character.toUiModel() }
                    .toPersistentList()
            )
        }
    }

    private suspend fun loadQuotes(
        userId: String = "O12OmGoVY8FPYFElNjKN",
        bookId: String = "YkFyRg6G0v2Us6b3V5Tm"
    ) {
            val result = bookRepository.getQuotes(userId, bookId)
            _uiState.update {
                it.copy(
                    quotes = result.map { quote -> quote.toUiModel() }.toPersistentList()
                )
            }
    }
//
//    fun addCharacter(
//        userId: String = "O12OmGoVY8FPYFElNjKN",
//        bookId: String = "YkFyRg6G0v2Us6b3V5Tm"
//    ) {
//        viewModelScope.launch {
//            bookRepository.addCharacter(
//                userId, bookId, CharacterRequest(
//                    role = "주인공",
//                    description = "설명설명설명",
//                    name = "이름이름"
//                )
//            )
//        }
//    }
//
//    fun addQuote(
//        userId: String = "O12OmGoVY8FPYFElNjKN",
//        bookId: String = "YkFyRg6G0v2Us6b3V5Tm"
//    ) {
//        viewModelScope.launch {
//            bookRepository.addQuote(
//                userId, bookId, QuoteRequest(
//                    content = "어쩌고저쩌고어쩌고저쩌고어쩌고저쩌고어쩌고저쩌고",
//                    createdAt = "2026.1.5",
//                    page = 121
//                )
//            )
//        }
//    }
//
//    fun deleteCharacter(
//        characterId: String = "e9WCxbOGW15gzMK9Dbnc",
//        userId: String = "O12OmGoVY8FPYFElNjKN",
//        bookId: String = "YkFyRg6G0v2Us6b3V5Tm"
//    ) {
//        viewModelScope.launch {
//            bookRepository.deleteCharacter(
//                userId, bookId, characterId)
//        }
//    }

    private suspend fun loadMemos(
        userId: String = "O12OmGoVY8FPYFElNjKN",
        bookId: String = "YkFyRg6G0v2Us6b3V5Tm"
    ) {
        val result = bookRepository.getMemos(userId, bookId)
        _uiState.update {
            it.copy(
                memos = result.map { memo -> memo.toUiModel() }.toPersistentList()
            )
        }
    }

    private suspend fun addTextMemo(
        userId: String = "O12OmGoVY8FPYFElNjKN",
        bookId: String = "YkFyRg6G0v2Us6b3V5Tm"
    ) {
        bookRepository.addTextMemo(
            userId, bookId, TextMemoRequest(
                title = "메모 제목",
                content = "메모 내용",
                startPage = 1,
                endPage = 2
            )
        )
    }
}