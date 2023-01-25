package com.example.nvvmapplication.UI.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nvvmapplication.UI.models.Article
import com.example.nvvmapplication.databinding.ItemArticlePreviewBinding

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(val binding: ItemArticlePreviewBinding): RecyclerView.ViewHolder(binding.root){
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
            holder.binding.apply {
                Glide.with(this)
                    .load(article.urlToImage)
                    .into(ivArticleImage);

                tvSource.text = article.source?.name
                tvTitle.text = article.title
                tvDescription.text = article.description
                tvPublishedAt.text = article.publishedAt
                onItemClickListener?.let { it(article) }
            }
        }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private  var onItemClickListener : ((Article) -> Unit)? = null

    fun setOnItemClickListener (listener :(Article) -> Unit){
        onItemClickListener = listener
    }



}
