package com.example.nvvmapplication.UI.DataBase

import com.example.nvvmapplication.UI.models.Source
import androidx.room.TypeConverter


//source is a data type of the .json you need to tell room how to interpret the source file , and how to create a source
class Converters {
    @TypeConverter // this tells room this is a converter function
    fun fromSource(source: Source): String {
        return source.name?:""
    }

    @TypeConverter
    fun toSource(name:String): Source {
        return Source(name,name)
    }
}