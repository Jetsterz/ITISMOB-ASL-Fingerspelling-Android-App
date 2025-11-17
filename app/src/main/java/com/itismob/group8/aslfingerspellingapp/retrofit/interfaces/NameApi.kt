package com.itismob.group8.aslfingerspellingapp.retrofit.interfaces

import com.itismob.group8.aslfingerspellingapp.dataclasses.NamesData
import retrofit2.Call
import retrofit2.http.GET

interface NameApi {
    @GET("api.php?type=random&with_surname")
    fun getName(): Call<NamesData>
}