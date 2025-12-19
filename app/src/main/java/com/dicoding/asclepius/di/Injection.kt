package com.dicoding.asclepius.di
import android.content.Context
import com.dicoding.asclepius.data.local.DB.HistoryRoomDB
import com.dicoding.asclepius.data.remote.retrofit.ApiConfig
import com.dicoding.asclepius.data.repo.CancerRepo


object Injection {

    fun provideRepository(context: Context): CancerRepo {
        val apiService = ApiConfig.getApiService()
        val database =  HistoryRoomDB.getDatabase(context)
        val dao = database.historyDao()
        return CancerRepo.getInstance(apiService, dao)
    }

}