package br.com.seucaio.icarmanager.ui.update

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.widget.Toast
import br.com.seucaio.icarmanager.R
import br.com.seucaio.icarmanager.data.remote.ApiService
import br.com.seucaio.icarmanager.data.remote.model.Car
import br.com.seucaio.icarmanager.util.toEditable
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
                { response ->
                    getCarData(response)
                    btn_update.setOnClickListener {
                        confirmUpdate(response)
                        progress_car_update.visibility = View.VISIBLE
                    }

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

        subscription = service.updateCarById(car.id,car)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->

                    if (response.code() == 201) {
                        showMessage(response.body()!!.mensagem)
                    } else {
                        showMessage("Erro ao alterar o carro")
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

//        Snackbar.make(btn_update, "Carro atu", Snackbar.LENGTH_LONG)
//            .setAction("Action", null).show()

        /*
             fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        */

    }

    private fun showMessage(message: String) {

        progress_car_update.visibility = View.INVISIBLE

        Snackbar.make(btn_update, message, Snackbar.LENGTH_INDEFINITE)
            .setAction("Okay") {finish()}

            .show()



//        Snackbar.make(view, "", Snackbar.LENGTH_LONG)
//            .setAction("Action", null).show()
//
//
//        val snackbar = Snackbar.make(view, "Replace with your own action",
//            Snackbar.LENGTH_LONG).setAction("Action", null)
//        snackbar.setActionTextColor(Color.BLUE)
//        val snackbarView = snackbar.view
//        snackbarView.setBackgroundColor(Color.LTGRAY)
//        val textView =
//            snackbarView.findViewById(android.support.design.R.id.snackbar_text) as TextView
//        textView.setTextColor(Color.BLUE)
//        textView.textSize = 28f
//        snackbar.show()
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
