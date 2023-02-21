package com.example.nvvmapplication.UI.IUAI

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nvvmapplication.UI.Repository.NewsRepository

// this defines how our own view model should be created
// this is for using constructive parameters in view model, cause they cannot be used by default

class NewsViewModelProviderFactory(
    val app: Application,
    val newsRepository: NewsRepository
): ViewModelProvider.Factory {// inherit from viewmodel provider.factory

    override fun <T : ViewModel> create(modelClass: Class<T>): T {//return a new instance of a newsViewModel with the news repository returning it as T
        return NewsViewModel(app,newsRepository) as T
    }

}