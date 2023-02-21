package com.example.nvvmapplication.UI.Repository

import com.example.nvvmapplication.UI.DataBase.ArticleDataBase
import com.example.nvvmapplication.UI.IUAI.RetrofitInstance
import com.example.nvvmapplication.UI.models.Article

//this is the repository created once room and retrofit are both set up

class NewsRepository(
    val db: ArticleDataBase // needs the actual database to acces the functiosn and API
//get the data from our data base and our remote data source to the retrofit from the APR
) {// create network request and get the response of breaking news and display in recicle view

    //purpose of repository is to get data from database and our remote
    //data source from retrofit from the API

    // get breaking news from API function directly querys the API from breaking news
    suspend fun getBreakingNews(countryCode:String,pageNumber:Int) =
        RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)

    suspend fun searchNews(searchQuery: String,pageNumber: Int) = //function for searching for news
        RetrofitInstance.api.searchForNews(searchQuery,pageNumber)

    suspend fun upsert(article : Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}

