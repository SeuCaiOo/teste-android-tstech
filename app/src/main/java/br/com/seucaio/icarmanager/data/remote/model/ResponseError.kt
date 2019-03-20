package br.com.seucaio.icarmanager.data.remote.model

import com.google.gson.annotations.SerializedName

data class ResponseError(
    @SerializedName("message")
    val message: String,
    @SerializedName("name")
    val name: String
)