package com.dicoding.asclepius.ui
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.vm.BaseVM
import com.dicoding.asclepius.vm.FactoryVM
import com.dicoding.asclepius.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {


    private lateinit var binding: ActivityResultBinding
    private val baseVM: BaseVM by viewModels {
        FactoryVM.getInstance(this) as ViewModelProvider.Factory
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val historyId = intent.getStringExtra("historyId")

        if (historyId != null) {
            baseVM.historyById.observe(this) { history ->
                history?.let {
                    val uriString = it.uri
                    if (!uriString.isNullOrEmpty()) {
                        binding.resultImage.setImageURI(Uri.parse(uriString))
                    } else {
                        Log.e("ResultActivity", "URI is null or empty")
                    }
                    binding.resultText.text = "${it.category} ${it.percentage}"
                }
            }
            baseVM.historyById(historyId)
        } else {
            val imageUriString = intent.getStringExtra(EXTRA_IMAGE_URI)
            val result: String? = intent.getStringExtra(EXTRA_RESULT)

            Log.d("ResultActivity", "Received imageUri: $imageUriString, result: $result")

            if (!imageUriString.isNullOrEmpty()) {
                val imageUri = Uri.parse(imageUriString)
                binding.resultImage.setImageURI(imageUri)
            } else {
                Log.e("ResultActivity", "Image URI is null or empty")
            }

            result?.let {
                binding.resultText.text = it
            }
        }


        binding.btnTryAgain.setOnClickListener {
            finish()
        }
    }


    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
    }
}
