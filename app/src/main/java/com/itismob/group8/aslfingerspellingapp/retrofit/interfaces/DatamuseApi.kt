package com.itismob.group8.aslfingerspellingapp.retrofit.interfaces

import com.itismob.group8.aslfingerspellingapp.dataclasses.WordsData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

// https://api.datamuse.com/words?rel_gen=food
interface DatamuseApi {
    @GET
     fun getWords(@Url relUrl: String ): Call<List<WordsData>>
     @GET("words")
     suspend fun getWordsOnly(@Query("rel_trg") cat: String) : List<WordsData>
}