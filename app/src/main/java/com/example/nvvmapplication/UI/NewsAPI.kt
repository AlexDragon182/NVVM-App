package com.example.nvvmapplication.UI

import USANews
import com.example.nvvmapplication.UI.Util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//singe request that can be executed from code

interface NewsAPI {//get all the breaking news from the API
    @GET("v2/top-headlines")//specify after the base URL
    suspend fun getBreakingNews( //suspend function is to make it able to enter a coroutine
        @Query("country")//add the name of the parameter ,finded in documentation of API
        countryCode : String = "us",//
        @Query("page")//for pagination
        pageNumber: Int = 1,
        @Query("apiKey")// for knowing who does the request
        apiKey: String = Constants.API_KEY
    ): Response<USANews> //response object that was generated from json to kotlin class

    @GET("v2/everything")
    suspend fun searchForNews( //suspend function is to make it able to enter a coroutine
        @Query("q")
        searchQuery : String,
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = Constants.API_KEY
    ): Response<USANews> //response object that was generated from json to kotlin class
}