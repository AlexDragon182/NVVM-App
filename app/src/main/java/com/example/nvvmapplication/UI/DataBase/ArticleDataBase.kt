package com.example.nvvmapplication.UI.DataBase

import com.example.nvvmapplication.UI.models.Article
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

//Actual Database class for room
@Database(// this tells room this is the data base class
entities = [Article::class],//first parameter, properties
version = 1 // used to update our database

)

@TypeConverters(Converters::class) //for using the converters

abstract class ArticleDataBase: RoomDatabase() {// database classes always need to be abstract, inherit from roomDatabase

    abstract fun getArticleDao () : ArticleDAO // the implementation of this will be made by room

    companion object { // this is to be able to create the actual database
        @Volatile // other threats can inmediatley see when a thread changes this instance
        private var instance : ArticleDataBase? = null
        private val LOCK = Any() //sincronize setting that instance, there is only a single instance of our database at once

        operator fun invoke(context: Context) = instance?: synchronized(LOCK){// this is called whenever we create an instance of our database
            // takes the context instance and if it is null you wan to start synchronizing , with the LOCK so it can't be accesed by other threads

            instance?: createDatabase(context).also  { instance = it}//calls the create Database with contexts and sets current instance to it (ArticleDataBase)

        }
        private fun createDatabase(context: Context) =//takes a context and set it to
            Room.databaseBuilder(// call the builder
                context.applicationContext,// pass the application context
                ArticleDataBase::class.java,"article_db.db" //refer to our database class and choose a name

        ).build()// call build to actually create our database
    }

}