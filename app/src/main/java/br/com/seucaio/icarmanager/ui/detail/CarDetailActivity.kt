package br.com.seucaio.icarmanager.ui.detail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import br.com.seucaio.icarmanager.Constants
import br.com.seucaio.icarmanager.R
import br.com.seucaio.icarmanager.data.remote.ApiService
import br.com.seucaio.icarmanager.data.remote.model.Proposta
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_car_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.adapter_car_list.view.*

class CarDetailActivity : AppCompatActivity() {

    val TAG = CarDetailActivity::class.java.simpleName

    var subscription: Disposable? = null

    val service by lazy { ApiService.create() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_detail)
        Log.d(TAG, "--> onCreate")
        progress_car_detail.visibility = View.VISIBLE

        val idCar = intent.getStringExtra("id")

        showCarDetail(idCar)
    }


    private fun initRecycler(proposalList: List<Proposta>) {
        rv_car_proposal.layoutManager = LinearLayoutManager(this)
        rv_car_proposal.adapter = CarProposalAdapter(proposalList) { proposal ->

            Toast.makeText(this,
                "Valor da proposta: ${proposal.valor}",
                Toast.LENGTH_SHORT).show()
        }

        progress_car_detail.visibility = View.INVISIBLE
    }

    private fun showCarDetail(id: String) {
        Toast.makeText(this, "$id do Carro", Toast.LENGTH_SHORT).show()

        subscription = service.getCarById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                        response ->

                    txt_brand_model.text = "${response.marca} ${response.modelo}"
                    txt_year_model.text = response.ano
                    txt_price_car.text = "R$ ${response.preco},00"
                    Glide.with(this).load("${Constants.BASE_URL_IMG}${response.foto}")
                        .into(img_car)

                    initRecycler(response.proposta)
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


    override fun onPause() {
        super.onPause()
        Log.d(TAG, "--> onPause")
        subscription?.dispose()
    }
}
