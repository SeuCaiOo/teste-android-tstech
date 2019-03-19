package br.com.seucaio.icarmanager.ui.detail

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.seucaio.icarmanager.R
import br.com.seucaio.icarmanager.data.remote.model.Proposta
import kotlinx.android.synthetic.main.adapter_car_proposal.view.*

class CarProposalAdapter(
    private val proposals: List<Proposta>,
    private val listener: (Proposta) -> Unit
): RecyclerView.Adapter<CarProposalAdapter.CarProposalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarProposalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_car_proposal, parent, false)
        return CarProposalViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarProposalViewHolder, position: Int) {
        holder.bind(proposals[position], listener)
    }

    override fun getItemCount() = proposals.size

    class CarProposalViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(proposal: Proposta, listener: (Proposta) -> Unit) =
                with(itemView) {
                    txt_proposal_date.text = proposal.dataProposta
                    txt_proposal_value.text = "R$ ${proposal.valor}"
                    txt_proposal_author.text = proposal.nomeCliente
                    setOnClickListener { listener(proposal) }
                }
    }

}