package br.ifsp.agendaroom.ui


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
import br.ifsp.agendaroom.R
import br.ifsp.agendaroom.adapter.ContatoAdapter
import br.ifsp.agendaroom.data.Contato
import br.ifsp.agendaroom.data.ContatoDatabase
import br.ifsp.agendaroom.databinding.FragmentListaContatosBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListaContatosFragment : Fragment(){

    private var _binding: FragmentListaContatosBinding? = null

    private val binding get() = _binding!!


    lateinit var contatoAdapter: ContatoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListaContatosBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.fab.setOnClickListener { findNavController().navigate(R.id.action_listaContatosFragment_to_cadastroFragment) }

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
                        contatoAdapter.filter.filter(p0)
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

        val db = ContatoDatabase.getDatabase(requireActivity().applicationContext)
        var contatosLista : ArrayList<Contato>

        val recyclerView = binding.recyclerview

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        CoroutineScope(Dispatchers.IO).launch {
            contatosLista = db.contatoDAO().listarContatos() as ArrayList<Contato>
            contatoAdapter = ContatoAdapter(contatosLista)


            withContext(Dispatchers.Main) {
                recyclerView.adapter = contatoAdapter

                val listener = object : ContatoAdapter.ContatoListener {
                    override fun onItemClick(pos: Int) {
                        val c = contatoAdapter.contatosListaFilterable[pos]

                        val bundle = Bundle()
                        bundle.putSerializable("contato", c)

                        findNavController().navigate(
                            R.id.action_listaContatosFragment_to_detalheFragment,
                            bundle
                        )

                    }
                }
                contatoAdapter.setClickListener(listener)


            }
        }

    }

}