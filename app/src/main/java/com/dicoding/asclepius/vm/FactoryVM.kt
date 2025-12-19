package com.dicoding.asclepius.vm
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.data.repo.CancerRepo
import com.dicoding.asclepius.di.Injection


class FactoryVM(

    private val cancerRepository: CancerRepo

) : ViewModelProvider.NewInstanceFactory() {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BaseVM::class.java)) {
            return BaseVM(cancerRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class: " + modelClass.name)
    }


    companion object {
        @Volatile
        private var instance: FactoryVM? = null
        fun getInstance(context: Context): Any =
            instance ?: synchronized(this) {
                val repository = Injection.provideRepository(context)
                instance ?: FactoryVM(repository)
            }.also { instance = it }
    }
}