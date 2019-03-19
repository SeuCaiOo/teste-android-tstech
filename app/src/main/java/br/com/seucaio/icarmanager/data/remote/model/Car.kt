package br.com.seucaio.icarmanager.data.remote.model

import com.google.gson.annotations.SerializedName

data class Car(
    @SerializedName("ano")
    val ano: String,
    @SerializedName("foto")
    val foto: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("marca")
    val marca: String,
    @SerializedName("modelo")
    val modelo: String,
    @SerializedName("preco")
    val preco: String,
    @SerializedName("propostas")
    val proposta: List<Proposta>
)