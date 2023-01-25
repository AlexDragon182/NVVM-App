package com.example.nvvmapplication.UI.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nvvmapplication.UI.models.Article
import com.example.nvvmapplication.databinding.ItemArticlePreviewBinding

class NewsAdapter   : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {//inherit from recycler view passing News adapter

    inner class ArticleViewHolder(private val binding: ItemArticlePreviewBinding): RecyclerView.ViewHolder(binding.root){ //inner class for view-binding
    }
    //implementing diff util, calculates the differences between 2 lists and enables to only update those different items, happens in background
    private val differCallback = object : DiffUtil.ItemCallback<Article>(){// for create a call back , to compare the lists passing the items on our list
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {//return if articles are the same
            return  oldItem.url == newItem.url //comparing urls cause they are unique
        }
        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {// checks if contents are same
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this,differCallback) // the tool the will take out two lists and compares them and calculate differences, parameter adapter and call back


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(ItemArticlePreviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }
    override fun getItemCount(): Int {//for returning the amount of items we have in the recycler view
        return differ.currentList.size
    }
    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {// set views accordingly
         val article = differ.currentList[position]// get current article
            holder.itemView.apply {
                Glide.with(this).load(article.urlToImage).into(ivArticleImage)// so we can reference our views

                tvSource.text = article.source?.name
                tvTitle.text = article.title
                tvDescription.text = article.description
                tvPublishedAt.text = article.publishedAt
                onItemClickListener?.let { it(article) }  // it refers to the on Item Lambda function which takes Article as a parameter
            }
        }
    private  var onItemClickListener : ((Article) -> Unit)? = null // for managing clicks outside item view

    fun setOnItemClickListener (listener :(Article) -> Unit){
        onItemClickListener = listener
    }



}
