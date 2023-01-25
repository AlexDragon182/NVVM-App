package com.example.nvvmapplication.UI.IUAI

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nvvmapplication.UI.Repository.NewsRepository

class NewsViewModelProviderFactory(
    val newsRepository: NewsRepository
): ViewModelProvider.Factory {// inherit from viewmodel provider.factory

    override fun <T : ViewModel> create(modelClass: Class<T>): T {//return a new instance of a newsViewModel with the news repository returning it as T
        return NewsViewModel(newsRepository) as T
    }

}