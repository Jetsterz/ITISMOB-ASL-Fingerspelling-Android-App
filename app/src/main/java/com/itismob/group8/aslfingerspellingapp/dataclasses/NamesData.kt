package com.itismob.group8.aslfingerspellingapp.dataclasses

import com.google.gson.annotations.SerializedName

data class NamesData(
    @SerializedName("name") val name: String,
    @SerializedName("status") val status: String,
)
