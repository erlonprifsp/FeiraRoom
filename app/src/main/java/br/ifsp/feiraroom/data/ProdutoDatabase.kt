package br.ifsp.feiraroom.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Produto::class], version = 1)
abstract class ProdutoDatabase: RoomDatabase() {
    abstract fun produtoDAO(): ProdutoDAO

    companion object {
        @Volatile
        private var INSTANCE: ProdutoDatabase? = null

        fun getDatabase(context: Context): ProdutoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProdutoDatabase::class.java,
                    "feiraroom.db"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}