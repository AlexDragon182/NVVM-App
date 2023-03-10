package com.example.nvvmapplication.UI.models

import androidx.room.Entity
import androidx.room.PrimaryKey


//this 3 files in the folder where created from an extension callde .json to kotlin


@Entity(//to actually be able to save an article in our Database
//tell android studio that this class is a table in our database (com.example.nvvmapplication.UI.models.Article table in our database, with several columns)

tableName = "articles" // set name (usually this is what you call)

)
data class Article(// this is the whole table vals are columns
    @PrimaryKey(autoGenerate = true)// this will tell room to automatically generate keys
    var id: Int? = null, //primary key is used to give our article class an unique identifier to differentiate with different articles
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
):java.io.Serializable
// Next Create com.example.nvvmapplication.UI.models.Article DAO Data Acces Object