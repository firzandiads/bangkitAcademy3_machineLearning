package com.dicoding.asclepius.vm
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.local.model.HistoryDB
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.data.repo.CancerRepo
import kotlinx.coroutines.launch
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat

class BaseVM(private val cancerRepository: CancerRepo) : ViewModel() {


    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _cancerInfo = MutableLiveData<List<ArticlesItem>?>()
    val cancerInfo: LiveData<List<ArticlesItem>?> = _cancerInfo

    private val _currentImageUri = MutableLiveData<Uri?>()
    val currentImageUri: LiveData<Uri?> = _currentImageUri

    private val _isLoading = MutableLiveData<Boolean?>()
    val isLoading: LiveData<Boolean?> = _isLoading

    private val _history = MutableLiveData<List<HistoryDB?>>()
    val history: LiveData<List<HistoryDB?>> = _history

    private val _historyById = MutableLiveData<HistoryDB>()
    val historyById: LiveData<HistoryDB> = _historyById


    init {
        listCancerInfo()
        listHistory()
    }


    fun listCancerInfo() {
        _isLoading.value = true
        viewModelScope.launch {
            val result = cancerRepository.getCancerInfo()
            result.onSuccess {
                _cancerInfo.value = it
                _isLoading.value = false
                clearErrorMessage()
            }.onFailure {
                _errorMessage.value = it.message
                _isLoading.value = false
            }
        }
    }


    fun listHistory() {
        _isLoading.value = true
        cancerRepository.getAllHistory().observeForever { history ->
            Log.d("MainViewModel", "History: $history")
            _isLoading.value = false
            _history.value = history
            clearErrorMessage()
        }
    }


    fun historyById(id: String?) {
        _isLoading.value = true
        cancerRepository.getHistoryById(id).observeForever { history ->
            _isLoading.value = false
            _historyById.value = history
            clearErrorMessage()
        }
    }


    fun insertHistory(uri: Uri, classifications: List<Classifications>) {
        viewModelScope.launch {
            val uriString = uri.toString()
            val resultString =
                classifications.joinToString { it.categories.joinToString { category -> category.label } }
            val percentageString = classifications.joinToString {
                it.categories.joinToString { category ->
                    NumberFormat.getPercentInstance()
                        .format(category.score).trim()
                }
            }
            val success = cancerRepository.insertHistory(uriString, resultString, percentageString)
            if (!success) {
                _errorMessage.value = "Failed to save data"
            }
        }
    }


    fun saveUri(uri: Uri) {
        viewModelScope.launch {
            _currentImageUri.value = uri
        }
    }


    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}