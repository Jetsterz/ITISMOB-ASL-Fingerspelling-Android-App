package com.itismob.group8.aslfingerspellingapp.retrofit

import com.itismob.group8.aslfingerspellingapp.retrofit.interfaces.NameApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NameRetrofitHelper {

    val baseUrl = "https://websonic.co.uk/names/"
    private val retrofit by lazy {
        Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val nameInterface by lazy {
        retrofit.create(NameApi::class.java)
    }
}