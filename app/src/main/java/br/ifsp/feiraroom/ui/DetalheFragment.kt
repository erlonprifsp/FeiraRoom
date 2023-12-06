package br.ifsp.feiraroom.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import br.ifsp.feiraroom.R
import br.ifsp.feiraroom.data.Produto
import br.ifsp.feiraroom.data.ProdutoDatabase
import br.ifsp.feiraroom.databinding.FragmentDetalheBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DetalheFragment : Fragment() {
    private var _binding: FragmentDetalheBinding? = null

    private val binding get() = _binding!!

    lateinit var produto: Produto


    lateinit  var nome: EditText
    lateinit var fone: EditText
    lateinit var email: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetalheBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        produto = requireArguments().getSerializable("produto",Produto::class.java) as Produto

        nome = binding.commonLayout.editTextNome
        fone = binding.commonLayout.editTextFone
        email = binding.commonLayout.editTextEmail

        nome.setText(produto.nome)
        fone.setText(produto.fone)
        email.setText(produto.email)

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.detalhe_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_alterarProduto -> {
                        val db = ProdutoDatabase.getDatabase(requireActivity().applicationContext)

                        val produtoUpdate=Produto(produto.id,nome.text.toString(),fone.text.toString(),email.text.toString())

                        CoroutineScope(Dispatchers.IO).launch {
                            db.produtoDAO().atualizarContato(produtoUpdate)
                        }

                        findNavController().popBackStack()
                        true
                    }
                    R.id.action_excluirProduto ->{

                        val db = ProdutoDatabase.getDatabase(requireActivity().applicationContext)

                        CoroutineScope(Dispatchers.IO).launch {
                            db.produtoDAO().apagarContato(produto)
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