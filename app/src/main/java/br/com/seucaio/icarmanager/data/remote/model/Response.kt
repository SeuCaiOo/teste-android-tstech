package br.com.seucaio.icarmanager.data.remote.model

import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("mensagem")
    val mensagem: String
)