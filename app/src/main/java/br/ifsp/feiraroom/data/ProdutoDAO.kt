package br.ifsp.feiraroom.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProdutoDAO {
    @Insert
    fun inserirContato(produto: Produto)

    @Update
    suspend fun atualizarContato (produto: Produto)

    @Delete
    suspend fun apagarContato(produto: Produto)

    @Query("SELECT * FROM produto ORDER BY nome")
    suspend fun listarProdutos(): List<Produto>
}