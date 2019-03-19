package br.com.seucaio.icarmanager.ui.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import br.com.seucaio.icarmanager.R
import br.com.seucaio.icarmanager.data.remote.ApiService
import br.com.seucaio.icarmanager.data.remote.model.Car
import br.com.seucaio.icarmanager.ui.list.CarListAdapter
import br.com.seucaio.icarmanager.ui.detail.CarDetailActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {
    val TAG = MainActivity::class.java.simpleName

    var subscription: Disposable? = null

    val service by lazy { ApiService.create() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "--> onCreate")
        progress_car_list.visibility = View.VISIBLE

        getCars()
    }

    private fun initRecycler(carList: List<Car>) {
        rv_car_list.layoutManager = LinearLayoutManager(this)
        rv_car_list.adapter = CarListAdapter(carList) { car ->
            startActivity<CarDetailActivity>("id" to car.id)
        }

        progress_car_list.visibility = View.INVISIBLE
    }


    private fun getCars() {
        subscription = service.getCars()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    response ->

                    initRecycler(response)

                    Log.d(TAG, response.toString())
                },
                {
                    error ->
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, error.message, error)
                },
                {}
            )
    }


    override fun onPause() {
        super.onPause()
        Log.d(TAG, "--> onPause")
        subscription?.dispose()
    }
}
