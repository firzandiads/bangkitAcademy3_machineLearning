package com.dicoding.asclepius.ui
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.vm.HistoryAdapter
import com.dicoding.asclepius.vm.BaseVM
import com.dicoding.asclepius.vm.FactoryVM
import com.dicoding.asclepius.data.local.model.HistoryDB
import com.dicoding.asclepius.databinding.ActivityHistoryBinding


class HistoryActivity : AppCompatActivity() {


    private lateinit var binding: ActivityHistoryBinding
    private lateinit var adapter: HistoryAdapter
    private val baseVM: BaseVM by viewModels {
        FactoryVM.getInstance(this) as ViewModelProvider.Factory

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "History"

        setupRecyclerView()
        setupAdapter()
        observeViewModel()
    }


    private fun setupRecyclerView() {
        binding.rvHistory.layoutManager = LinearLayoutManager(this)
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.rvHistory.addItemDecoration(itemDecoration)
    }


    private fun setupAdapter() {
        adapter = HistoryAdapter { historyId ->
            val intent = Intent(this, ResultActivity::class.java).apply {
                putExtra("historyId", historyId)
            }
            Log.d("HistoryActivity", "historyId: $historyId")
            startActivity(intent)
        }
        binding.rvHistory.adapter = adapter
    }


    private fun observeViewModel() {
        baseVM.history.observe(this) { listItem ->
            setHistory(listItem)
            baseVM.clearErrorMessage()
        }

        baseVM.errorMessage.observe(this) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage,Toast.LENGTH_SHORT).show()
                baseVM.clearErrorMessage()
            }
        }
    }


    private fun setHistory(lifeHistory: List<HistoryDB?>) {
        adapter.submitList(lifeHistory)
    }
}