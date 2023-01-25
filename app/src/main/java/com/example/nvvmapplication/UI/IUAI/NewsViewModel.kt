package com.example.nvvmapplication.UI.IUAI

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nvvmapplication.UI.Repository.NewsRepository
import com.example.nvvmapplication.UI.Util.Resource
import com.example.nvvmapplication.UI.models.USANews
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(// we cannot use constructor parameters by default on view models , for doing that you need to create a view model provider factory , to define how view model should be created
    val newsRepository : NewsRepository //called a repository
) : ViewModel() { // inherits from view model

    //create a live data object
    val breakingNews:MutableLiveData<Resource<USANews>> = MutableLiveData()
    var breakingNewsPage = 1

    init {
        getBreakingNews("us")
    }

    //executes api from the repository
    fun getBreakingNews(countryCode:String) = viewModelScope.launch { // this coroutine will stay alive as long as the view model stays alive
        breakingNews.postValue(Resource.Loading())
        val response = newsRepository.getBreakingNews(countryCode,breakingNewsPage)
        breakingNews.postValue(handleBreakingNewsRespondse(response))
    }

    private fun handleBreakingNewsRespondse(response: Response<USANews>) : Resource<USANews>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}