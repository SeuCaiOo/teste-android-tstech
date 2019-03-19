package br.com.seucaio.icarmanager.data.remote.model

import com.google.gson.annotations.SerializedName

data class Proposta(
    @SerializedName("data")
    val dataProposta: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("nomeCliente")
    val nomeCliente: String,
    @SerializedName("valor")
    val valor: String
)