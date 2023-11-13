package br.ifsp.agendaroom.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Contato (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val nome:String,
    val fone:String,
    val email:String?
): Serializable