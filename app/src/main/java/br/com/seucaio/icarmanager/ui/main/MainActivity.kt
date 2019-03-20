package br.com.seucaio.icarmanager.ui.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import br.com.seucaio.icarmanager.R
import br.com.seucaio.icarmanager.data.remote.ApiService
import br.com.seucaio.icarmanager.data.remote.model.Car
import br.com.seucaio.icarmanager.ui.add.CarAddActivity
import br.com.seucaio.icarmanager.ui.list.CarListAdapter
import br.com.seucaio.icarmanager.ui.detail.CarDetailActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_car_update.*
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {
    val TAG = MainActivity::class.java.simpleName

    var subscription: Disposable? = null

    val service by lazy { ApiService.create() }

    lateinit var carListAdapter: RecyclerView.Adapter<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "--> onCreate")
        progress_car_list.visibility = View.VISIBLE

        swipe.setOnRefreshListener{ getCars() }
        getCars()
    }

    private fun initRecycler(carList: MutableList<Car>) {

        carListAdapter = CarListAdapter(carList) { car ->
            startActivity<CarDetailActivity>("id" to car.id)
        }

        rv_car_list.apply {
            adapter = carListAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)


            val itemTouchListener = object : ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    viewHolder2: RecyclerView.ViewHolder
                ) = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val adapter = (carListAdapter as CarListAdapter)
                    progress_car_list.visibility = View.VISIBLE

                    with(viewHolder.adapterPosition) {
                        adapter.removeItem(this)
                      removeCarById(adapter.getItem(this))
                    }
                }
            }

            val itemTouchHelper = ItemTouchHelper(itemTouchListener)
            itemTouchHelper.attachToRecyclerView(rv_car_list)
        }

        progress_car_list.visibility = View.INVISIBLE
    }


    private fun removeCarById(carSelected: Car) {

        subscription = service.deleteCarById(carSelected.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    if (response.code() == 200) {
                        showMessage(response.body()!!.mensagem)
                    } else {
                        showMessage("Erro ao remover o carro")
                    }

                    Log.d(TAG, carSelected.toString())
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
        progress_car_list.visibility = View.INVISIBLE

        Snackbar.make(rv_car_list, message, Snackbar.LENGTH_LONG)
            .setAction("Okay", null)
            .show()

    }

    private fun getCars() {
        subscription = service.getCars()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    initRecycler(response.toMutableList())
                    Log.d(TAG, response.toString())
                },
                { error ->
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, error.message, error)
                },
                { swipe.isRefreshing = false }
            )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
            R.id.action_add -> {
                startActivity<CarAddActivity>()
                true
            }
        else -> super.onOptionsItemSelected(item)
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
