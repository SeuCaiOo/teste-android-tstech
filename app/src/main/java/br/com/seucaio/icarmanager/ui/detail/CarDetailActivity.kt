package br.com.seucaio.icarmanager.ui.detail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast
import br.com.seucaio.icarmanager.Constants
import br.com.seucaio.icarmanager.R
import br.com.seucaio.icarmanager.data.remote.ApiService
import br.com.seucaio.icarmanager.data.remote.model.Proposta
import br.com.seucaio.icarmanager.ui.update.CarUpdateActivity
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_car_detail.*
import org.jetbrains.anko.startActivity

class CarDetailActivity : AppCompatActivity() {

    val TAG = CarDetailActivity::class.java.simpleName

    private val KEY_ID = "id_car"

    var subscription: Disposable? = null

    val service by lazy { ApiService.create() }

    lateinit var proposalAdapter: RecyclerView.Adapter<*>

    var idCarDetail: String = ""

    private var currentId = Constants.ID_CAR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_detail)
        Log.d(TAG, "--> onCreate")
        progress_car_detail.visibility = View.VISIBLE
        txt_proposal_not_found.visibility = View.INVISIBLE

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val idCar = intent.getStringExtra("id")

        if (idCar != null) {
           idCarDetail = idCar
        } else {
            idCarDetail = currentId
        }

        setIdCar(idCarDetail)

        btn_update.setOnClickListener { updateCar(idCarDetail) }
        showCarDetail(idCarDetail)
    }

    fun setIdCar(id: String) { Constants.ID_CAR = id }

    private fun updateCar(id: String) {
        startActivity<CarUpdateActivity>("id" to id)
    }

    private fun initRecycler(proposalList: List<Proposta>) {
        proposalAdapter = CarProposalAdapter(proposalList) { proposal ->
            Toast.makeText(this,
                "Valor da proposta: ${proposal.valor}",
                Toast.LENGTH_SHORT).show()
        }

        rv_car_proposal.apply {
            adapter = proposalAdapter
            layoutManager = LinearLayoutManager(this@CarDetailActivity)

        }


        progress_car_detail.visibility = View.INVISIBLE
    }

    private fun showCarDetail(id: String) {
        subscription = service.getCarById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->

                    txt_brand_model.text = "${response.marca} ${response.modelo}"
                    txt_year_model.text = response.ano
                    txt_price_car.text = "R$ ${response.preco},00"
                    Glide.with(this).load("${Constants.BASE_URL_IMG}${response.foto}")
                        .into(img_car)

                    if (response.proposta.isNotEmpty()) {
                        txt_proposal_not_found.visibility = View.INVISIBLE
                        initRecycler(response.proposta)
                    } else {
                        txt_proposal_not_found.visibility = View.VISIBLE
                        progress_car_detail.visibility = View.INVISIBLE
                    }

                    Log.d(TAG, response.toString())
                },
                { error ->
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, error.message, error)
                }
            )
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
