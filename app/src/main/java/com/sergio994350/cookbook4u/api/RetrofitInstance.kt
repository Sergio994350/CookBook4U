package com.sergio994350.cookbook4u.api

import com.sergio994350.cookbook4u.util.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {
        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val rfClient = OkHttpClient.Builder().addInterceptor(logging).build()
            Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).client(rfClient).build()
        }
        val api: FoodApi by lazy {
            retrofit.create(FoodApi::class.java)
        }
    }
}
