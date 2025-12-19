package com.dicoding.asclepius.vm
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.databinding.ItemsInfoBinding

class InfoAdapter(private val onItemClick: ((Int) -> Unit)? = null) :
    ListAdapter<ArticlesItem, InfoAdapter.InfoViewHolder>(DIFF_CALLBACK) {


    class InfoViewHolder(
        private val binding: ItemsInfoBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(Info: ArticlesItem) {
            Info.let { it ->
                Glide.with(binding.ivThumbnail.context)
                    .load(it.urlToImage)
                    .into(binding.ivThumbnail)
                binding.tvName.text = it.title
                binding.tvItem1.text = it.description
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        val binding = ItemsInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InfoViewHolder(binding)
    }


    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>() {
            override fun areItemsTheSame(
                oldItem: ArticlesItem,
                newItem: ArticlesItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ArticlesItem,
                newItem: ArticlesItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}