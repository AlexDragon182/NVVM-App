package com.example.nvvmapplication.UI.IUAI

import com.example.nvvmapplication.UI.Util.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//retrofit dependencies where used to log responses of retrofit
// retrofit singleton class that enables us to make request from everywhere in our code.
class RetrofitInstance {

    companion object { // companion object so we do not create an instance of this class
        private val retrofit by lazy {// lazy means that we only initialize it once
            //dependencies where included for logging responses of retrofit (useful for debugging)
            //Key
            val logging = HttpLoggingInterceptor() // able to see which request are making and what responses are
            logging.setLevel(HttpLoggingInterceptor.Level.BODY) //set logging level see the body of the actual response
            //Person who uses the key
            val client = OkHttpClient.Builder()  //use the interceptor to create a client
                .addInterceptor(logging) // add the interceptor and pass the logging
                .build() // call the build
            //Door
            Retrofit.Builder()//use the client to pass it to a retrofit instance
                .baseUrl(BASE_URL)//set base URL
                .addConverterFactory(GsonConverterFactory.create())//use to determine how the response should be interpreded
                // and converted to a kotin object ~ GsonConverterFactory is the google implementation of Gson converting
                .client(client)//client abobe
                .build()
        }
        val api by lazy { //get out api instance from retrofit builder ,This is the actual API object
            retrofit.create(NewsAPI::class.java)// return a retrofit with the news api
             // this is the actual object that we will call to make our network request
        }
    }

}