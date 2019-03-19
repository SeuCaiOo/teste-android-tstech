package br.com.seucaio.icarmanager.ui.update

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import br.com.seucaio.icarmanager.Constants
import br.com.seucaio.icarmanager.R
import br.com.seucaio.icarmanager.data.remote.ApiService
import br.com.seucaio.icarmanager.data.remote.model.Car
import br.com.seucaio.icarmanager.data.remote.model.Proposta
import br.com.seucaio.icarmanager.ui.main.MainActivity
import br.com.seucaio.icarmanager.util.toEditable
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_car_update.*

class CarUpdateActivity : AppCompatActivity() {

    val TAG = CarUpdateActivity::class.java.simpleName

    var subscription: Disposable? = null

    val service by lazy { ApiService.create() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_update)
        Log.d(TAG, "--> onCreate")
        supportActionBar?.title = "Alterar Veículo"

        progress_car_update.visibility = View.VISIBLE

        val idCar = intent.getStringExtra("id")

        updateCar(idCar)



    }

    private fun updateCar(id: String) {



        subscription = service.getCarById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                        response ->

                    getCarData(response)
                    btn_update.setOnClickListener { confirmUpdate(response) }

                    Log.d(TAG, response.toString())
                },
                {
                        error ->
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, error.message, error)
                },
                {

                }
            )
    }

    private fun getCarData(car: Car) {
        edt_brand.text = car.marca.toEditable()
        edt_model.text = car.modelo.toEditable()
        edt_year.text = car.ano.toEditable()
        edt_price.text = car.preco.toEditable()
        edt_photo.text = car.foto.toEditable()
        progress_car_update.visibility = View.INVISIBLE
    }

    private fun confirmUpdate(car: Car) {

    }


    override fun onPause() {
        super.onPause()
        Log.d(TAG, "--> onPause")
        subscription?.dispose()
    }
}
