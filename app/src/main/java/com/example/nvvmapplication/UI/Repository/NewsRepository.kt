package com.example.nvvmapplication.UI.Repository

import com.example.nvvmapplication.UI.DataBase.ArticleDataBase
import com.example.nvvmapplication.UI.IUAI.RetrofitInstance

class NewsRepository(
    val db: ArticleDataBase // needs the actual database to acces the functiosn and API

) {// create network request and get the response of breaking news and display in recicle view

    // get breaking news from API
    suspend fun getBreakingNews(countryCode:String,pageNumber:Int) =
        RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)
}