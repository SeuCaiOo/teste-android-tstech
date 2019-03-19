package br.com.seucaio.icarmanager.data.remote

import br.com.seucaio.icarmanager.Constants
import br.com.seucaio.icarmanager.data.remote.model.Car
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {


    @GET("carros")
    fun getCars(): Observable<List<Car>>

    @GET("carros/{id}")
    fun getCarById(
        @Path("id") id: String
    ) : Observable<Car>



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