package br.ifsp.agendaroom.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import br.ifsp.agendaroom.R
import br.ifsp.agendaroom.data.Contato
import br.ifsp.agendaroom.data.ContatoDatabase
import br.ifsp.agendaroom.databinding.FragmentCadastroBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CadastroFragment : Fragment(){
    private var _binding: FragmentCadastroBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCadastroBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.cadastro_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_salvarContato -> {
                        val nome = binding.commonLayout.editTextNome.text.toString()
                        val fone = binding.commonLayout.editTextFone.text.toString()
                        val email = binding.commonLayout.editTextEmail.text.toString()

                        val c = Contato( 0,nome, fone, email)

                        val db = ContatoDatabase.getDatabase(requireActivity().applicationContext)

                        CoroutineScope(Dispatchers.IO).launch {
                            db.contatoDAO().inserirContato(c)
                        }

                        findNavController().popBackStack()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

}
