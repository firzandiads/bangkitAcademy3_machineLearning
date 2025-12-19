package com.dicoding.asclepius.ui
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.vm.BaseVM
import com.dicoding.asclepius.vm.InfoAdapter
import com.dicoding.asclepius.vm.FactoryVM
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.databinding.ActivityInfoBinding

class InfoActivity : AppCompatActivity() {


    private lateinit var binding: ActivityInfoBinding
    private lateinit var adapter: InfoAdapter
    private val baseVM: BaseVM by viewModels {
        FactoryVM.getInstance(this) as ViewModelProvider.Factory
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Info"

        setupRecyclerView()
        setupAdapter()
        observeViewModel()
    }


    private fun setupRecyclerView() {
        binding.rvInfo.layoutManager = LinearLayoutManager(this)
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.rvInfo.addItemDecoration(itemDecoration)
    }


    private fun setupAdapter() {
        adapter = InfoAdapter()
        binding.rvInfo.adapter = adapter
    }


    private fun observeViewModel() {
        baseVM.cancerInfo.observe(this) { listItem ->
            if (listItem != null) {
                setInfo(listItem)
                baseVM.clearErrorMessage()
            }
        }

        baseVM.errorMessage.observe(this) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage,Toast.LENGTH_SHORT).show()
                baseVM.clearErrorMessage()
            }
        }
    }


    private fun setInfo(lifeInfo: List<ArticlesItem>) {
        val filteredInfo = lifeInfo.filter {
            it.title != "[Removed]" && it.description != "[Removed]"
        }
        adapter.submitList(filteredInfo)
    }

}