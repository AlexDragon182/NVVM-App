package com.example.nvvmapplication.UI

import com.example.nvvmapplication.UI.Util.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//retrofit dependencies where used to log responses of retrofit

class RetrofitInstance {

    companion object { // companion object so we do not create an instance of this class
        private val retrofit by lazy {// lazy means that we only initialize it once
            val logging = HttpLoggingInterceptor() // able to see which request are making and what responses are
            logging.setLevel(HttpLoggingInterceptor.Level.BODY) // see the body of the actual response
            val client = OkHttpClient.Builder()  //create a client
                .addInterceptor(logging) // add the interceptor and pass the logging
                .build() // call the build
            Retrofit.Builder()//use the client to pass it to a retrofit instance
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())//use to determine how the response should be interpreded
                .client(client)//client abobe
                .build()
        }
        val api by lazy { //get out api instance from retrofit builder ,This is the actual API object
            retrofit.create(NewsAPI::class.java)
        }
    }

}