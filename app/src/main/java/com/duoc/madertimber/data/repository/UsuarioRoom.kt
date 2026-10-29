package com.duoc.madertimber.data.repository

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query

// Solo se guardan nombre y correo; la contraseña nunca se almacena
@Entity(tableName = "usuario")
data class UsuarioGuardado(
    @PrimaryKey val id: Int = 1,
    val nombre: String,
    val correo: String
)

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuario WHERE id = 1")
    suspend fun leer(): UsuarioGuardado?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(usuario: UsuarioGuardado)
}

class UsuarioRepository(private val dao: UsuarioDao) {
    suspend fun leer(): UsuarioGuardado? = dao.leer()
    suspend fun guardar(nombre: String, correo: String) = dao.guardar(UsuarioGuardado(nombre = nombre, correo = correo))
}
