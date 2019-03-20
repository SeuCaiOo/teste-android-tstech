package br.com.seucaio.icarmanager.data.remote

import br.com.seucaio.icarmanager.Constants
import br.com.seucaio.icarmanager.data.remote.model.Car
import br.com.seucaio.icarmanager.data.remote.model.NewCar
import br.com.seucaio.icarmanager.data.remote.model.Response
import br.com.seucaio.icarmanager.data.remote.model.ResponseError
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {


    @GET("carros")
    fun getCars(): Observable<List<Car>>

    @GET("carros/{id}")
    fun getCarById(
        @Path("id") id: String
    ) : Observable<Car>

    @POST("carros")
    fun postCar(
        @Body newCar: NewCar
    ) : Observable<retrofit2.Response<Response>>

    @PUT("carros/{id}")
    fun updateCarById(
        @Path("id") id: String,
        @Body car: Car
    ) : Observable<retrofit2.Response<Response>>

    @DELETE("carros/{id}")
    fun deleteCarById(
        @Path("id") id: String
    ) : Observable<retrofit2.Response<Response>>


   companion object {

       fun create(): ApiService {

           val retrofit = Retrofit.Builder()
               .addConverterFactory(GsonConverterFactory.create())
               .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
               .baseUrl(Constants.BASE_URL)
               .build()

           return retrofit.create(ApiService::class.java)
       }
   }
}