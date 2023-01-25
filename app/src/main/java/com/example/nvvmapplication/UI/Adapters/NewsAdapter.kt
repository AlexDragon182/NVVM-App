package com.example.nvvmapplication.UI.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.nvvmapplication.R
import com.example.nvvmapplication.UI.models.Article
import com.example.nvvmapplication.databinding.ItemArticlePreviewBinding

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(private val binding: ItemArticlePreviewBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item:String) = with(binding){
            binding.ivArticleImage = Glide.with(this).load(ar)
        }

    }


    private val differCallback = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return  oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(ItemArticlePreviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
            val article = differ.currentList[position]
            holder.
        }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}
