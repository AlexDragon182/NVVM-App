package com.example.nvvmapplication.UI.DataBase

import Article
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao //room needs to know this is the interface that defines the function for us
interface Article_DAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)// insert or update an article, on clonflict if we want to insert an article that already exists replace it
    suspend fun upsert(article: Article):Long// update or insert , parameter the article that we want to insert in our database, returns a long which is the ID that was inserted

    @Query("SELECT * FROM  articles") //to return all available articles in our database
    fun getAllArticles(): LiveData<List<Article>>// live data don't work with suspend functions
    //whenever an article inside of that list changes Live Data will notify all of its observers

    @Delete
    suspend fun deleteArticle(article: Article)
}