package com.example.project_enlishlearning.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_enlishlearning.data.local.database.AppDatabase
import com.example.project_enlishlearning.data.local.entity.VocabularySetEntity
import com.example.project_enlishlearning.data.local.entity.VocabularyWordEntity
import com.example.project_enlishlearning.data.importexport.ImportPreview
import com.example.project_enlishlearning.data.importexport.ImportResult
import com.example.project_enlishlearning.data.importexport.VocabularyExportFormatter
import com.example.project_enlishlearning.data.importexport.VocabularyImportParser
import com.example.project_enlishlearning.data.repository.VocabularyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.project_enlishlearning.utils.FirebaseManager
import java.io.ByteArrayInputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Sử dụng AndroidViewModel(application) để lấy được Context mở Database
class VocabularyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = VocabularyRepository(
        vocabularyDao = AppDatabase.getDatabase(application).vocabularyDao(),
        importParser = VocabularyImportParser(),
        exportFormatter = VocabularyExportFormatter()
    )

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

    private val _exportState = MutableStateFlow<ExportState>(ExportState.Idle)
    val exportState: StateFlow<ExportState> = _exportState.asStateFlow()

    private val _importPreviewState = MutableStateFlow<ImportPreviewState>(ImportPreviewState.Idle)
    val importPreviewState: StateFlow<ImportPreviewState> = _importPreviewState.asStateFlow()

    private val _importState = MutableStateFlow<ImportState>(ImportState.Idle)
    val importState: StateFlow<ImportState> = _importState.asStateFlow()

    private val _selectedImportFileName = MutableStateFlow<String?>(null)
    val selectedImportFileName: StateFlow<String?> = _selectedImportFileName.asStateFlow()

    private var cachedPreview: ImportPreview? = null

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
            repository.getAllSetsByUserId(currentUserId).collect { sets ->
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
            repository.insertSet(newSet)
        }
    }


    // ==========================================
    // CÁC HÀM THAO TÁC VỚI TỪ VỰNG CHI TIẾT
    // ==========================================

    // Hàm gọi khi người dùng bấm vào xem 1 bộ cụ thể (truyền ID của bộ đó vào)
    fun loadWordsForSet(setId: Int) {
        viewModelScope.launch {
            repository.getWordsBySetId(setId).collect { words ->
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
            repository.insertWord(newWord)

            // 2. Tăng số đếm totalWords của bộ từ vựng này lên 1
            repository.incrementTotalWords(setId)
        }
    }

    // Hàm gọi khi xoá từ vựng
    fun deleteWord(word: VocabularyWordEntity) {
        viewModelScope.launch {
            // 1. Xóa từ vựng khỏi Database
            repository.deleteWord(word)

            // 2. Giảm số đếm totalWords đi 1 dựa vào setId của từ vừa xóa
            repository.decrementTotalWords(word.setId)
        }
    }

    // Hàm bấm nút thả tim (Yêu thích)
    fun toggleFavorite(word: VocabularyWordEntity) {
        viewModelScope.launch {
            val updatedWord = word.copy(isFavorite = !word.isFavorite)
            repository.updateWord(updatedWord)
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
            repository.updateSet(updatedSet)
        }
    }

    fun deleteVocabularySet(set: VocabularySetEntity) {
        viewModelScope.launch {
            repository.deleteSet(set)
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
            repository.updateWord(updatedWord)
        }
    }

    fun loadWordById(wordId: Int) {
        viewModelScope.launch {
            _currentEditWord.value = repository.getWordById(wordId)
        }
    }

    fun exportVocabularySet(setId: Int, setTitle: String?) {
        viewModelScope.launch {
            _exportState.value = ExportState.Loading
            try {
                val csvContent = repository.exportVocabularySet(setId)
                val timeStamp = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
                val safeTitle = sanitizeFileName(setTitle ?: "VocabularySet")
                val fileName = "${safeTitle}_$timeStamp.csv"
                _exportState.value = ExportState.Ready(csvContent = csvContent, fileName = fileName)
            } catch (exception: Exception) {
                _exportState.value = ExportState.Error(exception.message ?: "Export failed")
            }
        }
    }

    fun resetExportState() {
        _exportState.value = ExportState.Idle
    }

    fun previewImport(setId: Int, fileName: String, fileBytes: ByteArray?) {
        viewModelScope.launch {
            _importPreviewState.value = ImportPreviewState.Loading
            _importState.value = ImportState.Idle
            try {
                _selectedImportFileName.value = fileName
                if (fileBytes == null) {
                    throw IllegalStateException("Unable to read selected file")
                }
                val preview = repository.previewImport(
                    inputStream = ByteArrayInputStream(fileBytes),
                    fileName = fileName
                )
                cachedPreview = preview
                _importPreviewState.value = ImportPreviewState.Success(preview)
            } catch (exception: Exception) {
                _importPreviewState.value = ImportPreviewState.Error(
                    exception.message ?: "Unable to parse selected file"
                )
            }
        }
    }

    fun confirmImport(setId: Int) {
        val preview = cachedPreview
        if (preview == null) {
            _importState.value = ImportState.Error("No preview data available")
            return
        }

        viewModelScope.launch {
            _importState.value = ImportState.Loading
            try {
                val result = repository.importFromPreview(setId, preview)
                _importState.value = ImportState.Success(result)
            } catch (exception: Exception) {
                _importState.value = ImportState.Error(
                    exception.message ?: "Import failed"
                )
            }
        }
    }

    fun resetImportState() {
        cachedPreview = null
        _selectedImportFileName.value = null
        _importPreviewState.value = ImportPreviewState.Idle
        _importState.value = ImportState.Idle
    }

    private fun sanitizeFileName(input: String): String {
        val trimmed = input.trim()
        if (trimmed.isBlank()) return "VocabularySet"
        return trimmed.replace(Regex("[^A-Za-z0-9_-]+"), "_")
    }
}

sealed class ExportState {
    data object Idle : ExportState()
    data object Loading : ExportState()
    data class Ready(val csvContent: String, val fileName: String) : ExportState()
    data class Error(val message: String) : ExportState()
}

sealed class ImportPreviewState {
    data object Idle : ImportPreviewState()
    data object Loading : ImportPreviewState()
    data class Success(val preview: ImportPreview) : ImportPreviewState()
    data class Error(val message: String) : ImportPreviewState()
}

sealed class ImportState {
    data object Idle : ImportState()
    data object Loading : ImportState()
    data class Success(val result: ImportResult) : ImportState()
    data class Error(val message: String) : ImportState()
}