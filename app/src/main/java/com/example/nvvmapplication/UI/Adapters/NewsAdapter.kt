package com.example.nvvmapplication.UI.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.nvvmapplication.UI.models.Article
import com.example.nvvmapplication.databinding.ItemArticlePreviewBinding

class NewsAdapter(
    private val context: Context
) : ListAdapter<Article, NewsAdapter.ArticleViewHolder>(ArticleDiffUtil) {//inherit from recycler view passing News adapter

    private var onItemClickListener: ((Article) -> Unit)? = null // for managing clicks outside item view

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }

    inner class ArticleViewHolder(val binding: ItemArticlePreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Article) = with(binding) {
            root.setOnClickListener(){
                onItemClickListener?.invoke(item)
            }
            Glide.with(context).load(item.urlToImage).into(ivArticleImage)
            tvSource.text = item.source?.name
            tvTitle.text = item.title
            tvDescription.text = item.description
            tvPublishedAt.text = item.publishedAt
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        //val view = LayoutInflater.from(context).inflate(R.layout.item_article_preview,parent,false)
        //return ArticleViewHolder(view)
        return ArticleViewHolder(
            ItemArticlePreviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ArticleViewHolder,
        position: Int
    ) {// set views accordingly
        holder.bind(getItem(position))
    }

}

object ArticleDiffUtil : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.id == newItem.id
    }

}
