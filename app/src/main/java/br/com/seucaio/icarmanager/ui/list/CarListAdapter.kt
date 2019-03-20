package br.com.seucaio.icarmanager.ui.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.seucaio.icarmanager.Constants
import br.com.seucaio.icarmanager.R
import br.com.seucaio.icarmanager.data.remote.model.Car
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.adapter_car_list.view.*

class CarListAdapter(
    private val cars: MutableList<Car>,
    private val listener: (Car) -> Unit
): RecyclerView.Adapter<CarListAdapter.CarListViewHolder>() {

    private var removedPosition: Int = 0
    private lateinit var removedItem: Car

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_car_list, parent, false)
        return CarListViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarListViewHolder, position: Int) {
        holder.bind(cars[position], listener)
    }

    override fun getItemCount() = cars.size

    fun getItem(position: Int) = removedItem

    fun removeItem(position: Int) {
        removedItem = cars[position]
        removedPosition = position

        cars.removeAt(position)
        notifyItemRemoved(position)

    }

    class CarListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(car: Car, listener: (Car) -> Unit) =
                with(itemView) {
                    txt_brand_model.text = "${car.marca} ${car.modelo}"
                    txt_year_model.text = car.ano
                    txt_price_car.text = "R$ ${car.preco},00"
                    Glide.with(context).load("${Constants.BASE_URL_IMG}${car.foto}")
                        .into(img_car)
                    setOnClickListener { listener(car) }
                }
    }
}