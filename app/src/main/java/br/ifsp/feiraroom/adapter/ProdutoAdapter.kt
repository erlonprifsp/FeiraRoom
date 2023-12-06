package br.ifsp.feiraroom.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import br.ifsp.feiraroom.data.Produto
import br.ifsp.feiraroom.databinding.ContatoCelulaBinding

class ProdutoAdapter(val produtosLista:ArrayList<Produto>): RecyclerView.Adapter<ProdutoAdapter.ContatoViewHolder>(),
    Filterable {

    var listener: ProdutoListener?=null

    var produtosListaFilterable = ArrayList<Produto>()

    private lateinit var binding: ContatoCelulaBinding

    init {
        this.produtosListaFilterable = produtosLista
    }

    fun setClickListener(listener: ProdutoListener)
    {
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContatoViewHolder {

        binding = ContatoCelulaBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return  ContatoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContatoViewHolder, position: Int) {
        holder.nomeVH.text = produtosListaFilterable[position].nome
        holder.foneVH.text = produtosListaFilterable[position].fone
    }

    override fun getItemCount(): Int {
        return produtosListaFilterable.size
    }

    inner class ContatoViewHolder(view: ContatoCelulaBinding): RecyclerView.ViewHolder(view.root)
    {
        val nomeVH = view.nome
        val foneVH = view.fone

        init {
            view.root.setOnClickListener {
                listener?.onItemClick(adapterPosition)
            }
        }

    }

    interface ProdutoListener
    {
        fun onItemClick(pos: Int)
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(p0: CharSequence?): FilterResults {
                if (p0.toString().isEmpty())
                    produtosListaFilterable = produtosLista
                else
                {
                    val resultList = ArrayList<Produto>()
                    for (row in produtosLista)
                        if (row.nome.lowercase().contains(p0.toString().lowercase()))
                            resultList.add(row)
                    produtosListaFilterable = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = produtosListaFilterable
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                produtosListaFilterable = p1?.values as ArrayList<Produto>
                notifyDataSetChanged()
            }

        }
    }

}