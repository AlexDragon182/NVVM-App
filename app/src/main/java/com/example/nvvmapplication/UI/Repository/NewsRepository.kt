package com.example.nvvmapplication.UI.Repository

import com.example.nvvmapplication.UI.DataBase.ArticleDataBase
import com.example.nvvmapplication.UI.IUAI.RetrofitInstance

class NewsRepository(
    val db: ArticleDataBase // needs the actual database to acces the functiosn and API
//get the data from our data base and our remote data source to the retrofit from the APR
) {// create network request and get the response of breaking news and display in recicle view

    // get breaking news from API
    suspend fun getBreakingNews(countryCode:String,pageNumber:Int) =
        RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)

    suspend fun searchNews(searchQuery: String,pageNumber: Int) = //function for searching for news
        RetrofitInstance.api.searchForNews(searchQuery,pageNumber)
}

