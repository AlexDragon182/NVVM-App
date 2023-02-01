package com.example.nvvmapplication.UI.IUAI

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_ETHERNET
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities.*
import android.os.Build
import android.provider.ContactsContract.CommonDataKinds.Email.TYPE_MOBILE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nvvmapplication.UI.NewsApplication
import com.example.nvvmapplication.UI.Repository.NewsRepository
import com.example.nvvmapplication.UI.Util.Resource
import com.example.nvvmapplication.UI.models.Article
import com.example.nvvmapplication.UI.models.USANews
import kotlinx.coroutines.launch
import okhttp3.internal.applyConnectionSpec
import retrofit2.Response
import java.io.IOException

class NewsViewModel(// we cannot use constructor parameters by default on view models , for doing that you need to create a view model provider factory , to define how view model should be created
    val app: Application,
    val newsRepository: NewsRepository //called a repository
) : AndroidViewModel(app) { // inherits from view model

    //create a live data object
    val breakingNews: MutableLiveData<Resource<USANews>> =
        MutableLiveData() //Resouce is used as repo class to a generic type T
    var breakingNewsPage = 1// to manage pagination
    var breakingNewsResponse: USANews? = null

    //live data objects for search news response
    val searchNews: MutableLiveData<Resource<USANews>> =
        MutableLiveData() //Resouce is used as repo class to a generic type T
    var searchNewsPage = 1// to manage pagination
    var searchNewsResponse: USANews? = null

    init {//init block of the used viewmodel. // uses internet permision in the manifest
        getBreakingNews("us")
    }

    //executes api from the repository
    // viewmodel scope is to start the coroutine in this function
    fun getBreakingNews(countryCode: String) =
        viewModelScope.launch { // this coroutine will stay alive as long as the view model stays alive
            safeBreakingNewsCall(countryCode)
            //breakingNews.postValue(Resource.Loading())//imit the loading state in the live data, because now we know we are about to make a call
            //val response = newsRepository.getBreakingNews(countryCode,breakingNewsPage)//make the network response and when this finish the coroutine will continue with the next line
            //breakingNews.postValue(handleBreakingNewsResponse(response))// handle response and pagination
        }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        safeSearchNewsCall(searchQuery)
        //searchNews.postValue(Resource.Loading())
        //val response = newsRepository.searchNews(searchQuery,searchNewsPage)
        //searchNews.postValue(handleSearchNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<USANews>): Resource<USANews> {//import response from retrofir
        if (response.isSuccessful) {// chech if it is succesfull
            response.body()?.let { resultResponse -> // not equal to null , lamda give the name
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    newArticles?.let {
                        oldArticles?.addAll(it)
                    }
                }
                return Resource.Success(
                    breakingNewsResponse ?: resultResponse
                ) // return resource success with resultResponse
            }
        }
        return Resource.Error(response.message())// if its not succesfull
    }

    private fun handleSearchNewsResponse(response: Response<USANews>): Resource<USANews> {//import response from retrofir
        if (response.isSuccessful) {// chech if it is succesfull
            response.body()?.let { resultResponse -> // not equal to null , lamda give the name
                searchNewsPage++
                if (searchNewsResponse == null) {
                    searchNewsResponse = resultResponse
                } else {
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    newArticles?.let {
                        oldArticles?.addAll(it)
                    }
                }
                return Resource.Success(
                    searchNewsResponse ?: resultResponse
                ) // return resource success with resultResponse
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

    private suspend fun safeBreakingNewsCall(countryCode: String) {
        breakingNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepository.getBreakingNews(
                    countryCode,
                    breakingNewsPage
                )//make the network response and when this finish the coroutine will continue with the next line
                breakingNews.postValue(handleBreakingNewsResponse(response))
            } else {
                breakingNews.postValue(Resource.Error("No internet connection"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> breakingNews.postValue(Resource.Error("Network Failure"))
                else -> breakingNews.postValue(Resource.Error("Conversion Error"))
            }

        }
    }

    private suspend fun safeSearchNewsCall(searchQuery: String) {
        searchNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepository.searchNews(
                    searchQuery,
                    searchNewsPage
                )//make the network response and when this finish the coroutine will continue with the next line
                searchNews.postValue(handleSearchNewsResponse(response))
            } else {
                searchNews.postValue(Resource.Error("No internet connection"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchNews.postValue(Resource.Error("Network Failure"))
                else -> searchNews.postValue(Resource.Error("Conversion Error"))
            }

        }
    }


    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }

        }
        //connectivityManager.activeNetworkInfo.isConnected// use fro app below 23
        return false
    }

}