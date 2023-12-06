package br.ifsp.feiraroom.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.ifsp.feiraroom.R
import br.ifsp.feiraroom.adapter.ProdutoAdapter
import br.ifsp.feiraroom.data.Produto
import br.ifsp.feiraroom.data.ProdutoDatabase
import br.ifsp.feiraroom.databinding.FragmentListaProdutosBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListaProdutosFragment : Fragment(){

    private var _binding: FragmentListaProdutosBinding? = null

    private val binding get() = _binding!!


    lateinit var produtoAdapter: ProdutoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListaProdutosBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.fab.setOnClickListener { findNavController().navigate(R.id.action_listaProdutosFragment_to_cadastroFragment) }

        return root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.main_menu, menu)

                val searchView = menu.findItem(R.id.action_search).actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(p0: String?): Boolean {
                        TODO("Not yet implemented")
                    }

                    override fun onQueryTextChange(p0: String?): Boolean {
                        produtoAdapter.filter.filter(p0)
                        return true
                    }

                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                TODO("Not yet implemented")
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }


    override fun onResume() {
        super.onResume()
        updateUI()
    }


    private fun updateUI()
    {

        val db = ProdutoDatabase.getDatabase(requireActivity().applicationContext)
        var produtosLista : ArrayList<Produto>

        val recyclerView = binding.recyclerview

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        CoroutineScope(Dispatchers.IO).launch {
            produtosLista = db.produtoDAO().listarProdutos() as ArrayList<Produto>
            produtoAdapter = ProdutoAdapter(produtosLista)


            withContext(Dispatchers.Main) {
                recyclerView.adapter = produtoAdapter

                val listener = object : ProdutoAdapter.ProdutoListener {
                    override fun onItemClick(pos: Int) {
                        val c = produtoAdapter.produtosListaFilterable[pos]

                        val bundle = Bundle()
                        bundle.putSerializable("produto", c)

                        findNavController().navigate(


                            R.id.action_listaProdutosFragment_to_detalheFragment,
                            bundle
                        )

                    }
                }
                produtoAdapter.setClickListener(listener)


            }
        }

    }

}