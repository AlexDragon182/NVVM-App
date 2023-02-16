package com.example.nvvmapplication.UI.DataBase

import com.example.nvvmapplication.UI.models.Source
import androidx.room.TypeConverter


//source is a data type of the .json you need to tell room how to interpret the source file , and how to create a source
class Converters {
    @TypeConverter // this tells room this is a converter function
    fun fromSource(source: Source): String { //whenever we call source we just call the name so it returns a string
        return source.name?:""
    }

    @TypeConverter
    fun toSource(name:String): Source {// whenever we have a string , we converted to soure
        return Source(name,name)
    }
}