package com.dicoding.asclepius.data.repo
import android.util.Log
import androidx.lifecycle.LiveData
import com.dicoding.asclepius.data.local.DB.HistoryDao
import com.dicoding.asclepius.data.local.model.HistoryDB
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.data.remote.retrofit.ApiService

class CancerRepo(

    private val historyDao: HistoryDao,
    private val apiService: ApiService

) {


    suspend fun getCancerInfo(): Result<List<ArticlesItem>> {
        return try {
            val response = apiService.getAllInfo()
            if (response.isSuccessful) {
                Result.success(response.body()?.articles ?: emptyList())
            } else {
                Result.failure(Exception("Failed to load data from Api, Status code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    fun getAllHistory(): LiveData<List<HistoryDB>> {
        return historyDao.getAllHistory()
    }


    fun getHistoryById(id: String?): LiveData<HistoryDB> {
        return historyDao.getHistoryById(id)
    }


    suspend fun insertHistory(uri: String, category: String, percentage: String): Boolean {
        return try {
            val historyDB = HistoryDB(
                uri = uri,
                category = category,
                percentage = percentage
            )
            val insertResult = historyDao.insertHistoryDB(historyDB)
            Log.d("Insert History", "History: $insertResult")
            insertResult != -1L
        } catch (e: Exception) {
            Log.e("Insert History", "Error inserting history: ${e.message}")
            false
        }
    }


    companion object {
        @Volatile
        private var instance: CancerRepo? = null
        fun getInstance(
            apiService: ApiService,
            historyDao: HistoryDao
        ): CancerRepo =
            instance ?: synchronized(this) {
                instance ?: CancerRepo(historyDao, apiService)
            }
                .also { instance = it }
    }
}