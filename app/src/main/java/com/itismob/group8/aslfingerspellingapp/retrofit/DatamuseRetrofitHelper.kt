package com.itismob.group8.aslfingerspellingapp.retrofit

import com.itismob.group8.aslfingerspellingapp.retrofit.interfaces.DatamuseApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DatamuseRetrofitHelper {
    val baseUrl = "https://api.datamuse.com/"

    private val retrofit by lazy {
        Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val datamuseInterface by lazy{
        retrofit.create(DatamuseApi::class.java)
    }


}