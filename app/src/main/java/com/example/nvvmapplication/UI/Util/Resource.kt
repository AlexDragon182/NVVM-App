package com.example.nvvmapplication.UI.Util

//recomended from google to wrap around the network responses
//sealed class are able to define what load inherits from this class

sealed class Resource<T> ( //usefull to diferentieate between succesfull and failed responses, generic type T
    val data : T? = null,
val message : String? = null
        ){
    // only this 3 classes are allowed to inherit from resource

    //Type t, has a data T, and inherits from Resource class
    class  Success<T> (data: T) : Resource<T> (data) //not nullabe because if it is a succesed response it  is something in it
    class Error<T>(message: String, data: T? = null) : Resource<T>(data,message)
    class Loading<T>: Resource<T>()
}