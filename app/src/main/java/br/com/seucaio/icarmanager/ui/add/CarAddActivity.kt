package br.com.seucaio.icarmanager.ui.add

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.com.seucaio.icarmanager.R
import br.com.seucaio.icarmanager.data.remote.ApiService
import br.com.seucaio.icarmanager.ui.main.MainActivity
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_car_add.*

class CarAddActivity : AppCompatActivity() {

    val TAG = CarAddActivity::class.java.simpleName

    var subscription: Disposable? = null

    val service by lazy { ApiService.create() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_add)
        Log.d(TAG, "--> onCreate")
        supportActionBar?.title = "Adicionar Veículo"

        btn_add.setOnClickListener { addCar() }

    }

    private fun addCar() {

    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "--> onPause")
        subscription?.dispose()
    }
}
