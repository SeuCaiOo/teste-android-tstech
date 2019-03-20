package br.com.seucaio.icarmanager.data.remote.model

import com.google.gson.annotations.SerializedName

data class NewCar(
    @SerializedName("ano")
    val ano: String,
    @SerializedName("foto")
    val foto: String?,
    @SerializedName("marca")
    val marca: String,
    @SerializedName("modelo")
    val modelo: String,
    @SerializedName("preco")
    val preco: String
)