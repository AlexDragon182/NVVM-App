package com.example.nvvmapplication.UI.IUAI

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nvvmapplication.UI.Repository.NewsRepository
import com.example.nvvmapplication.UI.Util.Resource
import com.example.nvvmapplication.UI.models.Article
import com.example.nvvmapplication.UI.models.USANews
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(// we cannot use constructor parameters by default on view models , for doing that you need to create a view model provider factory , to define how view model should be created
    val newsRepository : NewsRepository //called a repository
) : ViewModel() { // inherits from view model

    //create a live data object
    val breakingNews:MutableLiveData<Resource<USANews>> = MutableLiveData() //Resouce is used as repo class to a generic type T
    var breakingNewsPage = 1// to manage pagination
    var breakingNewsResponse : USANews? = null

//live data objects for search news response
    val searchNews:MutableLiveData<Resource<USANews>> = MutableLiveData() //Resouce is used as repo class to a generic type T
    var searchNewsPage = 1// to manage pagination
    var searchNewsResponse : USANews? = null

    init {//init block of the used viewmodel. // uses internet permision in the manifest
        getBreakingNews("us")
    }

    //executes api from the repository
    // viewmodel scope is to start the coroutine in this function
    fun getBreakingNews(countryCode:String) = viewModelScope.launch { // this coroutine will stay alive as long as the view model stays alive
        breakingNews.postValue(Resource.Loading())//imit the loading state in the live data, because now we know we are about to make a call
        val response = newsRepository.getBreakingNews(countryCode,breakingNewsPage)//make the network response and when this finish the coroutine will continue with the next line
        breakingNews.postValue(handleBreakingNewsResponse(response))// handle response and pagination
    }

    fun searchNews(searchQuery : String)= viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response = newsRepository.searchNews(searchQuery,searchNewsPage)
        searchNews.postValue(handleSearchNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<USANews>) : Resource<USANews>{//import response from retrofir
        if(response.isSuccessful){// chech if it is succesfull
            response.body()?.let { resultResponse -> // not equal to null , lamda give the name
                breakingNewsPage ++
                if (breakingNewsResponse == null){
                    breakingNewsResponse = resultResponse
                }else{
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse?:resultResponse) // return resource success with resultResponse
            }
        }
        return Resource.Error(response.message())// if its not succesfull
    }

    private fun handleSearchNewsResponse(response: Response<USANews>) : Resource<USANews>{//import response from retrofir
        if(response.isSuccessful){// chech if it is succesfull
            response.body()?.let { resultResponse -> // not equal to null , lamda give the name
                searchNewsPage ++
                if (searchNewsResponse == null){
                    searchNewsResponse = resultResponse
                }else{
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse?:resultResponse) // return resource success with resultResponse
            }
        }
        return Resource.Error(response.message())// if its not succesfull
    }
    fun savedArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }
    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

}