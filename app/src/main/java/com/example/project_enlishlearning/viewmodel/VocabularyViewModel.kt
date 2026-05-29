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

// Sử dụng AndroidViewModel(application) để lấy được Context mở Database
class VocabularyViewModel(application: Application) : AndroidViewModel(application) {

    // 1. Khởi tạo người quản lý kho (DAO)
    private val dao = AppDatabase.getDatabase(application).vocabularyDao()

    // 2. Biến chứa danh sách BỘ TỪ VỰNG (giao diện sẽ "lắng nghe" biến này)
    private val _vocabularySets = MutableStateFlow<List<VocabularySetEntity>>(emptyList())
    val vocabularySets: StateFlow<List<VocabularySetEntity>> = _vocabularySets.asStateFlow()

    // 3. Biến chứa danh sách TỪ VỰNG CHI TIẾT của 1 bộ
    private val _wordsInSet = MutableStateFlow<List<VocabularyWordEntity>>(emptyList())
    val wordsInSet: StateFlow<List<VocabularyWordEntity>> = _wordsInSet.asStateFlow()

    init {
        // Vừa vào app là tự động gọi hàm lấy danh sách các Bộ từ vựng luôn
        loadAllSets()
    }

    // ==========================================
    // CÁC HÀM THAO TÁC VỚI BỘ TỪ VỰNG
    // ==========================================
    private fun loadAllSets() {
        viewModelScope.launch {
            // Lắng nghe liên tục từ Database (collect). Có thay đổi là _vocabularySets cập nhật ngay.
            dao.getAllSets().collect { sets ->
                _vocabularySets.value = sets
            }
        }
    }

    // Hàm gọi khi bấm nút "Tạo bộ từ vựng mới"
    fun addVocabularySet(title: String, description: String) {
        viewModelScope.launch {
            val newSet = VocabularySetEntity(
                title = title,
                description = description
            )
            dao.insertSet(newSet)
        }
    }

    fun deleteSet(set: VocabularySetEntity) {
        viewModelScope.launch {
            dao.deleteSet(set)
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
            dao.insertWord(newWord)

            // TODO (Nâng cao): Có thể gọi thêm hàm updateSet để tăng totalWords lên 1
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
        viewModelScope.launch {
            val updatedSet = VocabularySetEntity(
                setId = setId,
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
}