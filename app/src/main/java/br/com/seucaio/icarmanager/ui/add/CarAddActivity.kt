package br.com.seucaio.icarmanager.ui.add

import android.app.ProgressDialog.show
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.widget.Toast
import br.com.seucaio.icarmanager.R
import br.com.seucaio.icarmanager.data.remote.ApiService
import br.com.seucaio.icarmanager.data.remote.model.Car
import br.com.seucaio.icarmanager.data.remote.model.NewCar
import br.com.seucaio.icarmanager.ui.main.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_car_add.*
import org.jetbrains.anko.startActivity

class CarAddActivity : AppCompatActivity() {

    val TAG = CarAddActivity::class.java.simpleName

    var subscription: Disposable? = null

    val service by lazy { ApiService.create() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_add)
        Log.d(TAG, "--> onCreate")
        supportActionBar?.title = "Adicionar Veículo"

        progress_car_add.visibility = View.INVISIBLE

        btn_add.isEnabled = false

        btn_add.setOnClickListener {
            addCar()
            progress_car_add.visibility = View.VISIBLE
        }

    }

    private fun addCar() {
        val brand = edt_brand.text.toString()
        val model = edt_model.text.toString()
        val year = edt_year.text.toString()
        val price = edt_year.text.toString()
        val photo = edt_photo.text.toString()


        val newCar = NewCar(year,photo, brand, model, price)

        confirmeAddCar(newCar)

    }

    private fun confirmeAddCar(car: NewCar) {

        subscription = service.postCar(car)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->

                    if (response.code() == 201) {
                        showMessage(response.body()!!.mensagem)
                    } else {
                        showMessage("Erro ao adicionar novo carro")
                    }


                    Log.d(TAG, car.toString())
                    Log.d(TAG, response.toString())
                    Log.d(TAG, response.message())
                    Log.d(TAG, response.isSuccessful.toString())
                    Log.d(TAG, response.body().toString())
                    Log.d(TAG, response.code().toString())
                    Log.d(TAG, response.errorBody().toString())
                    Log.d(TAG, response.headers().toString())
                    Log.d(TAG, response.raw().toString())

                },
                { error ->
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, error.message, error)
                }
            )

    }

    private fun showMessage(message: String) {

        progress_car_add.visibility = View.INVISIBLE

        Snackbar.make(btn_add, message, Snackbar.LENGTH_INDEFINITE)
            .setAction("Okay") {finish()}

            .show()

    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "--> onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "--> onResume")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "--> onRestart")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "--> onStop")
        subscription?.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "--> onDestroy")
    }
}
