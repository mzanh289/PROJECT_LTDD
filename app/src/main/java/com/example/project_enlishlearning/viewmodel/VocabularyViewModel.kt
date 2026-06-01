package com.example.project_enlishlearning.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_enlishlearning.data.local.database.AppDatabase
import com.example.project_enlishlearning.data.local.entity.VocabularySetEntity
import com.example.project_enlishlearning.data.local.entity.VocabularyWordEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.project_enlishlearning.utils.FirebaseManager

// Sử dụng AndroidViewModel(application) để lấy được Context mở Database
class VocabularyViewModel(application: Application) : AndroidViewModel(application) {

    // 1. Khởi tạo người quản lý kho (DAO)
    private val dao = AppDatabase.getDatabase(application).vocabularyDao()

    // Lấy userId hiện tại từ Firebase
    private val currentUserId: String
        get() = FirebaseManager.auth.currentUser?.uid ?: ""

    // 2. Biến chứa danh sách BỘ TỪ VỰNG (giao diện sẽ "lắng nghe" biến này)
    private val _vocabularySets = MutableStateFlow<List<VocabularySetEntity>>(emptyList())
    val vocabularySets: StateFlow<List<VocabularySetEntity>> = _vocabularySets.asStateFlow()

    // 3. Biến chứa danh sách TỪ VỰNG CHI TIẾT của 1 bộ
    private val _wordsInSet = MutableStateFlow<List<VocabularyWordEntity>>(emptyList())
    val wordsInSet: StateFlow<List<VocabularyWordEntity>> = _wordsInSet.asStateFlow()

    private val _currentEditWord = MutableStateFlow<VocabularyWordEntity?>(null)
    val currentEditWord: StateFlow<VocabularyWordEntity?> = _currentEditWord.asStateFlow()

    init {
        // Vừa vào app là tự động gọi hàm lấy danh sách các Bộ từ vựng luôn
        loadAllSets()
    }

    // ==========================================
    // CÁC HÀM THAO TÁC VỚI BỘ TỪ VỰNG
    // ==========================================
    private fun loadAllSets() {
        if (currentUserId.isEmpty()) return // Bỏ qua nếu user chưa đăng nhập

        viewModelScope.launch {
            // Thay đổi để gọi hàm lấy danh sách theo userId
            dao.getAllSetsByUserId(currentUserId).collect { sets ->
                _vocabularySets.value = sets
            }
        }
    }

    // Hàm gọi khi bấm nút "Tạo bộ từ vựng mới"
    fun addVocabularySet(title: String, description: String) {
        if (currentUserId.isEmpty()) return // Bỏ qua nếu user chưa đăng nhập

        viewModelScope.launch {
            val newSet = VocabularySetEntity(
                userId = currentUserId, // Truyền userId vào entity
                title = title,
                description = description
            )
            dao.insertSet(newSet)
        }
    }


    // ==========================================
    // CÁC HÀM THAO TÁC VỚI TỪ VỰNG CHI TIẾT
    // ==========================================

    // Hàm gọi khi người dùng bấm vào xem 1 bộ cụ thể (truyền ID của bộ đó vào)
    fun loadWordsForSet(setId: Int) {
        viewModelScope.launch {
            dao.getWordsBySetId(setId).collect { words ->
                _wordsInSet.value = words
            }
        }
    }

    // Hàm gọi khi bấm nút "Thêm từ vựng"
    fun addWord(setId: Int, word: String, pronunciation: String, meaning: String, example: String) {
        viewModelScope.launch {
            val newWord = VocabularyWordEntity(
                setId = setId,
                word = word,
                pronunciation = pronunciation,
                meaning = meaning,
                example = example
            )
            // 1. Thêm từ vựng vào Database
            dao.insertWord(newWord)

            // 2. Tăng số đếm totalWords của bộ từ vựng này lên 1
            dao.incrementTotalWords(setId)
        }
    }

    // Hàm gọi khi xoá từ vựng
    fun deleteWord(word: VocabularyWordEntity) {
        viewModelScope.launch {
            // 1. Xóa từ vựng khỏi Database
            dao.deleteWord(word)

            // 2. Giảm số đếm totalWords đi 1 dựa vào setId của từ vừa xóa
            dao.decrementTotalWords(word.setId)
        }
    }

    // Hàm bấm nút thả tim (Yêu thích)
    fun toggleFavorite(word: VocabularyWordEntity) {
        viewModelScope.launch {
            val updatedWord = word.copy(isFavorite = !word.isFavorite)
            dao.updateWord(updatedWord)
        }
    }

    fun updateVocabularySet(setId: Int, title: String, description: String, totalWords: Int, progress: Int) {
        if (currentUserId.isEmpty()) return // Bỏ qua nếu user chưa đăng nhập

        viewModelScope.launch {
            val updatedSet = VocabularySetEntity(
                setId = setId,
                userId = currentUserId, // Chắc chắn truyền lại userId
                title = title,
                description = description,
                totalWords = totalWords,
                progress = progress
            )
            dao.updateSet(updatedSet)
        }
    }

    fun deleteVocabularySet(set: VocabularySetEntity) {
        viewModelScope.launch {
            dao.deleteSet(set)
        }
    }

    fun updateWord(
        wordId: Long,
        setId: Int,
        word: String,
        pronunciation: String,
        meaning: String,
        example: String,
        status: String,
        isFavorite: Boolean
    ) {
        viewModelScope.launch {
            val updatedWord = VocabularyWordEntity(
                wordId = wordId,
                setId = setId,
                word = word,
                pronunciation = pronunciation,
                meaning = meaning,
                example = example,
                status = status,
                isFavorite = isFavorite
            )
            dao.updateWord(updatedWord)
        }
    }

    fun loadWordById(wordId: Long) {
        viewModelScope.launch {
            _currentEditWord.value = dao.getWordById(wordId)
        }
    }
}